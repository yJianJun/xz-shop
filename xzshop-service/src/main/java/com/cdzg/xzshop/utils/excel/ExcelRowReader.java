package com.cdzg.xzshop.utils.excel;

/**
 * excel行读取器
 * @author ice
 */
public interface ExcelRowReader<E> {

    /**
     * 把excel 行 转为数据模型
     * @param rowDatas
     * @return
     */
    E readRow(Object[] rowDatas);
}
