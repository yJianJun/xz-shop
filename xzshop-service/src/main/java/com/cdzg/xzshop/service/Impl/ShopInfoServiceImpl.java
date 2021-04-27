package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.mapper.ShopInfoMapper;
import com.cdzg.xzshop.service.ShopInfoService;

@Service
public class ShopInfoServiceImpl extends ServiceImpl<ShopInfoMapper, ShopInfo> implements ShopInfoService {

    @Resource
    private ShopInfoMapper shopInfoMapper;

    @Override
    public int insert(ShopInfo record) {
        return shopInfoMapper.insert(record);
    }

    @Override
    public int insertOrUpdate(ShopInfo record) {
        return shopInfoMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(ShopInfo record) {
        return shopInfoMapper.insertOrUpdateSelective(record);
    }


    @Override
    public int updateBatch(List<ShopInfo> list) {
        return shopInfoMapper.updateBatch(list);
    }

    @Override
    public int updateBatchSelective(List<ShopInfo> list) {
        return shopInfoMapper.updateBatchSelective(list);
    }

    @Override
    public int batchInsert(List<ShopInfo> list) {
        return shopInfoMapper.batchInsert(list);
    }


    public List<ShopInfo> findAllByShopNameAndStatusAndGmtPutOnTheShelfBetweenEqual(String shopName, Boolean status, LocalDateTime minGmtPutOnTheShelf, LocalDateTime maxGmtPutOnTheShelf) {
        return shopInfoMapper.findAllByShopNameAndStatusAndGmtPutOnTheShelfBetweenEqual(shopName, status, minGmtPutOnTheShelf, maxGmtPutOnTheShelf);
    }

    @Override
    public PageInfo<ShopInfo> findAllByShopNameAndStatusAndGmtPutOnTheShelfBetweenEqualwithPage(int page, int pageSize, String shopName, Boolean status, LocalDateTime minGmtPutOnTheShelf, LocalDateTime maxGmtPutOnTheShelf) {
        PageHelper.startPage(page, pageSize);
        return new PageInfo<>(shopInfoMapper.findAllByShopNameAndStatusAndGmtPutOnTheShelfBetweenEqual(shopName, status, minGmtPutOnTheShelf, maxGmtPutOnTheShelf));
    }
}





