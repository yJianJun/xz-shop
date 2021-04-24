package com.cdzg.xzshop.utils.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 设置excel的表头信息
 *
 * @Author xqlu
 * @Created by on 2019/1/4
 * @Version 1.0
 */
public interface ExcelHeaderWriter {
    /**
     * 设置excel的表头
     *
     * @param sheet
     * @param wb
     * @return
     */
    int setHeader(Sheet sheet, Workbook wb);
}
