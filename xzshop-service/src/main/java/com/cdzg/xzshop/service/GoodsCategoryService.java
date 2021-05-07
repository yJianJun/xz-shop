package com.cdzg.xzshop.service;

import com.cdzg.xzshop.domain.GoodsCategory;
import com.cdzg.xzshop.to.admin.GoodsCategoryTo;
import com.cdzg.xzshop.vo.admin.GoodsCategoryAddVo;
import com.cdzg.xzshop.vo.common.PageResultVO;

import java.util.List;

public interface GoodsCategoryService {


    int deleteByPrimaryKey(Long id);

    int insert(GoodsCategory record);

    int insertOrUpdate(GoodsCategory record);

    int insertOrUpdateSelective(GoodsCategory record);

    int insertSelective(GoodsCategory record);

    GoodsCategory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsCategory record);

    int updateByPrimaryKey(GoodsCategory record);

    int updateBatch(List<GoodsCategory> list);

    int updateBatchSelective(List<GoodsCategory> list);

    int batchInsert(List<GoodsCategory> list);



	List<GoodsCategory> findByLevelAndCategoryNameLike(Integer level,String likeCategoryName);


    List<GoodsCategoryTo> list(Integer level, String likeCategoryName);



	List<GoodsCategory> findByParentIdAndLevel(Long parentId,Integer level);

    PageResultVO<GoodsCategory> pageSub(int page, int pageSize, Long parentId, Integer level);



	GoodsCategory findOneByIdAndLevel(Long id,Integer level);

    void add(String adminUser, GoodsCategoryAddVo addVo);

    void batchPutOnDown(List<Long> list, Boolean flag);
}

