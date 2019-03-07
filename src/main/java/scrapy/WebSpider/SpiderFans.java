package scrapy.WebSpider;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import scrapy.Util.RestClient;
import java.io.IOException;

/**
 * 爬取粉丝信息
 */
public class SpiderFans {
    public static void spider() throws IOException {
        CloseableHttpResponse closeableHttpResponse;
        RestClient restClient = new RestClient();
        String Url = "https://m.weibo.cn/api/container/getIndex?containerid=231051_-_fans_-_2269710922&since_id=2";
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

