package com.cdzg.xzshop.mapper;

import com.cdzg.xzshop.domain.ShopImg;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShopImgMapper {
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
    int insert(ShopImg record);

    int insertOrUpdate(ShopImg record);

    int insertOrUpdateSelective(ShopImg record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(ShopImg record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    ShopImg selectByPrimaryKey(Long id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(ShopImg record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(ShopImg record);

    int updateBatch(List<ShopImg> list);

    int updateBatchSelective(List<ShopImg> list);

    int batchInsert(@Param("list") List<ShopImg> list);
}