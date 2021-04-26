package com.cdzg.xzshop.mapper;

import com.cdzg.xzshop.domain.GoodsSpu;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsSpuMapper {
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
    int insert(GoodsSpu record);

    int insertOrUpdate(GoodsSpu record);

    int insertOrUpdateSelective(GoodsSpu record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(GoodsSpu record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    GoodsSpu selectByPrimaryKey(Long id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(GoodsSpu record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(GoodsSpu record);

    int updateBatch(List<GoodsSpu> list);

    int updateBatchSelective(List<GoodsSpu> list);

    int batchInsert(@Param("list") List<GoodsSpu> list);
}