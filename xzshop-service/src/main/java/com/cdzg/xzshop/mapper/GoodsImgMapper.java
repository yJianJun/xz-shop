package com.cdzg.xzshop.mapper;

import com.cdzg.xzshop.domain.GoodsImg;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsImgMapper {
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
    int insert(GoodsImg record);

    int insertOrUpdate(GoodsImg record);

    int insertOrUpdateSelective(GoodsImg record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(GoodsImg record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    GoodsImg selectByPrimaryKey(Long id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(GoodsImg record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(GoodsImg record);

    int updateBatch(List<GoodsImg> list);

    int updateBatchSelective(List<GoodsImg> list);

    int batchInsert(@Param("list") List<GoodsImg> list);
}