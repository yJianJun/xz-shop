package com.cdzg.xzshop.service.Impl;

import java.util.List;

import com.beust.jcommander.internal.Lists;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.mapper.GoodsSpuMapper;
import com.cdzg.xzshop.to.admin.GoodsCategoryTo;
import com.cdzg.xzshop.vo.admin.GoodsCategoryAddVo;
import com.cdzg.xzshop.utils.PageUtil;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.cdzg.xzshop.domain.GoodsCategory;
import com.cdzg.xzshop.mapper.GoodsCategoryMapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.cdzg.xzshop.service.GoodsCategoryService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

    @Resource
    private GoodsCategoryMapper goodsCategoryMapper;

    @Resource
    private GoodsSpuMapper goodsSpuMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Long id) {

        List<GoodsSpu> goodsSpus = goodsSpuMapper.findByCategoryIdLevel2OrCategoryIdLevel1(id, id);
        if (CollectionUtils.isNotEmpty(goodsSpus)) {
            throw new BaseException("该分类已关联商品，无法删除");
        }

        GoodsCategory category = goodsCategoryMapper.findOneByIdAndLevel(id, 1);
        if (Objects.nonNull(category)) {
            List<Long> sons = goodsCategoryMapper.findByParentIdAndLevel(category.getId(), 2).stream().map(GoodsCategory::getId).collect(Collectors.toList());
            goodsSpus = goodsSpuMapper.findByCategoryIdLevel2In(sons);
            if (CollectionUtils.isNotEmpty(goodsSpus)) {
                throw new BaseException("该分类下二级分类已关联商品，无法删除");
            }
        }

        return goodsCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insert(GoodsCategory record) {
        return goodsCategoryMapper.insert(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertOrUpdate(GoodsCategory record) {
        return goodsCategoryMapper.insertOrUpdate(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertOrUpdateSelective(GoodsCategory record) {
        return goodsCategoryMapper.insertOrUpdateSelective(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertSelective(GoodsCategory record) {
        return goodsCategoryMapper.insertSelective(record);
    }

    @Override
    public GoodsCategory selectByPrimaryKey(Long id) {
        return goodsCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(GoodsCategory record) {
        return goodsCategoryMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateByPrimaryKey(GoodsCategory record) {
        return goodsCategoryMapper.updateByPrimaryKey(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateBatch(List<GoodsCategory> list) {
        return goodsCategoryMapper.updateBatch(list);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateBatchSelective(List<GoodsCategory> list) {
        return goodsCategoryMapper.updateBatchSelective(list);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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

        } else {

            List<GoodsCategory> parents = data;
            for (GoodsCategory parent : parents) {

                GoodsCategoryTo categoryTo = new GoodsCategoryTo();
                BeanUtils.copyProperties(parent, categoryTo);
                List<GoodsCategory> subs = findByParentIdAndLevel(parent.getId(), 2);
                categoryTo.setChildren(subs);
                categoryTos.add(categoryTo);
            }
        }
        return categoryTos;
    }

    @Override
    public List<GoodsCategory> findByParentIdAndLevel(Long parentId, Integer level) {
        return goodsCategoryMapper.findByParentIdAndLevel(parentId, level);
    }

    @Override
    public PageResultVO<GoodsCategory> pageSub(int page, int pageSize, Long parentId, Integer level) {
        PageHelper.startPage(page, pageSize);
        return PageUtil.transform(new PageInfo(goodsCategoryMapper.findByParentIdAndLevel(parentId, level)));
    }

    @Override
    public GoodsCategory findOneByIdAndLevel(Long id, Integer level) {
        return goodsCategoryMapper.findOneByIdAndLevel(id, level);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void add(String adminUser, GoodsCategoryAddVo addVo) {

        GoodsCategory category = GoodsCategory.builder()
                .categoryName(addVo.getName())
                .createUser(adminUser)
                .gmtCreate(LocalDateTime.now())
                .gmtUpdate(LocalDateTime.now())
                .level(addVo.getLevel())
                .parentId(addVo.getParentId())
                .status(addVo.getStatus())
                .build();

        insert(category);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchPutOnDown(List<Long> list, Boolean flag) {

        for (int i = 0; i < list.size(); i++) {

            Long id = list.get(i);
            GoodsCategory goodsCategory = selectByPrimaryKey(id);

            if (Objects.nonNull(goodsCategory)) {

                goodsCategory.setStatus(flag);
                goodsCategory.setGmtUpdate(LocalDateTime.now());
                insertOrUpdate(goodsCategory);
            }
        }

    }

    @Override
    public List<GoodsCategoryTo> listByStatus(Boolean status, Integer level) {

        List<GoodsCategory> data = goodsCategoryMapper.findByStatusAndLevel(status, level);
        List<GoodsCategoryTo> categoryTos = new ArrayList<>();

        Map<GoodsCategory, List<GoodsCategory>> map = new HashMap<>();

        if (2 == level) {
            List<GoodsCategory> sons = data;
            for (GoodsCategory son : sons) {

                GoodsCategory parent = findOneByIdAndLevelAndStatus(son.getParentId(), 1, status);
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

        } else {

            List<GoodsCategory> parents = data;
            for (GoodsCategory parent : parents) {

                GoodsCategoryTo categoryTo = new GoodsCategoryTo();
                BeanUtils.copyProperties(parent, categoryTo);
                List<GoodsCategory> subs = findByParentIdAndStatusAndLevel(parent.getId(), status, 2);
                categoryTo.setChildren(subs);
                categoryTos.add(categoryTo);
            }
        }
        return categoryTos;
    }

    @Override
    public List<GoodsCategory> findByStatusAndLevel(Boolean status, Integer level) {
        return goodsCategoryMapper.findByStatusAndLevel(status, level);
    }

    @Override
    public List<GoodsCategory> findByParentIdAndStatusAndLevel(Long parentId, Boolean status, Integer level) {
        return goodsCategoryMapper.findByParentIdAndStatusAndLevel(parentId, status, level);
    }

    @Override
    public GoodsCategory findOneByIdAndLevelAndStatus(Long id, Integer level, Boolean status) {
        return goodsCategoryMapper.findOneByIdAndLevelAndStatus(id, level, status);
    }

    @Override
    public String findCategoryNameByIdAndLevelAndStatus(Long id, Integer level, Boolean status) {
        return goodsCategoryMapper.findCategoryNameByIdAndLevelAndStatus(id, level, status);
    }

    @Override
    public String findCategoryNameByIdAndLevel(Long id, Integer level) {
        return goodsCategoryMapper.findCategoryNameByIdAndLevel(id, level);
    }

    @Override
    public GoodsCategory findOneById(Long id) {
        return goodsCategoryMapper.findOneById(id);
    }


}

