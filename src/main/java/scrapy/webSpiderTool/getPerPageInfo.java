package scrapy.webSpiderTool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class getPerPageInfo {
    /**
     * 爬取一个标签里的所有博主
     */
    public static String spiderOnePage(WebDriver driver, String urlPerPage, String type) throws InterruptedException {
        //爬取内容
        try {
            driver.navigate().to(urlPerPage);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } catch (NoSuchElementException e) {
            System.out.println("网络波动没有加载完全！");
            driver.navigate().to(urlPerPage);
        }
        Thread.sleep(1000);
        Document doc = Jsoup.parse(driver.getPageSource());
        //获取整个列表
        Elements nextUrlD = doc.select(" #Pl_Core_F4RightUserList__4 > div > div > div > div > div.WB_cardpage.S_line1 > div > a.page.next.S_txt1.S_line1");
        String nextUrl = nextUrlD.attr("href");
        Elements targets = doc.select("#Pl_Core_F4RightUserList__4>div>div>div>div.follow_box>div.follow_inner>ul>li>dl");
        for (Element target : targets) {
            //昵称
            String nickname = target.select("dd.mod_info.S_line1 >div.info_name.W_fb.W_f14>a.S_txt1").text();
            //性别
            String sex = "空";
            if (target.select("dd.mod_info.S_line1>div.info_name.W_fb.W_f14>a>i").hasClass("W_icon icon_male")) {
                sex = "M";
            } else {
                sex = "F";
            }
            //关注数
            String GZCount = target.select("dd.mod_info.S_line1>div.info_connect>span:nth-child(1)").text();
            //粉丝数
            String FSCount = target.select("dd.mod_info.S_line1>div.info_connect>span:nth-child(2)").text();
            //微博数
            String WBCount = target.select("dd.mod_info.S_line1>div.info_connect>span:nth-child(3)").text();
            //地址
            String address = target.select("dd.mod_info.S_line1>div.info_add>span").text();
            //简介
            String info = target.select(" dd.mod_info.S_line1>div.info_intro>span").text();
            //标签dd.mod_info.S_line1 > div
            String flag = "空";
            if (target.select("dd.mod_info.S_line1 > div").hasClass("info_relation")) {
                flag = target.select("dd.mod_info.S_line1 > div.info_relation").text();
            }
            System.out.println("----------------------------");
            System.out.println("昵称:"+nickname);
            System.out.println("性别:"+sex);
            System.out.println("关注数:"+GZCount);
            System.out.println("粉丝数:"+FSCount);
            System.out.println("微博数:"+WBCount);
            System.out.println("地址:"+address);
            System.out.println("简介:"+info);
            System.out.println(flag);
//            address="test";
//            info = "test";
            //System.out.println(nextUrl);
//            String sql = "insert into scrapy.info(type,label,nickName,sex,concernNum,fansNum,weiboNum,address,jianjie)Values("+type+","+flag+","+nickname+","+sex+","+GZCount+","+FSCount+","+WBCount+","+address+","+info+")";
//            String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
//            String sql = "insert into info2(type,label,nickName,sex,concernNum,fansNum,weiboNum,address,jianjie) values('" + type + "','" + flag + "','" + nickname + "','" + sex + "','" + GZCount + "','" + FSCount + "','" + WBCount + "','" + address + "','" + info.replaceAll(regEx, "") + "')";
//            System.out.println(sql);
//            mysqlInsert.insert(sql);
        }
        return nextUrl;
    }
}
