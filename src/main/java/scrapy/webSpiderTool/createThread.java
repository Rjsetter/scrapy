package scrapy.webSpiderTool;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class createThread extends Thread {
    public String typeName;
    public String typeUrl;
    final static Logger Log = Logger.getLogger(createThread.class);

    public createThread(String typeUrl, String typeName) {
        super();
        this.typeName = typeName;
        this.typeUrl = typeUrl;
    }

    public void run() {
        WebDriver driver = getChromeDriver.getDriver();
        System.out.println("------>" + typeUrl);
        try {
            Thread.sleep(10000);
            getFlags.getFlagsUrl(driver, typeName, typeUrl);
        } catch (InterruptedException e) {
//            e.printStackTrace();
            Log.error(e);
        } catch (IOException e) {
            Log.error(e);
//            e.printStackTrace();
        }
        //关闭chrome
        driver.close();
    }
}
