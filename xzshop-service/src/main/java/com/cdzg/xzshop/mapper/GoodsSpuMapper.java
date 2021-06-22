package com.cdzg.xzshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdzg.xzshop.constant.PaymentType;
import com.cdzg.xzshop.domain.GoodsSpu;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.cdzg.xzshop.to.app.GoodsSpuHomePageTo;
import com.cdzg.xzshop.vo.order.request.CommitOrderGoodsReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsSpuMapper extends BaseMapper<GoodsSpu> {
    int updateBatch(List<GoodsSpu> list);

    int batchInsert(@Param("list") List<GoodsSpu> list);

    int insertOrUpdate(GoodsSpu record);

    int insertOrUpdateSelective(GoodsSpu record);

    int updateStatusAndGmtPutOnTheShelfByIdIn(@Param("updatedStatus") Boolean updatedStatus, @Param("updatedGmtPutOnTheShelf") LocalDateTime updatedGmtPutOnTheShelf, @Param("idCollection") Collection<Long> idCollection);

    GoodsSpu findOneBySpuNoAndIsDeleteFalse(@Param("spuNo") Long spuNo);

    GoodsSpu findOneBySpuNo(@Param("spuNo")Long spuNo);

    List<GoodsSpu> findByStatusAndGoodsNameAndGmtPutOnTheShelfBetweenEqualAndSpuNoAndCategoryIdLevel1AndCategoryIdLevel2AndIsDeleteFalse(@Param("status") Boolean status, @Param("goodsName") String goodsName, @Param("minGmtPutOnTheShelf") LocalDateTime minGmtPutOnTheShelf, @Param("maxGmtPutOnTheShelf") LocalDateTime maxGmtPutOnTheShelf, @Param("spuNo") Long spuNo, @Param("categoryIdLevel1") Long categoryIdLevel1, @Param("categoryIdLevel2") Long categoryIdLevel2, @Param("shopName") String shopName);

    List<GoodsSpu> findByGoodsNameLike(@Param("likeGoodsName")String likeGoodsName);

    List<GoodsSpu> findByShopIdIn(@Param("shopIdCollection") Collection<Long> shopIdCollection);

    List<Long> findIdByShopIdIn(@Param("shopIdCollection") Collection<Long> shopIdCollection);

    List<GoodsSpuHomePageTo> findByPaymentMethodOrderBySales(@Param("paymentMethod") PaymentType paymentMethod, @Param("sort") Boolean sort);

    List<GoodsSpu> findByPaymentMethodOrderByFractionPrice(@Param("paymentMethod") PaymentType paymentMethod, @Param("sort") Boolean sort);

    List<GoodsSpu> findByPaymentMethodOrderByGmtPutOnTheShelf(@Param("paymentMethod")PaymentType paymentMethod,@Param("sort") Boolean sort);

    List<GoodsSpu> findBySpuNoInAndIsDeleteFalseAndStatusTrue(@Param("spuNoCollection")Collection<Long> spuNoCollection);

    List<GoodsSpu> findByShopIdAndIsDeleteFalseAndStatusTrue(@Param("shopId")Long shopId);

    /**
     * 批量库存的修改操作
     * @param commitGoodsList
     * @return
     */
    int batchUpdateGoodsStock(@Param("goodsList") List<CommitOrderGoodsReqVO> commitGoodsList);

}