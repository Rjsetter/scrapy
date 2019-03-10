package scrapy.mappersImp;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import scrapy.myppers.IpMapper;
import scrapy.pojo.IPBean;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class ipMapperImp {
    public static IpMapper ipMapper;
    public static SqlSession sqlSession;

    public static void initSet() throws IOException {
        // 指定配置文件
        String resource = "mybatis-config.xml";
        // 读取配置文件
        InputStream inputStream = Resources.getResourceAsStream(resource);
        // 构建sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 获取sqlSession
        sqlSession = sqlSessionFactory.openSession(true);
        // 1. 映射文件的命名空间（namespace）必须是mapper接口的全路径
        // 2. 映射文件的statement的id必须和mapper接口的方法名保持一致
        // 3. Statement的resultType必须和mapper接口方法的返回类型一致
        // 4. statement的parameterType必须和mapper接口方法的参数类型一致（不一定）
        ipMapper = sqlSession.getMapper(IpMapper.class);
    }

    public static void insert(IPBean ipBean) {
        try{
            initSet();
        }catch (IOException e){
            System.out.println("数据插入有误！"+ipBean);
        }
        ipMapper.insertIp(ipBean);
    }

    public static boolean deleteAllIp(){
        try{
            initSet();
        }catch (IOException e){
            System.out.println("操作有误！");
        }
        if(ipMapper.deleteAllIp()>0)
            return true;
        return false;
    }

    public static List<IPBean> getAllIps(){
        try{
            initSet();
        }catch (IOException e){
            System.out.println("查询有误！");
        }
        return ipMapper.getAllIp();
    }

    public static void main(String []args){
//        IPBean ipBean = new IPBean("128.125.100.1", 2255, 0);
//        insert(ipBean);
//        System.out.println("是否删除成功："+deleteAllIp());
        List<IPBean> ips =getAllIps();
        System.out.println(ips.size());
        ips.forEach(ipbean->{System.out.print("ip地址："+ipbean.getIp()+",端口号："+ipbean.getPort()
                +",请求类型："+ipbean.getType()+"\n");});
    }
}
