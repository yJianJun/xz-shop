package com.cdzg.xzshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdzg.xzshop.domain.UserGoodsFavorites;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserGoodsFavoritesMapper extends BaseMapper<UserGoodsFavorites> {
    int updateBatch(List<UserGoodsFavorites> list);

    int batchInsert(@Param("list") List<UserGoodsFavorites> list);

    int insertOrUpdate(UserGoodsFavorites record);

    int insertOrUpdateSelective(UserGoodsFavorites record);

    UserGoodsFavorites findOneByUserIdAndSpuNo(@Param("userId")String userId,@Param("spuNo")Long spuNo);


}