package com.cdzg.xzshop.service.Impl;

import com.cdzg.xzshop.to.admin.GoodsSpuTo;
import com.cdzg.xzshop.utils.PageUtil;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.cdzg.xzshop.domain.SearchHistory;
import com.cdzg.xzshop.mapper.SearchHistoryMapper;
import com.cdzg.xzshop.service.SearchHistoryService;

@Service
public class SearchHistoryServiceImpl extends ServiceImpl<SearchHistoryMapper, SearchHistory> implements SearchHistoryService {

    @Autowired
    private SearchHistoryMapper searchHistoryMapper;

    @Override
    public int updateBatch(List<SearchHistory> list) {
        return baseMapper.updateBatch(list);
    }

    @Override
    public int batchInsert(List<SearchHistory> list) {
        return baseMapper.batchInsert(list);
    }

    @Override
    public int insertOrUpdate(SearchHistory record) {
        return baseMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(SearchHistory record) {
        return baseMapper.insertOrUpdateSelective(record);
    }

    @Override
    public SearchHistory findOneByKeyWord(String keyWord) {
        return searchHistoryMapper.findOneByKeyWord(keyWord);
    }

	@Override
	public List<String> findKeyWordByUserIdOrderByCountDesc(Long userId){
		 return searchHistoryMapper.findKeyWordByUserIdOrderByCountDesc(userId);
	}

	@Override
    public PageResultVO<String> searchHistorywithPage(int page, int pageSize, Long userId) {
        PageHelper.startPage(page, pageSize);
        return PageUtil.transform(new PageInfo(searchHistoryMapper.findKeyWordByUserIdOrderByCountDesc(userId)));
    }
}

