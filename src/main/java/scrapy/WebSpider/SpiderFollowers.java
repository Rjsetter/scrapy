package scrapy.WebSpider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import scrapy.Util.RestClient;

import java.io.IOException;

import static scrapy.Util.JsonUtil.getValueByJPath;

/**
 * 根据微博用户唯一标识UID，查找自身关注列表人的信息、
 * 接口地址：https://m.weibo.cn/api/container/getIndex?containerid=231051_-_followers_-_6037687121&page=2
 * 参数：containerid ：231051_-_followers_-_6037687121 其中后面数字信息为UID前面固定
 * 参数：page： 可选，页数
 */
public class SpiderFollowers {
    public static void spider() throws IOException {
        CloseableHttpResponse closeableHttpResponse;
        RestClient restClient = new RestClient();
        String Url = "https://m.weibo.cn/api/container/getIndex?containerid=231051_-_followers_-_6037687121&page=4";
        closeableHttpResponse = restClient.get(Url);
        JSONObject responseJson = restClient.getResponseJson(closeableHttpResponse);
        String test = getValueByJPath(responseJson, "data/cards");
        JSONArray flag = JSON.parseArray(test);

        JSONArray array;//为存储后面的array
        int index = flag.size()-1;
        if (flag.size() == 1) {//判断json是否只有一个cards标签，当页面为第一页时，直接访问就好
            String s = getValueByJPath(responseJson,"data/cards[0]/card_group");
            array = JSON.parseArray(s);
        }else{
            String s = getValueByJPath(responseJson,"data/cards["+index+"]/card_group");
            array = JSON.parseArray(s);
        }
        System.out.println(array.size());
        int length = array.size();
        int i =0;
        while(i<length){
            String users = array.getString(i);
            JSONObject user = JSON.parseObject(users);
            String userInfo = getValueByJPath(user,"user");
            JSONObject userinfo = JSON.parseObject(userInfo);
            System.out.println("用户名称:"+userinfo.getString("screen_name"));
            System.out.println("用户标识id:"+userinfo.getString("id"));
            System.out.println("是否为认证用户："+userinfo.getString("verified"));
            i++;
        }
    }

    public static void main(String[] args) {
        try {
            spider();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
