package com.cdzg.xzshop.service.Impl;

import com.beust.jcommander.internal.Lists;
import com.cdzg.xzshop.to.admin.GoodsCategoryTo;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.cdzg.xzshop.domain.GoodsCategory;
import com.cdzg.xzshop.mapper.GoodsCategoryMapper;

import java.util.*;

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

    @Override
    public List<GoodsCategory> findByLevelAndCategoryNameLike(Integer level, String likeCategoryName) {
        return goodsCategoryMapper.findByLevelAndCategoryNameLike(level, likeCategoryName);
    }

    @Override
    public List<GoodsCategoryTo> list(Integer level, String likeCategoryName) {

        List<GoodsCategory> data = goodsCategoryMapper.findByLevelAndCategoryNameLike(level, likeCategoryName);
        List<GoodsCategoryTo> categoryTos = new ArrayList<>();

        Map<GoodsCategory, List<GoodsCategory>> map = new HashMap<>();

        if (2 == level) {
            List<GoodsCategory> sons = data;
            for (GoodsCategory son : sons) {

                GoodsCategory parent = findOneByIdAndLevel(son.getParentId(), 1);
                List<GoodsCategory> subs = map.get(parent);
                if (CollectionUtils.isNotEmpty(subs)) {
                    subs.add(son);
                    map.put(parent, subs);
                } else {
                    map.put(parent, Lists.newArrayList(son));
                }
            }

            for (Map.Entry<GoodsCategory, List<GoodsCategory>> entry : map.entrySet()) {

                GoodsCategory goodsCategory = entry.getKey();
                GoodsCategoryTo categoryTo = new GoodsCategoryTo();
                BeanUtils.copyProperties(goodsCategory, categoryTo);
                categoryTo.setChildren(entry.getValue());
                categoryTos.add(categoryTo);
            }

        }else {

            List<GoodsCategory> parents = data;
            for (GoodsCategory parent : parents) {

                GoodsCategoryTo categoryTo = new GoodsCategoryTo();
                BeanUtils.copyProperties(parent, categoryTo);
                List<GoodsCategory> subs = findByParentIdAndLevel(parent.getId(), 2);
                categoryTo.setChildren(subs);
                categoryTos.add(categoryTo);
            }
        }
        //yjjtodo 返回数据时将用户id 转成用户名
        return categoryTos;
    }

    @Override
    public List<GoodsCategory> findByParentIdAndLevel(Long parentId, Integer level) {
        return goodsCategoryMapper.findByParentIdAndLevel(parentId, level);
    }

    @Override
    public PageResultVO<GoodsCategory> pageSub(int page, int pageSize, Long parentId, Integer level) {
        PageHelper.startPage(page, pageSize);
        return new PageResultVO<>(goodsCategoryMapper.findByParentIdAndLevel(parentId, level));
    }

    @Override
    public GoodsCategory findOneByIdAndLevel(Long id, Integer level) {
        return goodsCategoryMapper.findOneByIdAndLevel(id, level);
    }


}

