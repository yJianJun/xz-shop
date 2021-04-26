package com.cdzg.xzshop.service.Impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.cdzg.xzshop.domain.ReturnGoodsInfo;
import com.cdzg.xzshop.mapper.ReturnGoodsInfoMapper;
import com.cdzg.xzshop.service.ReturnGoodsInfoService;
@Service
public class ReturnGoodsInfoServiceImpl implements ReturnGoodsInfoService{

    @Resource
    private ReturnGoodsInfoMapper returnGoodsInfoMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return returnGoodsInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ReturnGoodsInfo record) {
        return returnGoodsInfoMapper.insert(record);
    }

    @Override
    public int insertOrUpdate(ReturnGoodsInfo record) {
        return returnGoodsInfoMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(ReturnGoodsInfo record) {
        return returnGoodsInfoMapper.insertOrUpdateSelective(record);
    }

    @Override
    public int insertSelective(ReturnGoodsInfo record) {
        return returnGoodsInfoMapper.insertSelective(record);
    }

    @Override
    public ReturnGoodsInfo selectByPrimaryKey(Long id) {
        return returnGoodsInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ReturnGoodsInfo record) {
        return returnGoodsInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ReturnGoodsInfo record) {
        return returnGoodsInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateBatch(List<ReturnGoodsInfo> list) {
        return returnGoodsInfoMapper.updateBatch(list);
    }

    @Override
    public int updateBatchSelective(List<ReturnGoodsInfo> list) {
        return returnGoodsInfoMapper.updateBatchSelective(list);
    }

    @Override
    public int batchInsert(List<ReturnGoodsInfo> list) {
        return returnGoodsInfoMapper.batchInsert(list);
    }

}
