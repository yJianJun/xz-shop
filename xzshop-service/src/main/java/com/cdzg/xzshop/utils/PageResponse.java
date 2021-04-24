package com.cdzg.xzshop.utils;



import java.io.Serializable;
import java.util.List;

public class PageResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer currentPage;
    private Integer pageSize;
    private Long totalNum;
    private Integer totalPage;
    private List<T> items;

    public PageResponse() {
    }

    public PageResponse(Integer currentPage, Integer pageSize, Long totalNum, List<T> items) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalNum = totalNum;
        this.totalPage = (int)((this.totalNum + (long)this.pageSize - 1L) / (long)this.pageSize);
        this.items = items;
    }

    public PageResponse(Integer currentPage, Integer pageSize, Long totalNum) {
        this(currentPage, pageSize, totalNum, (List)null);
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

    public Integer getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getItems() {
        return this.items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

}
