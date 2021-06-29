package com.cdzg.xzshop.repository;

import com.cdzg.xzshop.domain.GoodsSpu;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;

@Repository
public interface GoodsSpuRepository extends ElasticsearchRepository<GoodsSpu,Long> {


    @Query("{\n" +
            "    \"bool\":{\n" +
            "\t    \"filter\": [\n" +
            "              {\n" +
            "                  \"term\" : { \n" +
            "                       \"status\" : 1 \n" +
            "                    }\n" +
            "               },\n" +
            "\t      {\n" +
            "                  \"term\" : { \n" +
            "                       \"is_delete\" : 0 \n" +
            "                    }\n" +
            "              }\n" +
            "      ],\n" +
            "        \"should\":[\n" +
            "            {\n" +
            "                \"match\":{\n" +
            "                    \"ad_word\":\"?0\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"match\":{\n" +
            "                    \"goods_name\":\"?0\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"term\":{\n" +
            "                    \"goods_name.keyword\":\"?0\"\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "\t\"minimum_should_match\": 1\n" +
            "    }\n" +
            "}")
    Page<GoodsSpu> search(String name, Pageable pageable);
}
