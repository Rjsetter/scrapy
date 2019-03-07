package scrapy.webSpiderTool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class getFlags {
    //添加日志
    private static final Logger logger = LoggerFactory.getLogger(getFlags.class);


    public static Set<String> getFlagsUrl(WebDriver driver, String type, String url) throws InterruptedException, IOException {
        //建立每个类型下所有页面的Url地址
//        driver.navigate().to("https:"+url);
        driver.navigate().to(url);
        Thread.sleep(8000);
        Set<String> flagsUrls = new HashSet<String>();
        String head = "https://d.weibo.com";
        String url_1 = "=Pl_Core_F4RightUserList__4&page=";
        String url_2 = "#Pl_Core_F4RightUserList__4";
        Document doc2 = Jsoup.parse(driver.getPageSource());
        //获取整个列表
        Elements nextUrlD = doc2.select(" #Pl_Core_F4RightUserList__4 > div > div > div > div > div.WB_cardpage.S_line1 > div > a.page.next.S_txt1.S_line1");
        String nextUrl = nextUrlD.attr("href");
        String[] Array = nextUrl.split("=");
        String changeId = Array[0];
        int pageNum = getPageCount.getPerPageCount(driver, type);
        int i;
        for (i = 1; i <= pageNum; i++) {
            String totalAddress = head + changeId + url_1 + i + url_2;
            System.out.println(type + "-->第" + i + "页地址为：" + totalAddress);
            flagsUrls.add(totalAddress);
//            URL getUrl = new URL(totalAddress);
            getPerPageInfo.spiderOnePage(driver, totalAddress, type);
        }
        return flagsUrls;
    }
}
