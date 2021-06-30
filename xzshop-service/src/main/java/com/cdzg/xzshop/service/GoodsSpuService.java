package com.cdzg.xzshop.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.enums.PaymentType;
import java.time.LocalDateTime;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cdzg.universal.vo.response.user.UserBaseInfoVo;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.to.admin.GoodsSpuTo;
import com.cdzg.xzshop.to.app.GoodsSpuHomePageTo;
import com.cdzg.xzshop.vo.admin.GoodsSpuAddVo;
import com.cdzg.xzshop.vo.admin.GoodsSpuUpdateVO;
import com.cdzg.xzshop.vo.app.GoodsSpuSearchPageVo;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.cdzg.xzshop.vo.order.request.CommitOrderGoodsReqVO;

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

    PageResultVO<GoodsSpu> homePage(int page, int pageSize, PaymentType paymentMethod, Boolean sort, Boolean type);

    PageResultVO<GoodsSpuHomePageTo> spuWithSalesByPage(PageResultVO<GoodsSpu> pageResultVO);

    GoodsSpuHomePageTo spuWithSales(GoodsSpu spu);

    GoodsSpuHomePageTo spuWithSalesIsCollect(GoodsSpu spu,String userId);

	GoodsSpu findOneBySpuNoAndIsDeleteFalse(Long spuNo);

    PageResultVO<GoodsSpu> search(GoodsSpuSearchPageVo vo, String customerId);

	List<GoodsSpu> findByShopIdIn(Collection<Long> shopIdCollection);

	List<Long> findIdByShopIdIn(Collection<Long> shopIdCollection);

	Map<Long, GoodsSpu> getMapByIds(List<Long> ids);



	List<GoodsSpu> findBySpuNoInAndIsDeleteFalseAndStatusTrue(Collection<Long> spuNoCollection);



	List<GoodsSpu> findByShopIdAndIsDeleteFalseAndStatusTrue(Long shopId);


    PageResultVO<GoodsSpu> pageByShop(int page, int pageSize, Long shopId);
    /**
     * 修改提交订单后的商品库存和销量
     * @param commitGoodsList
     */
    void updateGoodsStockAndSales(List<CommitOrderGoodsReqVO> commitGoodsList);

	GoodsSpu findOneBySpuNo(Long spuNo);

    List<GoodsSpu> findByPaymentMethodOrderByPrice(PaymentType paymentMethod, Boolean sort);

	List<GoodsSpu> findBySpuNoIn(Collection<Long> spuNoCollection);

    PageResultVO<GoodsSpu> findBySpuNoInwithPage(int page, int pageSize, Collection<Long> spuNoCollection);
}









