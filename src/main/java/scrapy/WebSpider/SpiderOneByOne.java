package scrapy.WebSpider;

import scrapy.mappersImp.typeMapperImp;
import scrapy.pojo.Type;
import scrapy.webSpiderTool.createThread;


public class SpiderOneByOne {
    public static void main(String[] args) {
        int i = 44;
        while (44 <= i && i <= 46) {

            Type type = new Type();
            type = typeMapperImp.selectUrlById(i);
            System.out.println("------------------------下面开始爬取" + type.getType() + "类数据---------------------");
//            getFlags.getFlagsUrl(driver,entry.getKey(),entry.getValue());
//            多线程
            Thread t = new createThread("https:" + type.getTypeUrl(), type.getType());
            t.start();

            i++;
        }

    }
    //输出程序运行时间

}
