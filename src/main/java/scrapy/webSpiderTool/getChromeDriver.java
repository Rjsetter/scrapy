package scrapy.webSpiderTool;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class getChromeDriver {

    public static WebDriver getDriver() {
        //浏览器头
        WebDriver driver;
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver\\chromedriver.exe");// chromedriver服务地址
        //模拟手机发送请求
        Map<String, String> mobileEmulation = new HashMap<String, String>();
        //mobileEmulation.put("deviceName", "iPhone 6/7/8");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        //启动最大化
        chromeOptions.addArguments("--start-maximized");
        //去除自动控制
        chromeOptions.addArguments("disable-infobars");
        //无头浏览器
        chromeOptions.addArguments("headless");
        Map<String, Object> prefs = new HashMap<String, Object>();
        //禁止图片
        prefs.put("profile.managed_default_content_settings.images", 2);
        //禁止javascript
        prefs.put("profile.managed_default_content_settings.javascript", 2);
        //禁止Css
        prefs.put("profile.managed_default_content_settings.css", 2); // 2就是代表禁止加载的意思
        chromeOptions.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(chromeOptions);
        return driver;
    }
}
