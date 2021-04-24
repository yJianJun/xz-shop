package com.cdzg.xzshop.utils.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

/**
 * excel输出工具
 */
@Slf4j
public class ExcelExport {

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 当前行号
     */
    private int rownum;

    public ExcelExport() {
        this.wb = new SXSSFWorkbook(500);
        this.sheet = this.wb.createSheet("sheet1");
        this.rownum = 0;
    }

    /**
     * 设置导出数据
     *
     * @param list
     * @param writer
     */
    public <T> void setExportData(List<T> list, ExcelRowWriter<T> writer) {
        if (Objects.isNull(list)) {
            return;
        }
        if (list.isEmpty()) {
            return;
        }
        for (T t : list) {
            Row row = this.sheet.createRow(this.rownum++);
            String[] rowDatas = writer.writeRow(t);
            for (int i = 0; i < rowDatas.length; i++) {
                String cellVal = rowDatas[i];
                Cell cell = row.createCell(i);
                if (Objects.isNull(cellVal)) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(cellVal);
                }
            }
        }
    }

    /**
     * 设置表头
     *
     * @param headerNames
     */
    public void setHeader(List<String> headerNames) {
        if (Objects.isNull(headerNames)) {
            return;
        }
        if (headerNames.isEmpty()) {
            return;
        }

        Row headerRow = this.sheet.createRow(this.rownum++);
        CellStyle cellStyle = getHeaderCellStyle();
        for (int i = 0, len = headerNames.size(); i < len; i++) {
            String headerName = headerNames.get(i);
            sheet.setColumnWidth(i, headerName.getBytes().length * 256);
        }
        for (int i = 0; i < headerNames.size(); i++) {
            String headerName = headerNames.get(i);
            // 列索引从0开始
            Cell cell = headerRow.createCell(i);
            // 列名1
            cell.setCellValue(headerName);
            // 单元格样式
            cell.setCellStyle(cellStyle);
        }
    }

    public void setHeader(ExcelHeaderWriter headerWriter) {
        this.rownum = headerWriter.setHeader(this.sheet, this.wb);
    }

    /**
     * 创建表头样式
     *
     * @return
     */
    private CellStyle getHeaderCellStyle() {
        //创建单元格样式
        CellStyle cellStyle = this.wb.createCellStyle();
        //居中样式
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //字体
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);
        cellStyle.setFont(font);

        return cellStyle;
    }

    public void write(OutputStream out) {
        try {
            this.wb.write(out);
        } catch (IOException e) {
            throw new ExcelException("excel数据写出有误");
        }
    }

    public void dispose() {
        if (this.wb instanceof SXSSFWorkbook) {
            ((SXSSFWorkbook) this.wb).dispose();
        }
    }

}
