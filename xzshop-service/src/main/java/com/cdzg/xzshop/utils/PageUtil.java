package com.cdzg.xzshop.utils;

import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;

import java.util.ArrayList;
import java.util.List;

/**
 * describe: 分页工具类
 *
 * @author yt
 * @date 2018/11/9
 */
public class PageUtil {
    public static List pageForList(List dataList, int start, int limit) {
        //当为第一页时
        if (start == 0 && dataList.size() - 1 < start + limit) {
            return dataList;
        }
        //当为后面页数时
        //如果总数小于要分页的数量 或者起始位置大于总数返回空
        if (start != 0 && dataList.size() - 1 < start) {
            return new ArrayList();
        }

        if (start != 0 && (dataList.size() - 1 < start + limit && start <= dataList.size() - 1)) {
            return dataList.subList(start, dataList.size());
        }
        return dataList.subList(start, start + limit);
    }

    public static  PageResultVO<T> transform(PageInfo<T> pageInfo) {

        PageResultVO<T> pageResultVO = new PageResultVO<>();
        pageResultVO.setCurrentPage(pageInfo.getPageNum());
        pageResultVO.setPageSize(pageInfo.getPageSize());
        pageResultVO.setData(pageInfo.getList());
        pageResultVO.setTotalPage(pageInfo.getPages());
        pageResultVO.setTotalNum(new Long(pageInfo.getTotal()).intValue());
        return pageResultVO;

    }

    public static  PageResultVO<T> transform(Page<T> pageInfo) {

        PageResultVO<T> pageResultVO = new PageResultVO<>();
        pageResultVO.setCurrentPage(pageInfo.getPageable().getPageNumber() + 1);
        pageResultVO.setPageSize(pageInfo.getSize());
        pageResultVO.setData(pageInfo.getContent());
        pageResultVO.setTotalPage(pageInfo.getTotalPages());
        pageResultVO.setTotalNum(new Long(pageInfo.getTotalElements()).intValue());
        return pageResultVO;

    }

}
