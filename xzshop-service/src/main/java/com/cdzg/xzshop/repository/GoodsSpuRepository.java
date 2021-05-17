package com.cdzg.xzshop.repository;

import com.cdzg.xzshop.domain.GoodsSpu;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;

@Repository
public interface GoodsSpuRepository extends ElasticsearchRepository<GoodsSpu,Long> {


    @Query("{\"bool\": {\"should\": [{\"match\": {\"ad_word\": \"?0\"}},{\"match\": {\"goods_name\": \"?0\"}},{\"term\": {\"goods_name.keyword\": \"?0\"}}]}}")
    Page<GoodsSpu> search(String name, Pageable pageable);
}
