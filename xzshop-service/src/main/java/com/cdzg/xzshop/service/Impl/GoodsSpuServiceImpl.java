package com.cdzg.xzshop.service.Impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.constant.PaymentType;

import com.cdzg.universal.vo.response.user.UserBaseInfoVo;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.componet.SnowflakeIdWorker;
import com.cdzg.xzshop.domain.GoodsSpuSales;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.mapper.GoodsSpuSalesMapper;
import com.cdzg.xzshop.repository.GoodsSpuRepository;
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
import org.apache.commons.compress.utils.Lists;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.cdzg.xzshop.mapper.GoodsSpuMapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.service.GoodsSpuService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodsSpuServiceImpl extends ServiceImpl<GoodsSpuMapper, GoodsSpu> implements GoodsSpuService {

    @Resource
    private GoodsSpuMapper goodsSpuMapper;

    @Resource
    ElasticsearchTemplate template;

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
    public List<GoodsSpu> findByPaymentMethodOrderByFractionPrice(PaymentType paymentMethod) {
        return goodsSpuMapper.findByPaymentMethodOrderByFractionPrice(paymentMethod);
    }

    @Override
    public List<GoodsSpuHomePageTo> findByPaymentMethodOrderBySales(PaymentType paymentMethod) {
        return goodsSpuMapper.findByPaymentMethodOrderBySales(paymentMethod);
    }

    @Override
    public PageResultVO<GoodsSpuHomePageTo> homePage(int page, int pageSize, PaymentType paymentMethod, Boolean sort) {

        if (sort) {
            PageHelper.startPage(page, pageSize);
            PageResultVO<GoodsSpu> pageResultVO = PageUtil.transform(new PageInfo(goodsSpuMapper.findByPaymentMethodOrderByFractionPrice(paymentMethod)));
            return spuWithSalesByPage(pageResultVO);
        } else {

            PageHelper.startPage(page, pageSize);
            return PageUtil.transform(new PageInfo(goodsSpuMapper.findByPaymentMethodOrderBySales(paymentMethod)));
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
	public GoodsSpu findOneBySpuNoAndIsDeleteFalse(Long spuNo){
		 return goodsSpuMapper.findOneBySpuNoAndIsDeleteFalse(spuNo);
	}

    @Override
    public PageResultVO<GoodsSpu> search(GoodsSpuSearchPageVo vo) {

        PageRequest pageRequest = PageRequest.of(vo.getCurrentPage() - 1, vo.getPageSize());
        Page goodsSpus = goodsSpuRepository.search(vo.getKeyWord(), pageRequest);
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
	public List<GoodsSpu> findByShopIdIn(Collection<Long> shopIdCollection){
		 return goodsSpuMapper.findByShopIdIn(shopIdCollection);
	}

	@Override
	public List<Long> findIdByShopIdIn(Collection<Long> shopIdCollection){
		 return goodsSpuMapper.findIdByShopIdIn(shopIdCollection);
	}






}









