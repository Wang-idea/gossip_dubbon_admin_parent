package spider.entertainmentnews.newstencent;

import com.google.gson.Gson;
import spider.entertainmentnews.newstencent.pojo.NewsTencent;
import utils.HttpClientUtils;
import utils.IdWorker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 腾讯新闻爬虫
 */
public class NewsTecentSpider {

    //json转换对象
    public static Gson gson = new Gson();

    //id生成器 需要两个参数
    //0 0是参数  每次添加不同参数即可
    public static IdWorker idworker = new IdWorker(0, 1);

    //创建新的实体类
    public static NewsTencent newsTencent = new NewsTencent();

    public static void main(String[] args) throws IOException {
        //1.确定url：热点url  非热点url
        String hoturl = "https://pacaio.match.qq.com/irs/rcd?cid=137&token=d0f13d594edfc180f5bf6b845456f3ea&id=&ext=ent&num=60";
        //https://pacaio.match.qq.com/irs/rcd?cid=135&token=6e92c215fb08afa901ac31eca115a34f&callback=jspnphotnews&ext=ent&page=0&num=15

        String nohoturl = "https://pacaio.match.qq.com/irs/rcd?cid=58&token=c232b098ee7611faeffc46409e836360&ext=ent&page=0";
        //2.使用httpClientUtils工具发送http请求，获得数据(Json)
        String hotJson = HttpClientUtils.doGet(hoturl);
        String nohotJson = HttpClientUtils.doGet(nohoturl);

        //解析Json数据
        List<NewsTencent> hotNewList = parseNewTencent(hotJson);
        List<NewsTencent> nohotnewsList = parseNewTencent(nohotJson);

        //4.将新闻列表保存到数据库中

    }

    /**
     * 根据新闻的json数据，解析成一个新闻列表
     *
     * @param
     * @return jspnphotnews（
     */
    private static List<NewsTencent> parseNewTencent(String newJson) {

        List<NewsTencent> newsTencentList = new ArrayList<>();

//        newJson = newJson.substring(newJson.indexOf("{"), newJson.lastIndexOf(")"));
//        System.out.println("newsTencentList = " + newJson);

        //1.进行json转换 ，String --->转换成Map<String,Object>
        Map<String,Object> map =gson.fromJson(newJson,Map.class);

        //2.从map中获取data新闻数据
        List<Map<String,Object>> data = (List<Map<String, Object>>) map.get("data");

        //判断是否为空



        //3.遍历打他数据(新闻数据列表)
        for (Map<String, Object> datum : data) {
            //注意这个对象 方法哦for循环里面
            NewsTencent newsTencent = new NewsTencent();

            //获取url地址
            String url = (String) datum.get("url");
            System.out.println("url = " + url);

            String title = (String) datum.get("title");
            String update_time = (String) datum.get("update_time");
            String source = (String) datum.get("source");
            String content = (String) datum.get("content");

            newsTencent.setId(idworker.nextId());
            newsTencent.setUrl(url);
            newsTencent.setTitle(title);
            newsTencent.setTime(update_time);
            newsTencent.setSource(source);
            newsTencent.setContent(content);
            newsTencentList.add(newsTencent);

        }
        return newsTencentList ;
    }

}