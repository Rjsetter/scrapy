package scrapy.WebSpider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eclipsesource.json.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import scrapy.Util.RestClient;

import java.io.IOException;

import static scrapy.Util.JsonUtil.getValueByJPath;

/**
 * 爬取粉丝信息
 */
public class SpiderFans {
    public static void spider() throws IOException {
        CloseableHttpResponse closeableHttpResponse;
        RestClient restClient = new RestClient();
        String Url = "https://m.weibo.cn/api/container/getIndex?containerid=231051_-_fans_-_1195242865&since_id=299";
        closeableHttpResponse = restClient.get(Url);
        JSONObject responseJson = restClient.getResponseJson(closeableHttpResponse);
        String s = getValueByJPath(responseJson,"data/cards[0]/card_group");
        JSONArray array = JSON.parseArray(s);
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

