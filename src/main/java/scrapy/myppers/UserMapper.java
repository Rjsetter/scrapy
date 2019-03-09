package scrapy.myppers;

import org.apache.ibatis.annotations.Param;
import scrapy.pojo.User;

import java.util.List;

public interface UserMapper {

    /**
     *
     * @return
     */
    public List<User> queryUserAll();

    /**
     * 通过标签来搜索微博用户数据
     * @param address
     * @return
     */
    public List<User> queryUserByLabel(@Param("address") String address);

    /**
     * 查询所有地址名称
     * @return
     */
    public List<String> queryAllAddress();

    /**
     * 查询所有标签
     * @return
     */
    public List<String> queryAllLabel();

    /**
     * 传入user类,插入数据
     * @param user
     */
    public void insertUser(User user);

    /**
     * 根据类型查询id
     * @param type
     * @return
     */
    public List<String> queryAllUidByType(@Param("type") String type);
}
