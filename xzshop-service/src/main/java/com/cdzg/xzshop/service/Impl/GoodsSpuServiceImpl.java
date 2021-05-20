package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.universal.vo.response.user.UserBaseInfoVo;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.componet.SnowflakeIdWorker;
import com.cdzg.xzshop.constant.PaymentType;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.domain.GoodsSpuSales;
import com.cdzg.xzshop.domain.SearchHistory;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.mapper.GoodsSpuMapper;
import com.cdzg.xzshop.mapper.GoodsSpuSalesMapper;
import com.cdzg.xzshop.mapper.SearchHistoryMapper;
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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GoodsSpuServiceImpl extends ServiceImpl<GoodsSpuMapper, GoodsSpu> implements GoodsSpuService {

    @Resource
    private GoodsSpuMapper goodsSpuMapper;

    @Resource
    ElasticsearchTemplate template;

    @Resource
    private SearchHistoryMapper historyMapper;

    @Resource
    private GoodsSpuRepository goodsSpuRepository;

    @Resource
    private GoodsSpuSalesMapper salesMapper;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    private ShopInfoService shopInfoService;

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
        to.setSales((spuSales != null) ? spuSales.getSales() : null);
        return to;
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
}









