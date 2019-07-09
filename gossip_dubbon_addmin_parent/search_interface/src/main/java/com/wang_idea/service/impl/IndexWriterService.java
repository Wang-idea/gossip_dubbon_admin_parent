package com.wang_idea.service.impl;

import com.wang_idea.pojo.News;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;

/**
 * 索引写入接口
 */
public interface IndexWriterService {
    /**
     *索引写入的方法
     * @param list
     */
    public void newsIndexWriter(List<News> list) throws IOException, SolrServerException;
}
