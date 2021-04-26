package com.cdzg.xzshop.mapper;

import com.cdzg.xzshop.domain.GoodsPayHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsPayHistoryMapper {
    /**
     * delete by primary key
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(GoodsPayHistory record);

    int insertOrUpdate(GoodsPayHistory record);

    int insertOrUpdateSelective(GoodsPayHistory record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(GoodsPayHistory record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    GoodsPayHistory selectByPrimaryKey(Long id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(GoodsPayHistory record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(GoodsPayHistory record);

    int updateBatch(List<GoodsPayHistory> list);

    int updateBatchSelective(List<GoodsPayHistory> list);

    int batchInsert(@Param("list") List<GoodsPayHistory> list);
}