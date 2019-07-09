package com.wang_idea.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wang_idea.constant.GossipConstant;
import com.wang_idea.mapper.NewsMapper;
import com.wang_idea.pojo.News;
import com.wang_idea.service.NewsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    /**
     * 注入dao代理对象
     */
    @Autowired
    private NewsMapper newsMapper;

    /**
     * jedis连接池
     */
    @Autowired
    private JedisPool jedisPool;

    /**
     * 将数据写入索引库  调用远程服务
     * 需要找注册中心注入远程服务
     */
    @Reference(timeout = 3000)
    private IndexWriterService indexWriterService;

    /**
     * 打印日志
     */
    private Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    /**
     * 调用dao，获取sql库中新闻列表数据,再调用远程的索引写入服务，将新闻数据写入索引库
     */
    @Override
    public void newsIndexWriter() throws Exception {
        //1. 注入dao的代理对象

        //从redis中获取maxId，如果不存在，初始化为0，如果存在，就是用这个最大的maxId
        Jedis jedis = jedisPool.getResource();
        String maxId = jedis.get(GossipConstant.bigData_GOSSIP_maxID);
        jedis.close();
        if (StringUtils.isEmpty(maxId)) {
            maxId = "0";
        }

        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd'T' HH:mm:ss'Z'");
        while (true) {
            //2. 调用dao层，获取新闻列表数据
            List<News> list = newsMapper.queryData(maxId);

            //跳出循环的逻辑，更新最大的id值到redis中
            if (list == null || list.size() == 0) {
                jedis = jedisPool.getResource();
                jedis.set(GossipConstant.bigData_GOSSIP_maxID, maxId);
                jedis.close();
                break;
            }

            // 做日期处理
            for (News news : list) {
                String time = news.getTime();
                Date date = oldFormat.parse(time);
                String newTime = newFormat.format(date);
                //将日期转换成solrCloud的标准格式
                news.setTime(newTime);
            }

            //3. 调用远程服务，将数据写入索引库
            indexWriterService.newsIndexWriter(list);

            System.out.println("写入索引的条数"+list.size());
            logger.info("写入solrCloud索引库的数据条数：" + list.size());
            //4. 更新maxId值：当前页的最大id值
            maxId = newsMapper.queryMaxId(maxId);


        }
    }

}
