package com.cdzg.xzshop.service.Impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.mapper.UserGoodsFavoritesMapper;
import java.util.List;
import com.cdzg.xzshop.domain.UserGoodsFavorites;
import com.cdzg.xzshop.service.UserGoodsFavoritesService;

@Service
public class UserGoodsFavoritesServiceImpl extends ServiceImpl<UserGoodsFavoritesMapper, UserGoodsFavorites> implements UserGoodsFavoritesService {

    @org.springframework.beans.factory.annotation.Autowired
    private UserGoodsFavoritesMapper userGoodsFavoritesMapper;

    @Override
    public int updateBatch(List<UserGoodsFavorites> list) {
        return baseMapper.updateBatch(list);
    }

    @Override
    public int batchInsert(List<UserGoodsFavorites> list) {
        return baseMapper.batchInsert(list);
    }

    @Override
    public int insertOrUpdate(UserGoodsFavorites record) {
        return baseMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(UserGoodsFavorites record) {
        return baseMapper.insertOrUpdateSelective(record);
    }

	@Override
	public UserGoodsFavorites findOneByUserIdAndSpuNo(String userId,Long spuNo){
		 return userGoodsFavoritesMapper.findOneByUserIdAndSpuNo(userId,spuNo);
	}

	@Override
	public List<UserGoodsFavorites> findByUserId(String userId){
		 return userGoodsFavoritesMapper.findByUserId(userId);
	}






}

