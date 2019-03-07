package scrapy.Util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import scrapy.myppers.TypeMapper;
import scrapy.myppers.UserMapper;
import scrapy.pojo.Type;
import scrapy.pojo.User;

import java.io.IOException;
import java.io.InputStream;

public class InserType {
    public static SqlSession sqlSession;
    public static TypeMapper typeMapper;

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
        typeMapper = sqlSession.getMapper(TypeMapper.class);
    }

    //插入博主分类信息
    public static void insertType(Type type) {
        try{
            initSet();
        }catch (IOException e){
            System.out.println("数据插入有误！"+type);
        }
        typeMapper.insertType(type);
    }

    //根据id查询用户地址
    public static String selectUrlById(int id){
        try{
            initSet();
        }catch (IOException e){
            System.out.println("数据插入有误！"+id);
        }
        String url = typeMapper.queryUrlById(id);
        return url;
    }
    public static void main(String []args){
        System.out.println(selectUrlById(2));
    }
}
