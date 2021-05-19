package com.cdzg.xzshop.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.constant.PaymentType;
import java.time.LocalDateTime;

import java.util.Collection;
import java.util.List;

import com.cdzg.universal.vo.response.user.UserBaseInfoVo;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.to.admin.GoodsSpuTo;
import com.cdzg.xzshop.to.app.GoodsSpuHomePageTo;
import com.cdzg.xzshop.vo.admin.GoodsSpuAddVo;
import com.cdzg.xzshop.vo.admin.GoodsSpuUpdateVO;
import com.cdzg.xzshop.vo.app.GoodsSpuSearchPageVo;
import com.cdzg.xzshop.vo.common.PageResultVO;

public interface GoodsSpuService extends IService<GoodsSpu> {


    int updateBatch(List<GoodsSpu> list);

    int batchInsert(List<GoodsSpu> list);

    int insertOrUpdate(GoodsSpu record);

    int insertOrUpdateSelective(GoodsSpu record);

    int insert(GoodsSpu record);

    void add(GoodsSpuAddVo addVo, UserBaseInfoVo userName);

    void batchPutOnDown(List<Long> list, Boolean flag);

    int updateStatusAndGmtPutOnTheShelfByIdIn(Boolean updatedStatus, LocalDateTime updatedGmtPutOnTheShelf, Collection<Long> idCollection);

    void update(GoodsSpuUpdateVO vo);

    PageResultVO<GoodsSpuTo> page(int page, int pageSize, Boolean status, String goodsName, LocalDateTime minGmtPutOnTheShelf, LocalDateTime maxGmtPutOnTheShelf, Long spuNo, Long categoryIdLevel1, Long categoryIdLevel2, String shopName);

    List<GoodsSpu> findByPaymentMethodOrderByFractionPrice(PaymentType paymentMethod);

    List<GoodsSpuHomePageTo> findByPaymentMethodOrderBySales(PaymentType paymentMethod);

    PageResultVO<GoodsSpuHomePageTo> homePage(int page, int pageSize, PaymentType paymentMethod, Boolean sort);

    PageResultVO<GoodsSpuHomePageTo> spuWithSalesByPage(PageResultVO<GoodsSpu> pageResultVO);

    GoodsSpuHomePageTo spuWithSales(GoodsSpu spu);

	GoodsSpu findOneBySpuNoAndIsDeleteFalse(Long spuNo);

    PageResultVO<GoodsSpu> search(GoodsSpuSearchPageVo vo, String customerId);



	List<GoodsSpu> findByShopIdIn(Collection<Long> shopIdCollection);



	List<Long> findIdByShopIdIn(Collection<Long> shopIdCollection);


}









