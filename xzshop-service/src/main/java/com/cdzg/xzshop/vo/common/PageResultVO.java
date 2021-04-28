package com.cdzg.xzshop.vo.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageResultVO<T> extends PageInfo<T> {


    @ApiModelProperty(value = "总条数")
    private int size;

    @ApiModelProperty(value = "总页数")
    private int pages;

    @ApiModelProperty(value = "当前页码")
    private int pageNum;

    @ApiModelProperty(value = "当前尺寸")
    private int pageSize;

    @ApiModelProperty(value = "数据列表")
    private List<T> data;

    public void setPageParams(IPage page){
        this.pageNum = new Long(page.getCurrent()).intValue();
        this.pages = new Long(page.getPages()).intValue();
        this.pageSize = new Long(page.getSize()).intValue();
        this.size = new Long(page.getTotal()).intValue();;
    }

    public PageResultVO(List<T> list) {
        super(list, 8);
        size =super.getSize();
        pages = super.getPages();
        pageNum = super.getPageNum();
        pageSize = super.getPageSize();
        data = super.getList();
    }
}
