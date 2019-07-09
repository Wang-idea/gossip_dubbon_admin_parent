package com.wang_idea.service;

import org.springframework.stereotype.Service;

/**
 * 新闻的服务实现类
 */
@Service
public interface NewsService {
    /**
     * 调用dao，获取sql库中新闻列表数据,再调用远程的索引写入服务，将新闻数据写入索引库
     */
    public void newsIndexWriter() throws Exception;
}
