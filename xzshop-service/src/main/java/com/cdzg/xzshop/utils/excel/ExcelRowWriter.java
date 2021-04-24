package com.cdzg.xzshop.utils.excel;

/**
 * excel行数据写入器
 * @author ice
 */
public interface ExcelRowWriter<T> {
    /**
     * 把数据模型转换为excel行数据
     * @param t
     * @return
     */
    String[] writeRow(T t);
}
