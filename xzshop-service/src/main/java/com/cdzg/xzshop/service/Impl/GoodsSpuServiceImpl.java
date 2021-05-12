package com.cdzg.xzshop.service.Impl;

import com.cdzg.universal.vo.response.user.UserBaseInfoVo;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.componet.SnowflakeIdWorker;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.service.ShopInfoService;
import com.cdzg.xzshop.to.admin.GoodsSpuTo;
import com.cdzg.xzshop.utils.PageUtil;
import com.cdzg.xzshop.vo.admin.GoodsSpuAddVo;
import com.cdzg.xzshop.vo.admin.GoodsSpuUpdateVO;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.cdzg.xzshop.mapper.GoodsSpuMapper;

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
public class GoodsSpuServiceImpl implements GoodsSpuService {

    @Resource
    private GoodsSpuMapper goodsSpuMapper;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    private ShopInfoService shopInfoService;

    @Override
    public int updateBatch(List<GoodsSpu> list) {
        return goodsSpuMapper.updateBatch(list);
    }


    @Override
    public int batchInsert(List<GoodsSpu> list) {
        return goodsSpuMapper.batchInsert(list);
    }

    @Override
    public int insertOrUpdate(GoodsSpu record) {
        return goodsSpuMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(GoodsSpu record) {
        return goodsSpuMapper.insertOrUpdateSelective(record);
    }

    @Override
    public int insert(GoodsSpu record) {
        return goodsSpuMapper.insert(record);
    }

    @Override
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
    public int updateStatusAndGmtPutOnTheShelfByIdIn(Boolean updatedStatus, LocalDateTime updatedGmtPutOnTheShelf, Collection<Long> idCollection) {
        return goodsSpuMapper.updateStatusAndGmtPutOnTheShelfByIdIn(updatedStatus, updatedGmtPutOnTheShelf, idCollection);
    }

    @Override
    public GoodsSpu findOneBySpuNo(Long spuNo) {
        return goodsSpuMapper.findOneBySpuNo(spuNo);
    }

    @Override
    public void update(GoodsSpuUpdateVO vo) {

        Long spuNo = vo.getSpuNo();
        GoodsSpu goodsSpu = goodsSpuMapper.findOneBySpuNo(spuNo);

        if (Objects.nonNull(goodsSpu)) {

            BeanUtils.copyProperties(vo, goodsSpu, "spuNo");
            goodsSpuMapper.insertOrUpdate(goodsSpu);
        } else {
            throw new BaseException(ResultCode.DATA_ERROR);
        }
    }

    @Override
    public PageResultVO<GoodsSpuTo> page(int page, int pageSize, Boolean status, String goodsName, LocalDateTime minGmtPutOnTheShelf, LocalDateTime maxGmtPutOnTheShelf, Long spuNo, Long categoryIdLevel1, Long categoryIdLevel2,String shopName) {
        PageHelper.startPage(page, pageSize);
        PageResultVO<GoodsSpu> resultVO = PageUtil.transform(new PageInfo(goodsSpuMapper.findByStatusAndGoodsNameAndGmtPutOnTheShelfBetweenEqualAndSpuNoAndCategoryIdLevel1AndCategoryIdLevel2(status, goodsName, minGmtPutOnTheShelf, maxGmtPutOnTheShelf, spuNo, categoryIdLevel1, categoryIdLevel2,shopName)));

        List<GoodsSpu> goodsSpus = resultVO.getData();
        List<GoodsSpuTo> goodsSpuTos = new ArrayList<>();
        for (GoodsSpu goodsSpu : goodsSpus) {

            GoodsSpuTo spuTo = new GoodsSpuTo();
            BeanUtils.copyProperties(goodsSpu,spuTo);
            ShopInfo shopInfo = shopInfoService.getById(goodsSpu.getShopId());
            spuTo.setShopName(shopInfo.getShopName());
            goodsSpuTos.add(spuTo);
        }

        PageResultVO<GoodsSpuTo> pageResultVO = new PageResultVO<>();
        BeanUtils.copyProperties(resultVO,pageResultVO);
        pageResultVO.setData(goodsSpuTos);
        return pageResultVO;
    }
}







