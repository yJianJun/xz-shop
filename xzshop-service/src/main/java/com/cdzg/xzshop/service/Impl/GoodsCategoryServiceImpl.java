package com.cdzg.xzshop.service.Impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.cdzg.xzshop.domain.GoodsCategory;
import com.cdzg.xzshop.mapper.GoodsCategoryMapper;
import java.util.List;
import com.cdzg.xzshop.service.GoodsCategoryService;

@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

    @Resource
    private GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return goodsCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(GoodsCategory record) {
        return goodsCategoryMapper.insert(record);
    }

    @Override
    public int insertOrUpdate(GoodsCategory record) {
        return goodsCategoryMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(GoodsCategory record) {
        return goodsCategoryMapper.insertOrUpdateSelective(record);
    }

    @Override
    public int insertSelective(GoodsCategory record) {
        return goodsCategoryMapper.insertSelective(record);
    }

    @Override
    public GoodsCategory selectByPrimaryKey(Long id) {
        return goodsCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(GoodsCategory record) {
        return goodsCategoryMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(GoodsCategory record) {
        return goodsCategoryMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateBatch(List<GoodsCategory> list) {
        return goodsCategoryMapper.updateBatch(list);
    }

    @Override
    public int updateBatchSelective(List<GoodsCategory> list) {
        return goodsCategoryMapper.updateBatchSelective(list);
    }

    @Override
    public int batchInsert(List<GoodsCategory> list) {
        return goodsCategoryMapper.batchInsert(list);
    }

}

