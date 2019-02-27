package scrapy;

import com.eclipsesource.json.JsonObject;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import scrapy.myppers.UserMapper;
import scrapy.pojo.User;
import tech.tablesaw.api.Table;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static scrapy.Util.bulidWordCloud.testDemo;

public class userMapperTest {
    public UserMapper userMapper;
    public SqlSession sqlSession;

    @Before
    public void setUp() throws Exception {
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
        this.userMapper = sqlSession.getMapper(UserMapper.class);
    }

    @Test
    public void queryUserAll(){
        List<User> userList = this.userMapper.queryUserAll();
        for (User user : userList) {
            System.out.println(user);
        }
    }

    @Test
    public void queryUserByLabel() throws IOException {
        List<User> userList = this.userMapper.queryUserByLabel("广东");
        System.out.println(userList.size());
        for (User user : userList) {
            System.out.println(user);
        }
    }

    @Test
    public void queryAllAddress(){
        List<String> addressList = this.userMapper.queryAllAddress();
        for(String address:addressList)
            System.out.println(address);
        testDemo(addressList);
    }

    @Test
    public void queryAllLabel(){
        List<String> addressList = this.userMapper.queryAllLabel();
        for(String address:addressList)
            System.out.println(address);
        testDemo(addressList);
    }
}
