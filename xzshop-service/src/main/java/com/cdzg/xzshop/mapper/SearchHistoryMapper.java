package com.cdzg.xzshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdzg.xzshop.domain.SearchHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SearchHistoryMapper extends BaseMapper<SearchHistory> {
    int updateBatch(List<SearchHistory> list);

    int batchInsert(@Param("list") List<SearchHistory> list);

    int insertOrUpdate(SearchHistory record);

    int insertOrUpdateSelective(SearchHistory record);

    SearchHistory findOneByKeyWord(@Param("keyWord") String keyWord);

    SearchHistory findOneByKeyWordAndUserId(@Param("keyWord")String keyWord,@Param("userId")Long userId);

    List<String> findKeyWordByUserIdOrderByCountDesc(@Param("userId")Long userId);

    int deleteByKeyWordAndUserId(@Param("keyWord")String keyWord,@Param("userId")Long userId);

    int deleteByUserId(@Param("userId")Long userId);




}