package com.cdzg.xzshop.service.Impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.cdzg.xzshop.mapper.GoodsSpuMapper;
import java.util.List;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.service.GoodsSpuService;

@Service
public class GoodsSpuServiceImpl implements GoodsSpuService {

    @Resource
    private GoodsSpuMapper goodsSpuMapper;

    @Override
    public int updateBatch(List<GoodsSpu> list) {
        return goodsSpuMapper.updateBatch(list);
    }

    @Override
    public int updateBatchSelective(List<GoodsSpu> list) {
        return goodsSpuMapper.updateBatchSelective(list);
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
    public int deleteByPrimaryKey(Long id) {
        return goodsSpuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(GoodsSpu record) {
        return goodsSpuMapper.insert(record);
    }

    @Override
    public int insertSelective(GoodsSpu record) {
        return goodsSpuMapper.insertSelective(record);
    }

    @Override
    public GoodsSpu selectByPrimaryKey(Long id) {
        return goodsSpuMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(GoodsSpu record) {
        return goodsSpuMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(GoodsSpu record) {
        return goodsSpuMapper.updateByPrimaryKey(record);
    }
}

