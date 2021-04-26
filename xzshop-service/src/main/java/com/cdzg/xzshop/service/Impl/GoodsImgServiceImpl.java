package com.cdzg.xzshop.service.Impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.cdzg.xzshop.mapper.GoodsImgMapper;
import com.cdzg.xzshop.domain.GoodsImg;
import java.util.List;
import com.cdzg.xzshop.service.GoodsImgService;
@Service
public class GoodsImgServiceImpl implements GoodsImgService{

    @Resource
    private GoodsImgMapper goodsImgMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return goodsImgMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(GoodsImg record) {
        return goodsImgMapper.insert(record);
    }

    @Override
    public int insertOrUpdate(GoodsImg record) {
        return goodsImgMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(GoodsImg record) {
        return goodsImgMapper.insertOrUpdateSelective(record);
    }

    @Override
    public int insertSelective(GoodsImg record) {
        return goodsImgMapper.insertSelective(record);
    }

    @Override
    public GoodsImg selectByPrimaryKey(Long id) {
        return goodsImgMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(GoodsImg record) {
        return goodsImgMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(GoodsImg record) {
        return goodsImgMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateBatch(List<GoodsImg> list) {
        return goodsImgMapper.updateBatch(list);
    }

    @Override
    public int updateBatchSelective(List<GoodsImg> list) {
        return goodsImgMapper.updateBatchSelective(list);
    }

    @Override
    public int batchInsert(List<GoodsImg> list) {
        return goodsImgMapper.batchInsert(list);
    }

}
