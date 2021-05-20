package com.cdzg.xzshop.service;

import java.util.List;
import com.cdzg.xzshop.domain.SearchHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.github.pagehelper.PageInfo;

public interface SearchHistoryService extends IService<SearchHistory> {


    int updateBatch(List<SearchHistory> list);

    int batchInsert(List<SearchHistory> list);

    int insertOrUpdate(SearchHistory record);

    int insertOrUpdateSelective(SearchHistory record);


    SearchHistory findOneByKeyWord(String keyWord);



	List<String> findKeyWordByUserIdOrderByCountDesc(Long userId);

}

