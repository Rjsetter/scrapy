package scrapy.Threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scrapy.mappersImp.typeMapperImp;
import scrapy.mappersImp.userMapperImp;
import scrapy.pojo.IPBean;
import scrapy.mappersImp.ipMapperImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import static scrapy.WebSpider.SpiderFollowers.spider;

/**
 * 测试类
 *
 * @author SHANHY(365384722 @ QQ.COM)
 * @date 2015年12月4日
 */
public class ExecutorTest {
    //添加日志
    private static final Logger logger = LoggerFactory.getLogger(ExecutorTest.class);

    public static void main(String[] args) {

        ExecutorProcessPool pool = ExecutorProcessPool.getInstance();
        //获取所有分类
        List<String> types = typeMapperImp.getAllType();
        //查询数据库中的有效ip地址
        List<IPBean> ips = ipMapperImp.getAllIps();
        Map<Integer, IPBean> mapIps = new HashMap<Integer, IPBean>();
        for(int i=0;i<ips.size();i++){
            mapIps.put(i,ips.get(i));
        }
        //按类型查询数据库中得地址
        List<String> uidList = userMapperImp.queryAllUidByType("美食");
        int len = uidList.size();
        int maxIndexSiza = mapIps.size();
        String ip = ""; //IP地址
        String type = "https";//请求类型
        int port = 0;//端口号
        int index = 0;//IP地址
        for (int i = 0; i < len; i++) {
            logger.error("正在爬取类型为美食,Uid为：" + uidList.get(i) + "的关注信息！");
            if (i % 3 == 0) {
                ++index;
                if (index == maxIndexSiza)//如果没有ip地址，就回归为1
                    index = 1;
                port = mapIps.get(index).getPort();
                ip = mapIps.get(index).getIp();
                if (mapIps.get(index).getType() == 0) {
                    type = "http";
                } else {
                    type = "https";
                }
            }
            pool.execute(new ExcuteTask2(uidList.get(i), ip, port, type));
        }//


        //关闭线程池，如果是需要长期运行的线程池，不用调用该方法。
        //监听程序退出的时候最好执行一下。
        pool.shutdown();
    }


    /**
     * 执行任务2，实现Runable方式
     *
     * @author SHANHY(365384722 @ QQ.COM)
     * @date 2015年12月4日
     */
    static class ExcuteTask2 implements Runnable {
        private String taskName;
        private String ip;
        private int port;
        private String type;

        public ExcuteTask2(String taskName, String ip, int port, String type) {
            this.taskName = taskName;
            this.ip = ip;
            this.port = port;
            this.type = type;
        }

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep((int) (Math.random() * 1000));// 1000毫秒以内的随机数，模拟业务逻辑处理
//                System.out.println("xianc"+ip+port+type);
                spider(taskName, ip, port, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
/**
 * 执行任务1，实现Callable方式
 *
 * @author SHANHY(365384722 @ QQ.COM)
 * @date 2015年12月4日
 */
//static class ExcuteTask1 implements Callable<String> {
//    private String taskName;
//
//    public ExcuteTask1(String taskName) {
//        this.taskName = taskName;
//    }
//
//    @Override
//    public String call() throws Exception {
//        try {
////              Java 6/7最佳的休眠方法为TimeUnit.MILLISECONDS.sleep(100);
////              最好不要用 Thread.sleep(100);
//            TimeUnit.MILLISECONDS.sleep((int)(Math.random() * 1000));// 1000毫秒以内的随机数，模拟业务逻辑处理
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("-------------这里执行业务逻辑，Callable TaskName = " + taskName + "-------------");
//        return ">>>>>>>>>>>>>线程返回值，Callable TaskName = " + taskName + "<<<<<<<<<<<<<<";
//    }
//}
//       for (int i = 0; i < 200; i++) {
//            Future<?> future = pool.submit(new ExcuteTask1(i+""));
////          try {
////              如果接收线程返回值，future.get() 会阻塞，如果这样写就是一个线程一个线程执行。所以非特殊情况不建议使用接收返回值的。
////              System.out.println(future.get());
////          } catch (Exception e) {
////              e.printStackTrace();
////          }
//        }