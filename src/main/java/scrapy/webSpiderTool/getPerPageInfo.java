package scrapy.webSpiderTool;

import com.vdurmont.emoji.EmojiParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scrapy.mappersImp.userMapperImp;
import scrapy.pojo.User;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;


public class getPerPageInfo {
    //添加日志
    private static final Logger logger = LoggerFactory.getLogger(getPerPageInfo.class);

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
//        System.out.println(doc);
        //获取整个列表
        Elements nextUrlD = doc.select(" #Pl_Core_F4RightUserList__4 > div > div > div > div > div.WB_cardpage.S_line1 > div > a.page.next.S_txt1.S_line1");
        String nextUrl = nextUrlD.attr("href");
        Elements targets = doc.select("#Pl_Core_F4RightUserList__4>div>div>div>div.follow_box>div.follow_inner>ul>li>dl");
        for (Element target : targets) {
            //昵称
            String nickname = target.select("dd.mod_info.S_line1 >div.info_name.W_fb.W_f14>a.S_txt1").text();
            //获取用户唯一标识
            String id = target.select("dd.mod_info.S_line1 >div.info_name.W_fb.W_f14>a.S_txt1").attr("usercard");
            String s = id.split("=")[1];
            String Id = s.split("&")[0];
            //System.out.println("测试爬取ID信息："+Id);
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
            info = EmojiParser.removeAllEmojis(info);
            //标签dd.mod_info.S_line1 > div
            String flag = null;
            if (target.select("dd.mod_info.S_line1 > div").hasClass("info_relation")) {
                flag = target.select("dd.mod_info.S_line1 > div.info_relation").text();
            }
//            System.out.println("----------------------------");
//            System.out.println("昵称:"+nickname);
//            System.out.println("性别:"+sex);
//            System.out.println("关注数:"+GZCount.replace("关注",""));
            String fans=FSCount.replace("粉丝","");
            //筛选数据，万字改为数字0000，便于数据分析
            if(fans.contains("万")){
                 fans=fans.substring(0,fans.length()-1)+"0000";
            }
//            System.out.println("粉丝数:"+fans);
//            System.out.println("微博数:"+WBCount.replace("微博",""));
//            System.out.println("地址:"+address);
//            System.out.println("简介:"+info);
            //剔除"标签："
            if(flag!=null)
                flag=flag.substring(3,flag.length());

            User user = new User();
            user.setInfoid(Id);
            user.setLabel(flag);
            user.setJianjie(null);
            user.setSex(sex);
            user.setAddress(address);
            user.setWeiboNum(WBCount.replace("微博",""));
            user.setFansNum(fans);
            user.setConcernNum(GZCount);
            user.setNickName(nickname);
            user.setType(type);
            userMapperImp.insert(user);
            logger.info("正在插入类型为"+type+"的博主信息:"+user.toString());
        }
        return nextUrl;
    }
}
