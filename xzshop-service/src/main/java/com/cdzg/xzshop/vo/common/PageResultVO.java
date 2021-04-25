package com.cdzg.xzshop.vo.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResultVO<T> {

    @ApiModelProperty(value = "总条数")
    private Long totalNum;

    @ApiModelProperty(value = "总页数")
    private Long totalPage;

    @ApiModelProperty(value = "当前页码")
    private Long currentPage;

    @ApiModelProperty(value = "当前尺寸")
    private Long pageSize;

    @ApiModelProperty(value = "数据列表")
    private List<T> data;

    public void setPageParams(IPage page){
        this.setCurrentPage(page.getCurrent());
        this.setTotalPage(page.getPages());
        this.setPageSize(page.getSize());
        this.setTotalNum(page.getTotal());
    }

}
