package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.mapper.ShopInfoMapper;
import com.cdzg.xzshop.service.ShopInfoService;

@Service
public class ShopInfoServiceImpl extends ServiceImpl<ShopInfoMapper, ShopInfo> implements ShopInfoService {

    @Resource
    private ShopInfoMapper shopInfoMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return shopInfoMapper.deleteByPrimaryKey(id);
    }

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
    public int insertSelective(ShopInfo record) {
        return shopInfoMapper.insertSelective(record);
    }

    @Override
    public ShopInfo selectByPrimaryKey(Long id) {
        return shopInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ShopInfo record) {
        return shopInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ShopInfo record) {
        return shopInfoMapper.updateByPrimaryKey(record);
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

}

