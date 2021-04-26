package com.cdzg.xzshop.mapper;

import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReceivePaymentInfoMapper {
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
    int insert(ReceivePaymentInfo record);

    int insertOrUpdate(ReceivePaymentInfo record);

    int insertOrUpdateSelective(ReceivePaymentInfo record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(ReceivePaymentInfo record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    ReceivePaymentInfo selectByPrimaryKey(Long id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(ReceivePaymentInfo record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(ReceivePaymentInfo record);

    int updateBatch(List<ReceivePaymentInfo> list);

    int updateBatchSelective(List<ReceivePaymentInfo> list);

    int batchInsert(@Param("list") List<ReceivePaymentInfo> list);
}