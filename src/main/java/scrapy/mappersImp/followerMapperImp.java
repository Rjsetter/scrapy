package scrapy.mappersImp;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import scrapy.myppers.FollowerMapper;
import scrapy.pojo.Follower;

import java.io.IOException;
import java.io.InputStream;

public class followerMapperImp {
    public static SqlSession sqlSession;
    public static FollowerMapper followerMapper;

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
        followerMapper = sqlSession.getMapper(FollowerMapper.class);
    }

    //插入博主分类信息
    public static void insertType(Follower follower) {
        try{
            initSet();
        }catch (IOException e){
            System.out.println("数据插入有误！"+follower);
        }
        followerMapper.insertFollower(follower);
    }

    public static void main(String []args){
        Follower follower = new Follower();
        follower.setFollowers_uid("12444");
        follower.setUid("3037994791");
        follower.setScreen_name("sdfsdf");
        insertType(follower);
    }
}
