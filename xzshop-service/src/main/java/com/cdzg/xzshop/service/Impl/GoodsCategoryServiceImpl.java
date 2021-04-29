package com.cdzg.xzshop.service.Impl;

import com.cdzg.xzshop.to.admin.GoodsCategoryTo;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.cdzg.xzshop.domain.GoodsCategory;
import com.cdzg.xzshop.mapper.GoodsCategoryMapper;

import java.util.ArrayList;
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

	@Override
	public List<GoodsCategory> findByLevelAndCategoryNameLike(Integer level,String likeCategoryName){
		 return goodsCategoryMapper.findByLevelAndCategoryNameLike(level,likeCategoryName);
	}

	@Override
    public PageResultVO<GoodsCategoryTo> page(int page, int pageSize, Integer level, String likeCategoryName) {
        PageHelper.startPage(page, pageSize);
        List<GoodsCategory> data = goodsCategoryMapper.findByLevelAndCategoryNameLike(level, likeCategoryName);

        List<GoodsCategoryTo> categoryTos = new ArrayList<>();
        for (GoodsCategory category : data) {

            GoodsCategoryTo categoryTo = new GoodsCategoryTo();
            BeanUtils.copyProperties(category,categoryTo);

            List<GoodsCategory> subs = findByParentIdAndLevel(category.getId(), level);
            categoryTo.setHasChildren(CollectionUtils.isNotEmpty(subs));
            categoryTos.add(categoryTo);
        }
        return new PageResultVO<GoodsCategoryTo>(categoryTos);
    }

	@Override
	public List<GoodsCategory> findByParentIdAndLevel(Long parentId,Integer level){
		 return goodsCategoryMapper.findByParentIdAndLevel(parentId,level);
	}

	@Override
    public PageResultVO<GoodsCategory> pageSub(int page, int pageSize, Long parentId, Integer level) {
        PageHelper.startPage(page, pageSize);
        return new PageResultVO<>(goodsCategoryMapper.findByParentIdAndLevel(parentId, level));
    }
}

