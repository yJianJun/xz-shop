package com.cdzg.xzshop.utils.excel;

import com.beust.jcommander.internal.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * excel工具类
 * @author ice
 */
public final class ExcelUtils {

    /**
     * 读取excel数据
     * @param in
     * @param rowReader
     * @param <T>
     * @return
     */
    public static <T> List<T> readRows(InputStream in, ExcelRowReader<T> rowReader) {
        try {
            ExcelImport excelImport = new ExcelImport(in);
            return excelImport.getDataList(rowReader);
        } finally {
            close(in);
        }
    }

    /**
     * list数据写入到excel
     * @param headerNames
     * @param list
     * @param rowWriter
     * @param out
     * @param <T>
     */
    public static <T> void writeToExcel(List<String> headerNames, List<T> list, ExcelRowWriter<T> rowWriter, OutputStream out) {
        ExcelExport ee = new ExcelExport();
        ee.setHeader(headerNames);
        ee.setExportData(list, rowWriter);
        try {
            ee.write(out);
            out.flush();
            ee.dispose();
        } catch (IOException e) {
            throw new ExcelException("导出数据有误");
        } finally {
            close(out);
        }
    }

    /**
     * list数据写入到excel文件
     * @param headerNames
     * @param list
     * @param rowWriter
     * @param file
     * @param <T>
     */
    public static <T> void writeToExcel(List<String> headerNames, List<T> list, ExcelRowWriter<T> rowWriter, File file) {
        try {
            writeToExcel(headerNames, list, rowWriter, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ExcelException("导出数据有误:" + e.getMessage());
        }
    }


    /**
     * 关闭流
     * @param closeable
     */
    private static void close(Closeable closeable) {
        if (Objects.nonNull(closeable)) {
            try {
                closeable.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }

    /**
     * 输出到excel文件
     *
     * @param headerNames
     * @param list
     * @param rowWriter
     * @param fileName
     * @param response
     */
    public static <T> void writeToExcel(List<String> headerNames, List<T> list, ExcelRowWriter<T> rowWriter, String fileName, HttpServletResponse response) {
        setExcelFileName(response, fileName);
        try {
            ExcelUtils.writeToExcel(headerNames, list, rowWriter, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException("excel输出异常：" + e.getMessage());
        }
    }

    /**
     * 设置excel文件名
     *
     * @param resp
     * @param excelFileName
     */
    private static void setExcelFileName(HttpServletResponse resp, String excelFileName) {
        if (StringUtils.isBlank(excelFileName)) {
            throw new ExcelException("excel文件名不能为空");
        }
        resp.setContentType("application/octet-stream; charset=utf-8");
        try {
            excelFileName = new String(excelFileName.getBytes("GBK"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new ExcelException("文件名有误");
        }
        resp.setHeader("Content-disposition", "attachment; filename=" + excelFileName);
    }

    /**
     * 导入excel
     *
     *
     * @param clazz
     * @return
     */
    public static <T> List<T> importExcel(InputStream inputStream, Class<T> clazz, int dataFirstRow) {

        if (!clazz.isAnnotationPresent(ModelTitle.class)) {
            return Lists.newArrayList();
        }
        Workbook book = null;
        try {
            book = WorkbookFactory.create(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (book == null) {
            return Lists.newArrayList();
        }
        Sheet sheet = book.getSheetAt(0);

        if (sheet == null) {
            return Lists.newArrayList();
        }
        List<T> list = Lists.newArrayList();

        for (int i = dataFirstRow; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (row == null) {
                break;
            }
            T data = init(row, clazz);

            if (data == null) {
                break;
            }
            list.add(data);
        }
        try {
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
    /**
     * 通过行初始数据
     *
     * @param row
     * @param clazz
     * @return
     */
    public static <T> T init(Row row, Class<T> clazz) {

        try {
            Field[] fields = clazz.getDeclaredFields();
            T data = clazz.newInstance();

            for (Field field : fields) {
                ExcelFieldMeta meta = field.getAnnotation(ExcelFieldMeta.class);

                if (meta == null) {
                    continue;
                }
                Integer colIndex = meta.colIndex();

                if (colIndex == null || colIndex < 0) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method writeMethod = pd.getWriteMethod();
                String value = getCellValue(row.getCell(colIndex));
                writeMethod.invoke(data, value);
            }

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取单元格值
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {

        if (cell == null) {
            return "";
        }
        cell.setCellType(Cell.CELL_TYPE_STRING);

        String res = cell.getStringCellValue();

        if (StringUtils.isBlank(res)) {
            return "";
        }

        return res;
    }

}
