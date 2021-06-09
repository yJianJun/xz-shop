package com.cdzg.xzshop.vo.common;


import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@ApiModel(description = "列表分页模型")
public class BasePageRequest implements Serializable {
    private static final long serialVersionUID = 7929720707782186157L;

    @ApiModelProperty(value = "当前页码", position = 1,required = true)
    @NotNull(message = "当前页码不能为空")
    private Integer currentPage = 1;

    @ApiModelProperty(value = "每页条数", position = 2,required = true)
    @NotNull(message = "每页条数不能为空")
    private Integer pageSize = 20;

    @ApiModelProperty(value = "数据总条数",allowEmptyValue = true)
    private Long totalNum;

    public BasePageRequest() {
    }

    public void startPage() {
        if (!this.hasTotalNum()) {
            PageHelper.startPage(this.currentPage, this.pageSize);
        } else {
            PageHelper.startPage(this.currentPage, this.pageSize, false);
        }

    }

    public boolean hasTotalNum() {
        return this.totalNum != null && this.totalNum > 0L;
    }

    public Integer getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalNum() {
        return this.totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }
}
