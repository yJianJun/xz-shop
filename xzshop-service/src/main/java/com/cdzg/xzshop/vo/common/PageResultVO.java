package com.cdzg.xzshop.vo.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResultVO<T>{


    @ApiModelProperty(value = "总条数")
    private int totalNum;

    @ApiModelProperty(value = "总页数")
    private int totalPage;

    @ApiModelProperty(value = "当前页码")
    private int currentPage;

    @ApiModelProperty(value = "当前尺寸")
    private int pageSize;

    @ApiModelProperty(value = "数据列表")
    private List<T> data;

    public void setPageParams(IPage page){
        this.currentPage = new Long(page.getCurrent()).intValue();
        this.totalPage = new Long(page.getPages()).intValue();
        this.pageSize = new Long(page.getSize()).intValue();
        this.totalNum = new Long(page.getTotal()).intValue();;
    }
}
