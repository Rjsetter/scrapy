package scrapy.webSpiderTool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class getTypes {
    /**
     * 获取分类数据
     *
     * @param driver
     * @return 返回一个Map对象
     */
    public static Map<String, String> getInfoType(WebDriver driver) {
        //点击更多按钮，不能删，加载出分类信息
        driver.findElement(By.xpath("//*[@id=\"Pl_Discover_TextNewList__3\"]/div/div/div/div/div[1]/ul/li[9]/a/span[1]")).click();
        String html = driver.getPageSource();
        Document doc = Jsoup.parse(html);
        Elements contents = doc.select("#Pl_Discover_TextNewList__3 > div > div > div > div > div.subitem_box.S_line1 > ul >li");
        //用Map存储字典数据
        Map<String, String> Types = new HashMap<String, String>();
        Types.put("明星", "//d.weibo.com/1087030002_2975_1003_0#");
        Types.put("商界", "//d.weibo.com/1087030002_2975_1001_0#");
        for (Element content : contents) {
            String type = content.getElementsByClass("item_title S_txt1").text();
            String linkHref = content.getElementsByTag("a").attr("href");
//            System.out.println("-->"+type+":"+linkHref);
            Types.put(type, linkHref);
        }
        return Types;
    }
}
