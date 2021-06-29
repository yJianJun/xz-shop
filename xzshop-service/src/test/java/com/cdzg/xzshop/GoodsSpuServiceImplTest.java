package com.cdzg.xzshop;

import com.cdzg.xzshop.componet.SnowflakeIdWorker;
import com.cdzg.xzshop.constant.PaymentType;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.mapper.GoodsSpuMapper;
import com.cdzg.xzshop.repository.GoodsSpuRepository;
import com.cdzg.xzshop.service.GoodsSpuService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
class GoodsSpuServiceImplTest extends BaseTest {

    //@BeforeEach
    //void setUp() {
    //}
    //
    //@AfterEach
    //void tearDown() {
    //}

    @Autowired
    GoodsSpuService goodsSpuService;

    @Autowired
    GoodsSpuRepository goodsSpuRepository;

    @Autowired
    GoodsSpuMapper goodsSpuMapper;

    @Autowired
    SnowflakeIdWorker snowflakeIdWorker;

    @Test
    void insertOrUpdate() {

        //GoodsSpu goodsSpu = GoodsSpu.builder()
        //        .status(false)
        //        .stock(10L)
        //        .showImgs(Lists.newArrayList("gdfgdfgddfgsdfg","agfaddfsfasd"))
        //        .shopId(2L)
        //        .spuNo(snowflakeIdWorker.nextId())
        //        .promotionPrice(BigDecimal.ZERO)
        //        .price(BigDecimal.ONE)
        //        .paymentMethod(PaymentType.Integral)
        //        .gmtCreate(LocalDateTime.now())
        //        .gmtUpdate(LocalDateTime.now())
        //        .fractionPrice(BigDecimal.TEN)
        //        .descImgs("dsfdasfasdfasdfas")
        //        .createUser("叶建军")
        //        .categoryIdLevel2(2L)
        //        .categoryIdLevel1(1L)
        //        .goodsName("测试")
        //        .adWord("测试")
        //        .isDelete(false)
        //        .build();
        //
        //goodsSpuRepository.save(goodsSpu);
        Page<GoodsSpu> page = goodsSpuRepository.findAll(PageRequest.of(0, 10));
        log.info("page:{}",page.getContent());

    }

    @Test
    void get(){
        Page<GoodsSpu> page = goodsSpuRepository.findAll(PageRequest.of(0, 10));
        log.info("page:{}",page.getContent());
    }

    @Test
    void sync(){
        List<GoodsSpu> goodsSpus = goodsSpuMapper.selectByMap(Maps.newHashMap());
        log.info("商品:{}",goodsSpus);
        goodsSpuRepository.saveAll(goodsSpus);
    }
}