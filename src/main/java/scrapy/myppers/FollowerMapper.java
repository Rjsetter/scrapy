package scrapy.myppers;

import scrapy.pojo.Follower;

public interface FollowerMapper {

    /**
     * 插入关注的人的信息
     * @param follower
     */
    public void insertFollower(Follower follower);
}
