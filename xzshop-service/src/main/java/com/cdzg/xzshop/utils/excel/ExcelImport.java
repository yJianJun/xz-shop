package com.cdzg.xzshop.utils.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * excel导入工具
 * @author ice
 */
public class ExcelImport {
    private static Logger log = LoggerFactory.getLogger(ExcelImport.class);

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 标题行数
     */
    private int headerNum;

    public ExcelImport(File file) throws IOException {
        this(new FileInputStream(file), 1, 0);
    }

    /**
     * 构造函数
     * @param in
     * @throws IOException
     */
    public ExcelImport(InputStream in) {
        this(in, 1, 0);
    }

    /**
     * 构造函数
     * @param headerNum 标题行数，数据行号=标题行数+1
     * @param sheetIndexOrName 工作表编号或名称
     * @throws IOException
     */
    public ExcelImport(InputStream is, int headerNum, Object sheetIndexOrName){

        try {
            this.wb = WorkbookFactory.create(is);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExcelException("文档格式不正确!");
        }
        if (this.wb == null) {
            throw new ExcelException("导入文档为空!");
        }
        this.setSheet(sheetIndexOrName, headerNum);
        log.debug("Initialize success.");
    }

    /**
     * 设置当前工作表和标题行数
     * @author ThinkGem
     */
    public void setSheet(Object sheetIndexOrName, int headerNum) {
        if (sheetIndexOrName instanceof Integer || sheetIndexOrName instanceof Long){
            this.sheet = this.wb.getSheetAt(Integer.parseInt(sheetIndexOrName.toString()));
        }else{
            this.sheet = this.wb.getSheet(sheetIndexOrName.toString());
        }
        if (this.sheet == null){
            throw new ExcelException("没有找到‘"+sheetIndexOrName+"’工作表!");
        }
        this.headerNum = headerNum;
    }


    /**
     * 获取行对象
     * @param rownum
     * @return 返回Row对象，如果空行返回null
     */
    private Row getRow(int rownum){
        Row row = this.sheet.getRow(rownum);
        if (row == null){
            return null;
        }
        // 验证是否是空行，如果空行返回null
        short cellNum = 0;
        short emptyNum = 0;
        Iterator<Cell> it = row.cellIterator();
        while (it.hasNext()) {
            cellNum++;
            Cell cell = it.next();
            if (StringUtils.isBlank(cell.toString())) {
                emptyNum++;
            }
        }
        if (cellNum == emptyNum) {
            return null;
        }
        return row;
    }

    /**
     * 获取数据行号
     * @return
     */
    public int getDataRowNum(){
        return headerNum;
    }

    /**
     * 获取最后一个数据行号
     * @return
     */
    public int getLastDataRowNum(){
        return this.sheet.getLastRowNum() + headerNum;
    }

    /**
     * 获取最后一个列号
     * @return
     */
    public int getLastCellNum(){
        Row row = this.getRow(headerNum);
        return row == null ? 0 : row.getLastCellNum();
    }


    /**
     * 获取单元格值
     * @param row 获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column){
        if (row == null){
            return row;
        }
        Object val = "";
        try{
            Cell cell = row.getCell(column);
            if (cell != null){
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                    val = cell.getNumericCellValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // POI Excel 日期格式转换
                        val = DateUtil.getJavaDate((Double) val);
                    }else{
                        if ((Double) val % 1 > 0){
                            val = new DecimalFormat("0.00").format(val);
                        }else{
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                }else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    String cellString = cell.getStringCellValue();
                    val = cellString == null ? null : cellString.trim();
                }else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
                    try {
                        val = cell.getStringCellValue();
                    } catch (Exception e) {
                        FormulaEvaluator evaluator = cell.getSheet().getWorkbook()
                                .getCreationHelper().createFormulaEvaluator();
                        evaluator.evaluateFormulaCell(cell);
                        CellValue cellValue = evaluator.evaluate(cell);
                        switch (cellValue.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                val = cellValue.getNumberValue();
                                break;
                            case Cell.CELL_TYPE_STRING:
                                val = cellValue.getStringValue();
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                val = cellValue.getBooleanValue();
                                break;
                            case Cell.CELL_TYPE_ERROR:
                                val = ErrorEval.getText(cellValue.getErrorValue());
                                break;
                            default:
                                val = cell.getCellFormula();
                        }
                    }
                }else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
                    val = cell.getBooleanCellValue();
                }else if (cell.getCellType() == Cell.CELL_TYPE_ERROR){
                    val = cell.getErrorCellValue();
                }
            }
        }catch (Exception e) {
            return val;
        }
        return val;
    }


    /**
     * 把excel行数据转换为List
     * @param reader
     * @param <E>
     * @return
     */
    public <E> List<E> getDataList(ExcelRowReader<E> reader) {
        List<E> ret = new ArrayList<>();
        for (int i = this.getDataRowNum(); i < this.getLastDataRowNum(); i++) {
            Row row = getRow(i);
            if (row == null) {
                continue;
            }
            int cellNum = getLastCellNum();

            Object[] rowDatas = new Object[cellNum];
            for (int j = 0; j < cellNum; j++) {
                rowDatas[j] = getCellValue(row, j);
            }
            E e = reader.readRow(rowDatas);
            ret.add(e);
        }
        return ret;
    }
}
