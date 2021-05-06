package com.cdzg.xzshop.mapper;

import com.cdzg.xzshop.domain.ReturnGoodsInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReturnGoodsInfoMapper {
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
    int insert(ReturnGoodsInfo record);

    int insertOrUpdate(ReturnGoodsInfo record);

    int insertOrUpdateSelective(ReturnGoodsInfo record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(ReturnGoodsInfo record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    ReturnGoodsInfo selectByPrimaryKey(Long id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(ReturnGoodsInfo record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(ReturnGoodsInfo record);

    int updateBatch(List<ReturnGoodsInfo> list);

    int updateBatchSelective(List<ReturnGoodsInfo> list);

    int batchInsert(@Param("list") List<ReturnGoodsInfo> list);

    ReturnGoodsInfo findOneByShopId(@Param("shopId")Long shopId);
}