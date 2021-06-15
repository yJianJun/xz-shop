package com.cdzg.xzshop.utils;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * excel导出帮助类
 *
 * @author yuhu
 *
 */
public class ExcelExportUtils {

    /**
     * 导出excel数据
     *
     * @param fileName
     * @param sheetName
     * @param data
     * @param clazz
     * @param response
     * @throws IOException
     */
    public static final void export(String fileName, String sheetName, List<? extends BaseRowModel> data,
                                    Class<? extends BaseRowModel> clazz, HttpServletResponse response) throws IOException {
        response.reset();
        ServletOutputStream out = response.getOutputStream();
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
        Sheet sheet1 = new Sheet(1, 0, clazz);
        sheet1.setSheetName(sheetName);
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Content-disposition",
                "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1") + ".xlsx");

        writer.write(data, sheet1);
        writer.finish();
        out.flush();
        out.close();
    }

    /**
     * 导出数据
     * @param fileName
     * @param sheetName
     * @param data
     * @param response
     * @throws IOException
     */
    public static final void export(String fileName, String sheetName, List<List<String>> data,
                                    HttpServletResponse response) throws IOException {
        response.reset();
        ServletOutputStream out = response.getOutputStream();
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
        Sheet sheet1 = new Sheet(1, 0);
        sheet1.setSheetName(sheetName);
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Content-disposition",
                "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1") + ".xlsx");

        writer.write0(data, sheet1);
        writer.finish();
        out.flush();
        out.close();
    }
}