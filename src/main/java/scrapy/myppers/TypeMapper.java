package scrapy.myppers;

import org.apache.ibatis.annotations.Param;
import scrapy.pojo.Type;

public interface TypeMapper {
    /**
     * 传入user类,插入数据
     * @param type
     */
    public void insertType(Type type);

    /**
     * 根据用户id查询地址
     * @param id
     * @return
     */
    public String queryUrlById(@Param("id") int id);
}
