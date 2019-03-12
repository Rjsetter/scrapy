package scrapy.WebSpider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scrapy.Util.RestClient;
import scrapy.pojo.Follower;
import scrapy.pojo.IPBean;
import scrapy.mappersImp.followerMapperImp;
import scrapy.mappersImp.ipMapperImp;
import scrapy.mappersImp.userMapperImp;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static scrapy.Util.JsonUtil.getValueByJPath;

/**
 * 根据微博用户唯一标识UID，查找自身关注列表人的信息、
 * 接口地址：https://m.weibo.cn/api/container/getIndex?containerid=231051_-_followers_-_6037687121&page=2
 * 参数：containerid ：231051_-_followers_-_6037687121 其中后面数字信息为UID前面固定
 * 参数：page： 可选，页数
 */
public class SpiderFollowers {
    //添加日志
    private static final Logger logger = LoggerFactory.getLogger(SpiderFollowers.class);

    public static void spider(String uid, String ip, int port, String type, List<IPBean> ips) throws IOException, InterruptedException {
        //查询数据库中的有效ip地址
        Map<Integer, IPBean> mapIps = new HashMap<Integer, IPBean>();
        for (int i = 0; i < ips.size(); i++) {
            mapIps.put(i, ips.get(i));
        }
        //产生随机数
        Random r = new Random();
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
            try {
                closeableHttpResponse = restClient.get(Url, ip, port, type);
                responseJson = restClient.getResponseJson(closeableHttpResponse);
                test = getValueByJPath(responseJson, "data/cards");
            } catch (ConnectException er) {
                System.out.println("连接出错，现在开始重新请求！");
                int index = r.nextInt(ips.size());
                ip = getIp(index, mapIps);
                port = getPort(index, mapIps);
                Thread.sleep(500);
                continue;
            } catch (NullPointerException noP) {
                Thread.sleep(1000);
                int index = r.nextInt(ips.size());
                ip = getIp(index, mapIps);
                port = getPort(index, mapIps);
                System.out.println("IP被封禁，更换IP地址为" + ip + "，需要等待轮询,轮询间隔1秒！");
                continue;
            } catch (SSLHandshakeException ssl) {
                logger.error("出现握手失败，url为:" + Url + ",IP地址为：" + ip + ",端口号为：" + port + ",请求类型为：" + type);
                int index = r.nextInt(ips.size());
                ip = getIp(index, mapIps);
                port = getPort(index, mapIps);
                Thread.sleep(3000);
                continue;
//            } catch (HttpHostConnectException h) {
//                System.out.println("连接超时，正在尝试重新连接，当前IP为：" + ip);
//                int index = r.nextInt(ips.size());
//                ip=getIp(index,mapIps);
//                port=getPort(index,mapIps);
////                if (counts%4==6&&counts>6) {
////                    ip = "121.69.46.178";
////                    port = 9000;
////                }else if(counts%6==2&&counts>6){
////                    ip="124.193.37.5";
////                    port=8888;
////                }else if(counts%6==3&&counts>6){
////                    ip="124.205.155.147";
////                    port=9090;
////                }else if(counts%6==4&&counts>6){
////                    ip="58.244.55.174";
////                    port = 8080;
////                }else if(counts%6==5&&counts>6){
////                    ip ="116.209.54.225";
////                    port=9999;
////                }
//                Thread.sleep(1000);
//                continue;
            } catch (JSONException json) {
                logger.error("json错误，当前IP为：" + ip);
                int index = r.nextInt(ips.size());
                ip = getIp(index, mapIps);
                port = getPort(index, mapIps);
                continue;
            } catch (SocketException socket) {
                logger.error("软件导致连接中止，：recv失败：当前IP为：" + ip + ",当前Uid为：" + uid);
                int index = r.nextInt(ips.size());
                ip = getIp(index, mapIps);
                port = getPort(index, mapIps);
                continue;
            } catch (SSLException ssls) {
                logger.error("出现认证错误当前IP为：" + ip + ",当前Uid为：" + uid);
                int index = r.nextInt(ips.size());
                ip = getIp(index, mapIps);
                port = getPort(index, mapIps);
                System.out.println(ssls);
                continue;
            } catch (NoHttpResponseException nor) {
                System.out.println("没有返回！现在开始重新重试！");
                int index = r.nextInt(ips.size());
                ip = getIp(index, mapIps);
                port = getPort(index, mapIps);
                Thread.sleep(500);
                continue;
            }catch (SocketTimeoutException S){
                System.out.println("读取数据超时：SocketTimeout,现在开始重新请求！");
                int index = r.nextInt(ips.size());
                ip = getIp(index, mapIps);
                port = getPort(index, mapIps);
                Thread.sleep(500);
                continue;
            }catch(ConnectTimeoutException c){
                System.out.println("连接超时,现在开始重新请求！-->"+ip);
                int index = r.nextInt(ips.size());
                ip = getIp(index, mapIps);
                port = getPort(index, mapIps);
                Thread.sleep(500);
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
                i++;
//                System.out.println("正在爬取用户Uid为"+uid+"第" + i + "页，目前爬取了"+count+"个用户！");
            }
        }
        logger.error("用户Uid为" + uid + "已爬取完成");
        //设置该用户已经爬取完成关注列表
        userMapperImp.setTrue(uid);
    }

    public static String getIp(int index, Map<Integer, IPBean> mapIps) {
        return mapIps.get(index).getIp();
    }

    public static int getPort(int index, Map<Integer, IPBean> mapIps) {
        return mapIps.get(index).getPort();
    }
//    public static void main(String[] args) throws InterruptedException {
//        //按类型查询数据库中得地址
//        List<String> uidList = userMapperImp.queryAllUidByType("IT互联网");
//        //查询数据库中的有效ip地址
//        List<IPBean> ips = ipMapperImp.getAllIps();
//        Map<Integer, IPBean> mapIps = new HashMap<Integer, IPBean>();
//        ips.forEach(ipBean -> mapIps.put(ipBean.getId(), ipBean));
//        int len = uidList.size();
//        int maxIndexSiza = mapIps.size();
//        String ip = ""; //IP地址
//        String type = "https";//请求类型
//        int port = 0;//端口号
//        int index = 0;//IP地址
//        for (int i = 0; i < len; i++) {
//            if (i % 3 == 0) {
//                ++index;
//                if (index == maxIndexSiza)//如果没有ip地址，就回归为1
//                    index = 1;
//                port = mapIps.get(index).getPort();
//                ip = mapIps.get(index).getIp();
//                if (mapIps.get(index).getType() == 0)
//                    type = "http";
//                type = "https";
//            }
//            try {
//                spider(uidList.get(i), ip, port, type);
//            } catch (IOException e) {
//                System.out.println(e);
//            } catch (NullPointerException n) {
//                System.out.print("出现NullPointerException");
//                ++index;
//                port = mapIps.get(index).getPort();
//                ip = mapIps.get(index).getIp();
//                if (mapIps.get(index).getType() == 0)
//                    type = "http";
//                type = "https";
//                Thread.sleep(60000);
//                i--;
//                continue;
//            }
//        }
//    }
}
