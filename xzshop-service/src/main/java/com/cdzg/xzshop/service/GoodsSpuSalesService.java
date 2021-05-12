package com.cdzg.xzshop.service;

import com.cdzg.xzshop.domain.GoodsSpuSales;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
public interface GoodsSpuSalesService extends IService<GoodsSpuSales>{


    int updateBatch(List<GoodsSpuSales> list);

    int batchInsert(List<GoodsSpuSales> list);

    int insertOrUpdate(GoodsSpuSales record);

    int insertOrUpdateSelective(GoodsSpuSales record);

}
