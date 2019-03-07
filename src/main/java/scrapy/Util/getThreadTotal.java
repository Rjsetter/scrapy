package scrapy.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scrapy.webSpiderTool.getPerPageInfo;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class getThreadTotal {
    //添加日志
    private static final Logger logger = LoggerFactory.getLogger(getPerPageInfo.class);

    /**
     * 获取当前Java进程的活动线程数
     * @return
     */
    public static Map<String, Object> getThreadsTotals() {
        Map<String, Object> threadDataMap = new HashMap<String, Object>();

        Integer currentThreadCount = 0;
        Integer currentThreadsBusy = 0;

        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        List<ObjectName> threadPools = new ArrayList<ObjectName>();
        try {
            threadPools.addAll(mbeanServer.queryNames(new ObjectName("*:type=ThreadPool,*"), null));

            for (final ObjectName threadPool : threadPools) {
                //获取所有线程池下的线程总数
                currentThreadCount += (Integer) mbeanServer.getAttribute(threadPool, "currentThreadCount");
                currentThreadsBusy += (Integer) mbeanServer.getAttribute(threadPool, "currentThreadsBusy");

            }
        } catch (MalformedObjectNameException e) {
            logger.error("Get threads information error.", e);
        } catch (ReflectionException e) {
            logger.error("Reflecttion error", e);
        } catch (InstanceNotFoundException e) {
            logger.error("Instance not found error.", e);
        } catch (MBeanException e) {
            logger.error("Mean error", e);
        } catch (AttributeNotFoundException e) {
            logger.error("Could not get attribute", e);
        }

        threadDataMap.put("currentThreadCount", currentThreadCount);
        threadDataMap.put("currentThreadsBusy", currentThreadsBusy);

        return threadDataMap;
    }
}
