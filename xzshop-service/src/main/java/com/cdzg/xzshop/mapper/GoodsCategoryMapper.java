package com.cdzg.xzshop.mapper;

import com.cdzg.xzshop.domain.GoodsCategory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsCategoryMapper {
    /**
     * delete by primary key
     *
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(GoodsCategory record);

    int insertOrUpdate(GoodsCategory record);

    int insertOrUpdateSelective(GoodsCategory record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(GoodsCategory record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    GoodsCategory selectByPrimaryKey(Long id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(GoodsCategory record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(GoodsCategory record);

    int updateBatch(List<GoodsCategory> list);

    int updateBatchSelective(List<GoodsCategory> list);

    int batchInsert(@Param("list") List<GoodsCategory> list);
}