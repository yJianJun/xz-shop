package com.cdzg.xzshop.repository;

import com.cdzg.xzshop.domain.GoodsSpu;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsSpuRepository extends ElasticsearchRepository<GoodsSpu,Long> {
}
