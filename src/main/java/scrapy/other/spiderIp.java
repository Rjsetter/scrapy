package scrapy.other;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scrapy.Util.HttpUtils;
import scrapy.Util.IPUtils;
import scrapy.Util.RestClient;
import scrapy.mappersImp.ipMapperImp;
import scrapy.myppers.IpMapper;
import scrapy.pojo.IPBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 爬取西刺网上高匿，存活时间天以上的IP地址
 * @author Rjsetter
 * @version 1.0
 */
public class spiderIp {
    //https的API接口
    private static final String HTTPS_API = "https://www.xicidaili.com/wn/";

    /**
     * 爬取西刺的IP地址
     * @param page  需要爬去的页数
     */
    public static void spider(int page) {
        for(int j=51;j<=page;j++){

         String html = HttpUtils.getResponseContent(HTTPS_API+j);
//         System.out.println(html);
            Document document = Jsoup.parse(html);
            Elements eles = document.selectFirst("table").select("tr");

            for (int i = 0; i < eles.size(); i++){
                if (i == 0) continue;
                Element ele = eles.get(i);
//                System.out.println(ele);
                String ip = ele.children().get(1).text();
                int port = Integer.parseInt(ele.children().get(2).text().trim());
                String typeStr = ele.children().get(5).text().trim();
                String liveTime =ele.children().get(8).text().trim();
                String ipType = ele.children().get(4).text().trim();
                int type;
                System.out.println("正在检查Ip:"+ip);
                if(liveTime.contains("天")) {
                    if ("HTTP".equalsIgnoreCase(typeStr))
                        type = IPBean.TYPE_HTTP;
                    else
                        type = IPBean.TYPE_HTTPS;
                    IPBean ipBean = new IPBean();
                    ipBean.setIp(ip);
                    ipBean.setPort(port);
                    ipBean.setType(type);
                    if (IPUtils.isValid(ipBean)) {
                        System.out.println(ipBean.getIp());
                        ipMapperImp.insert(ipBean);
                    }
                }
            }
        }

    }
    public static  void main(String []args){
        spider(70);
    }
}
