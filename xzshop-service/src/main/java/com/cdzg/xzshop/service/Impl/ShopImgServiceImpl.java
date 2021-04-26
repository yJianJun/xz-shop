package com.cdzg.xzshop.service.Impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.cdzg.xzshop.mapper.ShopImgMapper;
import com.cdzg.xzshop.domain.ShopImg;
import com.cdzg.xzshop.service.ShopImgService;
@Service
public class ShopImgServiceImpl implements ShopImgService{

    @Resource
    private ShopImgMapper shopImgMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return shopImgMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ShopImg record) {
        return shopImgMapper.insert(record);
    }

    @Override
    public int insertOrUpdate(ShopImg record) {
        return shopImgMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(ShopImg record) {
        return shopImgMapper.insertOrUpdateSelective(record);
    }

    @Override
    public int insertSelective(ShopImg record) {
        return shopImgMapper.insertSelective(record);
    }

    @Override
    public ShopImg selectByPrimaryKey(Long id) {
        return shopImgMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ShopImg record) {
        return shopImgMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ShopImg record) {
        return shopImgMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateBatch(List<ShopImg> list) {
        return shopImgMapper.updateBatch(list);
    }

    @Override
    public int updateBatchSelective(List<ShopImg> list) {
        return shopImgMapper.updateBatchSelective(list);
    }

    @Override
    public int batchInsert(List<ShopImg> list) {
        return shopImgMapper.batchInsert(list);
    }

}
