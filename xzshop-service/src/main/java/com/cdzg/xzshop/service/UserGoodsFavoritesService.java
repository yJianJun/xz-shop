package com.cdzg.xzshop.service;

import java.util.List;
import com.cdzg.xzshop.domain.UserGoodsFavorites;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserGoodsFavoritesService extends IService<UserGoodsFavorites> {


    int updateBatch(List<UserGoodsFavorites> list);

    int batchInsert(List<UserGoodsFavorites> list);

    int insertOrUpdate(UserGoodsFavorites record);

    int insertOrUpdateSelective(UserGoodsFavorites record);



	UserGoodsFavorites findOneByUserIdAndSpuNo(String userId,Long spuNo);



	List<UserGoodsFavorites> findByUserId(String userId);



}

