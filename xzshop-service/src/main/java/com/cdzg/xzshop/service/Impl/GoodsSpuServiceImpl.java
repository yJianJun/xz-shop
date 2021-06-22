package com.cdzg.xzshop.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdzg.xzshop.domain.GoodsSpu;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.universal.vo.response.user.UserBaseInfoVo;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.componet.SnowflakeIdWorker;
import com.cdzg.xzshop.constant.PaymentType;
import com.cdzg.xzshop.domain.*;
import com.cdzg.xzshop.mapper.GoodsSpuMapper;
import com.cdzg.xzshop.mapper.GoodsSpuSalesMapper;
import com.cdzg.xzshop.mapper.SearchHistoryMapper;
import com.cdzg.xzshop.mapper.UserGoodsFavoritesMapper;
import com.cdzg.xzshop.repository.GoodsSpuRepository;
import com.cdzg.xzshop.service.GoodsSpuService;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.to.admin.GoodsSpuTo;
import com.cdzg.xzshop.to.app.GoodsSpuHomePageTo;
import com.cdzg.xzshop.utils.PageUtil;
import com.cdzg.xzshop.vo.admin.GoodsSpuAddVo;
import com.cdzg.xzshop.vo.admin.GoodsSpuUpdateVO;
import com.cdzg.xzshop.vo.app.GoodsSpuSearchPageVo;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.cdzg.xzshop.vo.order.request.CommitOrderGoodsReqVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoodsSpuServiceImpl extends ServiceImpl<GoodsSpuMapper, GoodsSpu> implements GoodsSpuService {

    @Resource
    private GoodsSpuMapper goodsSpuMapper;

    @Resource
    ElasticsearchTemplate template;

    @Resource
    private SearchHistoryMapper historyMapper;

    @Resource
    private UserGoodsFavoritesMapper favoritesMapper;

    @Resource
    private GoodsSpuRepository goodsSpuRepository;

    @Resource
    private GoodsSpuSalesMapper salesMapper;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    private ShopInfoService shopInfoService;

    @Resource
    private GoodsSpuSalesMapper getSalesMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateBatch(List<GoodsSpu> list) {
        return goodsSpuMapper.updateBatch(list);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int batchInsert(List<GoodsSpu> list) {
        return goodsSpuMapper.batchInsert(list);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertOrUpdate(GoodsSpu record) {
        return goodsSpuMapper.insertOrUpdate(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertOrUpdateSelective(GoodsSpu record) {
        return goodsSpuMapper.insertOrUpdateSelective(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insert(GoodsSpu record) {
        return goodsSpuMapper.insert(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void add(GoodsSpuAddVo addVo, UserBaseInfoVo userBaseInfoVo) {

        GoodsSpu goodsSpu = GoodsSpu.builder().build();
        BeanUtils.copyProperties(addVo, goodsSpu);

        goodsSpu.setCreateUser(userBaseInfoVo.getUserName());
        goodsSpu.setSpuNo(snowflakeIdWorker.nextId());

        BigInteger organizationId = userBaseInfoVo.getOrganizationId();
        ShopInfo shopInfo = shopInfoService.findOneByShopUnion(organizationId + "");

        if (Objects.isNull(shopInfo)) {
            throw new BaseException(ResultCode.DATA_ERROR.getCode(), "你所在的工会没有创建店铺！");
        }

        goodsSpu.setShopId(shopInfo.getId());
        goodsSpu.setGmtCreate(LocalDateTime.now());
        goodsSpuMapper.insert(goodsSpu);
        goodsSpuRepository.save(goodsSpu);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchPutOnDown(List<Long> list, Boolean flag) {

        if (flag) {
            updateStatusAndGmtPutOnTheShelfByIdIn(flag, LocalDateTime.now(), list);
        } else {
            updateStatusAndGmtPutOnTheShelfByIdIn(flag, LocalDateTime.parse("1000-01-01T00:00:00"), list);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateStatusAndGmtPutOnTheShelfByIdIn(Boolean updatedStatus, LocalDateTime updatedGmtPutOnTheShelf, Collection<Long> idCollection) {
        Iterable<GoodsSpu> goodsSpus = goodsSpuRepository.findAllById(idCollection);
        for (Iterator<GoodsSpu> iter = goodsSpus.iterator(); iter.hasNext();) {
            GoodsSpu spu = (GoodsSpu)iter.next();
            spu.setStatus(updatedStatus);
            spu.setGmtPutOnTheShelf(updatedGmtPutOnTheShelf);
            goodsSpuRepository.save(spu);
        }
        return goodsSpuMapper.updateStatusAndGmtPutOnTheShelfByIdIn(updatedStatus, updatedGmtPutOnTheShelf, idCollection);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update(GoodsSpuUpdateVO vo) {

        Long spuNo = vo.getSpuNo();
        GoodsSpu goodsSpu = goodsSpuMapper.findOneBySpuNoAndIsDeleteFalse(spuNo);

        if (Objects.nonNull(goodsSpu)) {

            BeanUtils.copyProperties(vo, goodsSpu, "spuNo");
            goodsSpuMapper.insertOrUpdate(goodsSpu);
            goodsSpuRepository.save(goodsSpu);
        } else {
            throw new BaseException(ResultCode.DATA_ERROR);
        }
    }

    @Override
    public PageResultVO<GoodsSpuTo> page(int page, int pageSize, Boolean status, String goodsName, LocalDateTime minGmtPutOnTheShelf, LocalDateTime maxGmtPutOnTheShelf, Long spuNo, Long categoryIdLevel1, Long categoryIdLevel2, String shopName) {
        PageHelper.startPage(page, pageSize);
        PageResultVO<GoodsSpu> resultVO = PageUtil.transform(new PageInfo(goodsSpuMapper.findByStatusAndGoodsNameAndGmtPutOnTheShelfBetweenEqualAndSpuNoAndCategoryIdLevel1AndCategoryIdLevel2AndIsDeleteFalse(status, goodsName, minGmtPutOnTheShelf, maxGmtPutOnTheShelf, spuNo, categoryIdLevel1, categoryIdLevel2, shopName)));

        List<GoodsSpu> goodsSpus = resultVO.getData();
        List<GoodsSpuTo> goodsSpuTos = new ArrayList<>();
        for (GoodsSpu goodsSpu : goodsSpus) {

            GoodsSpuTo spuTo = new GoodsSpuTo();
            BeanUtils.copyProperties(goodsSpu, spuTo);
            ShopInfo shopInfo = shopInfoService.getById(goodsSpu.getShopId());
            spuTo.setShopName(shopInfo.getShopName());

            GoodsSpuSales spuSales = salesMapper.findOneBySpuNo(goodsSpu.getSpuNo());
            spuTo.setSales((spuSales != null) ? spuSales.getSales() : 0);
            goodsSpuTos.add(spuTo);
        }

        PageResultVO<GoodsSpuTo> pageResultVO = new PageResultVO<>();
        BeanUtils.copyProperties(resultVO, pageResultVO);
        pageResultVO.setData(goodsSpuTos);
        return pageResultVO;
    }


    @Override
    public PageResultVO<GoodsSpu> homePage(int page, int pageSize, PaymentType paymentMethod, Boolean sort, Boolean type) {

        if (!type) {
            if (PaymentType.Integral == paymentMethod) {

                PageHelper.startPage(page, pageSize);
                return PageUtil.transform(new PageInfo(goodsSpuMapper.findByPaymentMethodOrderByFractionPrice(paymentMethod, sort)));
            }
            PageHelper.startPage(page, pageSize);
            return PageUtil.transform(new PageInfo(goodsSpuMapper.findByPaymentMethodOrderBySales(paymentMethod, sort)));

        } else {
            PageHelper.startPage(page, pageSize);
            return PageUtil.transform(new PageInfo(goodsSpuMapper.findByPaymentMethodOrderByGmtPutOnTheShelf(paymentMethod, sort)));
        }
    }

    @Override
    public PageResultVO<GoodsSpuHomePageTo> spuWithSalesByPage(PageResultVO<GoodsSpu> pageResultVO) {

        List<GoodsSpu> data = pageResultVO.getData();
        List<GoodsSpuHomePageTo> homePageTos = new ArrayList<>();
        for (GoodsSpu spu : data) {

            GoodsSpuHomePageTo to = spuWithSales(spu);
            homePageTos.add(to);
        }

        PageResultVO<GoodsSpuHomePageTo> resultVO = new PageResultVO<>();
        BeanUtils.copyProperties(pageResultVO, resultVO);
        resultVO.setData(homePageTos);
        return resultVO;
    }

    @Override
    public GoodsSpuHomePageTo spuWithSales(GoodsSpu spu) {
        GoodsSpuHomePageTo to = new GoodsSpuHomePageTo();
        GoodsSpuSales spuSales = salesMapper.findOneBySpuNo(spu.getSpuNo());
        BeanUtils.copyProperties(spu, to);
        to.setSales((spuSales != null) ? spuSales.getSales() : 0);
        return to;
    }

    @Override
    public GoodsSpuHomePageTo spuWithSalesIsCollect(GoodsSpu spu, String userId) {
        GoodsSpuHomePageTo to = spuWithSales(spu);
        boolean collect = isCollect(spu.getSpuNo(), userId);
        to.setIsCollect(collect);
        return to;
    }

    private boolean isCollect(Long spuNo, String userId) {

        UserGoodsFavorites userGoodsFavorites = favoritesMapper.findOneByUserIdAndSpuNo(userId, spuNo);
        return Objects.nonNull(userGoodsFavorites);
    }


    @Override
    public GoodsSpu findOneBySpuNoAndIsDeleteFalse(Long spuNo) {
        return goodsSpuMapper.findOneBySpuNoAndIsDeleteFalse(spuNo);
    }

    @Override
    public PageResultVO<GoodsSpu> search(GoodsSpuSearchPageVo vo, String customerId) {

        String keyWord = vo.getKeyWord();
        PageRequest pageRequest = PageRequest.of(vo.getCurrentPage() - 1, vo.getPageSize());
        Page goodsSpus = goodsSpuRepository.search(keyWord, pageRequest);

        if (!CollectionUtils.isEmpty(goodsSpus.getContent())) {
            if (StringUtils.isNotBlank(keyWord)) {

                SearchHistory history = historyMapper.findOneByKeyWordAndUserId(keyWord, Long.parseLong(customerId));
                if (Objects.nonNull(history)) {

                    history.setCount(history.getCount() + 1);
                } else {
                    history = SearchHistory.builder()
                            .keyWord(keyWord)
                            .userId(Long.parseLong(customerId))
                            .count(1L)
                            .build();
                }
                historyMapper.insertOrUpdate(history);
            }
        }
        return PageUtil.transform(goodsSpus);

        //String keyWord = vo.getKeyWord();
        //NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withFilter(QueryBuilders.boolQuery().filter(
        //        QueryBuilders.boolQuery()
        //                .should(QueryBuilders.matchQuery("ad_word", keyWord))
        //                .should(QueryBuilders.matchQuery("goods_name", keyWord))
        //                .should(QueryBuilders.termQuery("goods_name.keyword", keyWord))
        //)).withPageable(PageRequest.of(vo.getCurrentPage() - 1, vo.getPageSize())).build();
        //AggregatedPage goodsSpus = template.queryForPage(searchQuery, GoodsSpu.class);
        //return PageUtil.transform(goodsSpus);
    }

    @Override
    public List<GoodsSpu> findByShopIdIn(Collection<Long> shopIdCollection) {
        return goodsSpuMapper.findByShopIdIn(shopIdCollection);
    }

    @Override
    public List<Long> findIdByShopIdIn(Collection<Long> shopIdCollection) {
        return goodsSpuMapper.findIdByShopIdIn(shopIdCollection);
    }

    @Override
    public Map<Long, GoodsSpu> getMapByIds(List<Long> ids) {
        Map<Long, GoodsSpu> map = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(ids)) {
            List<GoodsSpu> list = this.listByIds(ids);
            for (GoodsSpu goods : list) {
                map.put(goods.getId(), goods);
            }
        }
        return map;
    }

    @Override
    public List<GoodsSpu> findBySpuNoInAndIsDeleteFalseAndStatusTrue(Collection<Long> spuNoCollection) {
        return goodsSpuMapper.findBySpuNoInAndIsDeleteFalseAndStatusTrue(spuNoCollection);
    }

    @Override
    public List<GoodsSpu> findByShopIdAndIsDeleteFalseAndStatusTrue(Long shopId) {
        return goodsSpuMapper.findByShopIdAndIsDeleteFalseAndStatusTrue(shopId);
    }


    @Override
    public PageResultVO<GoodsSpu> pageByShop(int page, int pageSize, Long shopId) {
        PageHelper.startPage(page, pageSize);
        return PageUtil.transform(new PageInfo(goodsSpuMapper.findByShopIdAndIsDeleteFalseAndStatusTrue(shopId)));
    }


    @Autowired
    private GoodsSpuSalesMapper spuSalesMapper;

    /**
     * 修改提交订单后的商品库存和销量
     * 销量：如果存在，修改，不存在，新增
     *
     * @param commitGoodsList
     */
    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsStockAndSales(List<CommitOrderGoodsReqVO> commitGoodsList) {
        try {
            //批量修改库存
            baseMapper.batchUpdateGoodsStock(commitGoodsList);
            //批量修改销量
            List<String> goodsIds = commitGoodsList.stream().map(CommitOrderGoodsReqVO::getGoodsId).collect(Collectors.toList());
            //查询所有spuno
            List<GoodsSpu> goodsSpuList = baseMapper.selectBatchIds(goodsIds);
            if (!CollectionUtils.isEmpty(goodsSpuList)) {
                LocalDateTime date = LocalDateTime.now();
                List<Long> spuNos = goodsSpuList.stream().map(GoodsSpu::getSpuNo).collect(Collectors.toList());
                LambdaQueryWrapper<GoodsSpuSales> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(GoodsSpuSales::getSpuNo, spuNos);
                List<GoodsSpuSales> goodsSpuSales = getSalesMapper.selectList(queryWrapper);
                List<GoodsSpuSales> addList = new ArrayList<>();
                List<GoodsSpuSales> updateList = new ArrayList<>();
                List<Long> updateSpuNos = new ArrayList<>();
                if (!CollectionUtils.isEmpty(goodsSpuSales)) {
                    updateSpuNos = goodsSpuSales.stream().map(GoodsSpuSales::getSpuNo).collect(Collectors.toList());
                    goodsSpuSales.forEach(g -> goodsSpuList.forEach(gs -> commitGoodsList.forEach(c -> {
                        if (g.getSpuNo().equals(gs.getSpuNo()) && (gs.getId() + "").equals(c.getGoodsId())) {
                            GoodsSpuSales spuSales = GoodsSpuSales.builder().build();
                            spuSales.setSpuNo(g.getSpuNo());
                            //此处sales保存的是需要修改的数量，而非全量
                            spuSales.setSales(Long.valueOf(c.getGoodsCount()));
                            spuSales.setGmtUpdate(date);
                            updateList.add(spuSales);
                        }
                    })));
                    //修改
                    spuSalesMapper.batchUpdateSales(updateList);
                }
                if (!CollectionUtils.isEmpty(updateSpuNos)) {
                    spuNos.removeAll(updateSpuNos);
                }
                if (!CollectionUtils.isEmpty(spuNos)) {
                    goodsSpuList.forEach(g -> commitGoodsList.forEach(c -> {
                        if (spuNos.contains(g.getSpuNo()) && (g.getId() + "").equals(c.getGoodsId())) {
                            GoodsSpuSales spuSales = GoodsSpuSales.builder().build();
                            spuSales.setSpuNo(g.getSpuNo());
                            spuSales.setSales(Long.valueOf(c.getGoodsCount()));
                            spuSales.setGmtCreate(date);
                            spuSales.setGmtUpdate(date);
                            addList.add(spuSales);
                        }
                    }));
                    //新增
                    spuSalesMapper.batchInsert(addList);
                }
            }
        } catch (Exception e) {
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("订单批量修改库存销量error:{} , request: {}", e.getMessage(), JSONObject.toJSONString(commitGoodsList));
        }

    }
}







