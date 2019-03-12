package scrapy.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import scrapy.pojo.IPBean;
import scrapy.mappersImp.ipMapperImp;


import java.io.IOException;
import java.net.*;

public class IPUtils {

    private static final String MY_IP_API = "https://www.ipip.net/ip.html";

    // 获取当前ip地址，判断是否代理成功
    public static String getMyIp() {
        try {
            String html = HttpUtils.getResponseContent(MY_IP_API);

            Document doc = Jsoup.parse(html);
            Element element = doc.selectFirst("div.tableNormal");

            Element ele = element.selectFirst("table").select("td").get(1);

            String ip = element.selectFirst("a").text();

            // System.out.println(ip);
            return ip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检测代理ip是否有效
     *
     * @param ipBean
     * @return
     */
    public static boolean isValid(IPBean ipBean) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ipBean.getIp(), ipBean.getPort()));
        try {
            URLConnection httpCon = new URL("https://m.weibo.cn/api/container/getIndex?containerid=231051_-_followers_-_2046556007").openConnection(proxy);
            httpCon.setConnectTimeout(1000);
            httpCon.setReadTimeout(1000);
            int code = ((HttpURLConnection) httpCon).getResponseCode();
            System.out.println("有效的ip地址："+ipBean.getIp()+"-->"+ipBean.getPort()+"->"+ipBean.getType());
            //插入有效的ip地址到数据库中
            ipMapperImp.insert(ipBean);
            Thread.sleep(500);
            return code == 200;
        } catch (IOException e) {
//            e.printStackTrace();
        }catch (InterruptedException in){
            in.printStackTrace();
        }
        return false;
    }
}
