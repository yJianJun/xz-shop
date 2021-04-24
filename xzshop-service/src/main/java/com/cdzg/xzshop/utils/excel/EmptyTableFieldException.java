package com.cdzg.xzshop.utils.excel;

/**
 * Created by xqlu on 2019/3/21.
 *
 * @author xqlu
 * @description 单元格空异常基类
 * @modify
 */
public class EmptyTableFieldException extends RuntimeException {
    public EmptyTableFieldException(String msg) {
        super(msg);
    }
}
