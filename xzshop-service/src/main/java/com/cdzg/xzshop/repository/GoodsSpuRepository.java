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


    @Query("{\"bool\": {\"should\": [{\"match\": {\"adWord\": \"?0\"}},{\"match\": {\"goodsName\": \"?0\"}},{\"term\": {\"goodsName.keyword\": \"?0\"}}]}}")
    Page<GoodsSpu> search(String name, Pageable pageable);
}
