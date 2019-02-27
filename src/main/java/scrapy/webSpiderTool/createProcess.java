package scrapy.webSpiderTool;

import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class createProcess {
    public static void createNewProcess(String typeUrl, String typeName) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder();
        Process p = pb.start();
        p.waitFor();
        WebDriver driver = getChromeDriver.getDriver();
        driver.get(typeUrl);
//        getFlags.getFlagsUrl(driver,typeName,typeUrl,count);
    }
}
