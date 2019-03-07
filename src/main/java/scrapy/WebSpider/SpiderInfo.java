package scrapy.WebSpider;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import scrapy.Util.RestClient;

import java.io.IOException;

/**
 * 爬取个人信息
 */
public class SpiderInfo {
    public static void spider() throws IOException {
        CloseableHttpResponse closeableHttpResponse;
        RestClient restClient = new RestClient();
        String Url = "https://m.weibo.cn/profile/info?uid=3505635557";
        closeableHttpResponse = restClient.get(Url);
        JSONObject responseJson = restClient.getResponseJson(closeableHttpResponse);
        System.out.println(responseJson);
    }
    public static void main(String []args){
        try{spider();}catch (IOException e){
            System.out.println(e);
        }
    }
}
