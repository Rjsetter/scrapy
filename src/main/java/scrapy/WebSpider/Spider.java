package scrapy.WebSpider;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import scrapy.webSpiderTool.createThread;
import scrapy.webSpiderTool.getChromeDriver;
import scrapy.webSpiderTool.getTypes;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Spider {
    public static int count = 0;

    public static void main(String[] args) throws InterruptedException, IOException {
        long startTime = System.currentTimeMillis();    //获取开始时间
        WebDriver driver = getChromeDriver.getDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://d.weibo.com/1087030002_417#");
        Thread.sleep(3000);
        //获取cookie

        Set<Cookie> cookies = driver.manage().getCookies();
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getDomain());
            System.out.println("name :" + cookie.getName() + "\n" + "value :" + cookie.getValue());
        }

        //获取需要爬取的信息的大类,用set数组储存遍历

        Map<String, String> infoType = getTypes.getInfoType(driver);
        driver.close();
        infoType.remove("交通");
        infoType.remove("电商");
        infoType.remove("电台");
        Iterator<Map.Entry<String, String>> it = infoType.entrySet().iterator();
        int c = 0;
        int count = 0;
        while (it.hasNext()) {
            c++;
            count++;
            Map.Entry<String, String> entry = it.next();
            String URL = "https:" + entry.getValue();
            System.out.println("------------------------<" + count + ">下面开始爬取" + entry.getKey() + "类数据---------------------");
//            getFlags.getFlagsUrl(driver,entry.getKey(),entry.getValue());
//            多线程
            Thread t = new createThread("https:" + entry.getValue(), entry.getKey());
            t.start();
            if (c == 4) {
                c = 0;
                Thread.sleep(300000);
            }
        }
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }
}
