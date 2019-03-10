package scrapy.myppers;

import scrapy.pojo.IPBean;

import java.util.List;

public interface IpMapper {
    /**
     * 插入ip
     * @param ipBean
     */
    public void insertIp(IPBean ipBean);

    /**
     * 删除数据库中的所有ip地址
     */
    public int deleteAllIp();

    /**
     * 获取所有IP地址
     * @return
     */
    public List<IPBean> getAllIp();

}
