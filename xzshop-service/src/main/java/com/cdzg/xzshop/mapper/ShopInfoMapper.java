package com.cdzg.xzshop.mapper;

import com.cdzg.xzshop.domain.ShopInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShopInfoMapper {
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
    int insert(ShopInfo record);

    int insertOrUpdate(ShopInfo record);

    int insertOrUpdateSelective(ShopInfo record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(ShopInfo record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    ShopInfo selectByPrimaryKey(Long id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(ShopInfo record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(ShopInfo record);

    int updateBatch(List<ShopInfo> list);

    int updateBatchSelective(List<ShopInfo> list);

    int batchInsert(@Param("list") List<ShopInfo> list);
}