###毕业设计-基于Selenium的网络爬虫实现及数据的分析 
<hr>

>微博热门用户信息爬取

   入口地址：https://d.weibo.com/1087030002_417
   <br>分类一：https://d.weibo.com/1087030002_2975_5007_0#


>数据库设计
    
    CREATE TABLE `info` (
      `id` int(50) NOT NULL AUTO_INCREMENT,
      `type` varchar(255) DEFAULT NULL COMMENT '分类',
      `nickName` varchar(255) DEFAULT NULL COMMENT '微博名',
      `sex` varchar(255) DEFAULT NULL COMMENT '性别',
      `concernNum` varchar(255) DEFAULT NULL COMMENT '关注数',
      `fansNum` varchar(255) DEFAULT NULL COMMENT '粉丝数',
      `weiboNum` varchar(255) DEFAULT NULL COMMENT '微博数',
      `address` varchar(30) DEFAULT NULL COMMENT '地址',
      `jianJie` varchar(255) DEFAULT NULL COMMENT '简介',
      `label` varchar(255) DEFAULT NULL COMMENT '标签',
      PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=52506 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

>爬虫入口主函数
   
    scrapy.WebSpider.Spider.main
    
>注意

外部依赖需要对应的Chromedriver,对应的chrome版本为72
