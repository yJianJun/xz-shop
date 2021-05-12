package com.cdzg.xzshop.service.Impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.domain.GoodsSpuSales;
import java.util.List;
import com.cdzg.xzshop.mapper.GoodsSpuSalesMapper;
import com.cdzg.xzshop.service.GoodsSpuSalesService;
@Service
public class GoodsSpuSalesServiceImpl extends ServiceImpl<GoodsSpuSalesMapper, GoodsSpuSales> implements GoodsSpuSalesService{

    @Override
    public int updateBatch(List<GoodsSpuSales> list) {
        return baseMapper.updateBatch(list);
    }
    @Override
    public int batchInsert(List<GoodsSpuSales> list) {
        return baseMapper.batchInsert(list);
    }
    @Override
    public int insertOrUpdate(GoodsSpuSales record) {
        return baseMapper.insertOrUpdate(record);
    }
    @Override
    public int insertOrUpdateSelective(GoodsSpuSales record) {
        return baseMapper.insertOrUpdateSelective(record);
    }
}
