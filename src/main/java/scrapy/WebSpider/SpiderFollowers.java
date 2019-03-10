package scrapy.WebSpider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scrapy.Threads.ExecutorTest;
import scrapy.Util.RestClient;
import scrapy.pojo.Follower;
import scrapy.pojo.IPBean;
import scrapy.mappersImp.followerMapperImp;
import scrapy.mappersImp.ipMapperImp;
import scrapy.mappersImp.userMapperImp;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
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
public class SpiderFollowers  {
    //添加日志
    private static final Logger logger = LoggerFactory.getLogger(SpiderFollowers.class);

    public static void spider(String uid, String ip, int port, String type) throws IOException ,InterruptedException{
        CloseableHttpResponse closeableHttpResponse;
        RestClient restClient = new RestClient();
        String BaseUrl = "https://m.weibo.cn/api/container/getIndex?";
        String midUrl = "containerid=231051_-_followers_-_" + uid + "&page=";
        int pageSize = 1;
        boolean jumpout = true;//用于退出循环
        int count = 0;
        String test = "";//获取文本
        JSONObject responseJson = null;
        while (jumpout) {//判断是否已经到最后一页了
            String Url = BaseUrl + midUrl + pageSize;
            try{
            closeableHttpResponse = restClient.get(Url,ip,port,type);
            responseJson = restClient.getResponseJson(closeableHttpResponse);
            test = getValueByJPath(responseJson, "data/cards");
            }catch (NullPointerException noP){
                System.out.println("出现空指针错误，需要等待轮询！轮询间隔10秒！");
                Thread.sleep(10000);
                continue;
            }catch (SSLHandshakeException ssl){
                logger.error("出现握手失败，url为:"+Url+",IP地址为："+ip+",端口号为："+port+",请求类型为："+type);
                Thread.sleep(3000);
                continue;
            }catch (HttpHostConnectException h){
                System.out.println("连接超时，正在尝试重新连接，当前IP为："+ip);
                continue;
            }catch (JSONException json){
                System.out.println("json错误，当前IP为："+ip);
            }catch (SocketException socket){
                logger.error("软件导致连接中止，：recv失败：当前IP为："+ip+",当前Uid为："+uid);
                continue;
            }catch (SSLException  ssls){
                logger.error("出现认证错误当前IP为："+ip+",当前Uid为："+uid);
                System.out.println(ssls);
                continue;
            }
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
//            System.out.println("当前页面爬取的用户数："+array.size());
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
//                System.out.println("正在爬取用户Uid为"+uid+"第" + i + "页，目前爬取了"+count+"个用户！");
            }
        }
        logger.error("用户Uid为"+uid+"已爬取完成");
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
