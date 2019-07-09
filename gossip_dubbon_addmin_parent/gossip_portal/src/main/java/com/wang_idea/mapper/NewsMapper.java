package com.wang_idea.mapper;

import com.wang_idea.pojo.News;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * dao层的服务接口
 */
public interface NewsMapper {
    /**
     * 查询新闻数据(此处设置一次一百条)
     * sql：select *from news where id > 参数 limit 0,100
     */
    //查询数据
     public List<News> queryData(String maxId);

    /**
     * 获取当前(100条)数据的最大值(id)
     * select max(id) from （sql：select *from news where id > maxid limit 0,100） as temp
     */
    public String queryMaxId(String maxId);

}
