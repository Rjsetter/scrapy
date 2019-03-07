package scrapy.webSpiderTool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class getPageCount {
    //添加日志
    private static final Logger logger = LoggerFactory.getLogger(getPageCount.class);


    /**
     * 接受浏览器头，返回每个标签下博主的页数
     *
     * @param driver 浏览器头
     * @param type   需要爬取的类
     * @throws InterruptedException
     */
    public static int getPerPageCount(WebDriver driver, String type) throws InterruptedException {
        Thread.sleep(3000);
        Document Doc = Jsoup.parse(driver.getPageSource());
        Elements L = Doc.select("#Pl_Core_F4RightUserList__4 > div > div > div > div.follow_box > div.WB_cardpage.S_line1 > div>a");
        int length = 0;
        try {
            length = Integer.parseInt(L.get(L.size() - 2).text());
        } catch (ArrayIndexOutOfBoundsException x) {
            logger.error("没有加载出来的类有：" + type);
            System.out.println("没有加载出来的类有：" + type);

            return length;
        }
        return length;
    }

}
