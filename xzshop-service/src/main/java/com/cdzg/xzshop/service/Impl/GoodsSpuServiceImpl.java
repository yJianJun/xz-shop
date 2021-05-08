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

}



