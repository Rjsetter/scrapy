package scrapy.WebSpider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import scrapy.Util.RestClient;
import scrapy.pojo.Follower;
import scrapy.pojo.IPBean;
import scrapy.mappersImp.followerMapperImp;
import scrapy.mappersImp.ipMapperImp;
import scrapy.mappersImp.userMapperImp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static scrapy.Util.JsonUtil.getValueByJPath;

/**
 * 根据微博用户唯一标识UID，查找自身关注列表人的信息、
 * 接口地址：https://m.weibo.cn/api/container/getIndex?containerid=231051_-_followers_-_6037687121&page=2
 * 参数：containerid ：231051_-_followers_-_6037687121 其中后面数字信息为UID前面固定
 * 参数：page： 可选，页数
 */
public class SpiderFollowers {
    public static void spider(String uid, String ip, int port, String type) throws IOException {
        CloseableHttpResponse closeableHttpResponse;
        RestClient restClient = new RestClient();
        String BaseUrl = "https://m.weibo.cn/api/container/getIndex?";
        String midUrl = "containerid=231051_-_followers_-_" + uid + "&page=";
        int pageSize = 1;
        boolean jumpout = true;//用于退出循环
        int count = 0;
        while (jumpout) {//判断是否已经到最后一页了
            String Url = BaseUrl + midUrl + pageSize;
            closeableHttpResponse = restClient.get(Url);
            JSONObject responseJson = restClient.getResponseJson(closeableHttpResponse);
            String test = getValueByJPath(responseJson, "data/cards");
            if (test.length() <= 2) {//返回数据为空时
                jumpout = false;
                break;
            }
            pageSize++;//页数增加
            JSONArray flag = JSON.parseArray(test);
            JSONArray array;//为存储后面的array
            int index = flag.size() - 1;
            if (flag.size() == 1) {//判断json是否只有一个cards标签，当页面为第一页时，直接访问就好
                String s = getValueByJPath(responseJson, "data/cards[0]/card_group");
                array = JSON.parseArray(s);
            } else {
                String s = getValueByJPath(responseJson, "data/cards[" + index + "]/card_group");
                array = JSON.parseArray(s);
            }
            System.out.println(array.size());
            int length = array.size();
            int i = 0;
            while (i < length) {
                String users = array.getString(i);
                JSONObject user = JSON.parseObject(users);
                String userInfo = getValueByJPath(user, "user");
                JSONObject userinfo = JSON.parseObject(userInfo);
                Follower follower = new Follower();
                follower.setUid(uid);
                follower.setFollowers_uid(userinfo.getString("id"));
                follower.setScreen_name(userinfo.getString("screen_name"));
                follower.setFollow_count(userinfo.getInteger("follow_count"));
                follower.setFollowers_count(userinfo.getInteger("followers_count"));
                follower.setProfile_image_url(userinfo.getString("profile_image_url"));
                follower.setProfile_url(userinfo.getString("profile_url"));
                follower.setVerified(userinfo.getString("verified"));
                followerMapperImp.insertType(follower);
                count++;
                i++;
            }
        }
        System.out.println("一共关注了：" + count + "个用户！");
    }

    public static void main(String[] args) throws InterruptedException {
        //按类型查询数据库中得地址
        List<String> uidList = userMapperImp.queryAllUidByType("IT互联网");
        //查询数据库中的有效ip地址
        List<IPBean> ips = ipMapperImp.getAllIps();
        Map<Integer, IPBean> mapIps = new HashMap<Integer, IPBean>();
        ips.forEach(ipBean -> mapIps.put(ipBean.getId(), ipBean));
        int len = uidList.size();
        int maxIndexSiza = mapIps.size();
        String ip = ""; //IP地址
        String type = "https";//请求类型
        int port = 0;//端口号
        int index = 0;//IP地址
        for (int i = 0; i < len; i++) {
            if (i % 3 == 0) {
                ++index;
                if(index==maxIndexSiza)//如果没有ip地址，就回归为1
                    index=1;
                port = mapIps.get(index).getPort();
                ip = mapIps.get(index).getIp();
                if (mapIps.get(index).getType() == 0)
                    type = "http";
                type = "https";
            }
            try {
                spider(uidList.get(i), ip, port, type);
            } catch (IOException e) {
                System.out.println(e);
            } catch (NullPointerException n) {
                System.out.print("出现NullPointerException");
                ++index;
                port = mapIps.get(index).getPort();
                ip = mapIps.get(index).getIp();
                if (mapIps.get(index).getType() == 0)
                    type = "http";
                type = "https";
                Thread.sleep(60000);
                i--;
                continue;
            }
        }
    }
}
