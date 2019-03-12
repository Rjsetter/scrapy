###毕业设计-基于Selenium的网络爬虫实现及数据的分析 
<hr>

>微博热门用户信息爬取

   入口地址：https://d.weibo.com/1087030002_417
   <br>分类一：https://d.weibo.com/1087030002_2975_5007_0#


>数据库设计
    
    ---->爬取分类信息存储的表<-------
    CREATE TABLE `tb_weibo` (MENT,
      `type` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '分类',
      `nickName` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '微博名',
      `sex` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '性别',
      `concernNum` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '关注数',
      `fansNum` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '粉丝数',
      `weiboNum` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '微博数',
      `address` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '地址',
      `jianJie` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '简介',
      `label` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '标签',
      `infoid` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户唯一标识Id',
      PRIMARY KEY (`id`) USING BTREE,
      KEY `infoid` (`infoid`)
    ) ENGINE=InnoDB AUTO_INCREMENT=65394 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
    
    ---->爬取分类信息的表<-------
    CREATE TABLE `tb_weibo_type` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `type` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '博主类型',
      `typeUrl` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '博主类型的地址',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4;
    
    ---->爬取博主关注列表的表<-------
    CREATE TABLE `tb_weibo_followers` (
      `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
      `uid` varchar(10) NOT NULL COMMENT '外键-关联数据表的uid',
      `screen_name` varchar(20) NOT NULL COMMENT '微博名',
      `profile_url` varchar(50) DEFAULT NULL COMMENT '个人微博主页网址',
      `profile_image_url` varchar(30) DEFAULT NULL COMMENT '头像网址',
      `verified` varchar(10) DEFAULT NULL COMMENT '是否为认证用户',
      `followers_count` int(10) DEFAULT NULL COMMENT '粉丝数',
      `follow_count` int(10) DEFAULT NULL COMMENT '关注数',
      `followers_uid` varchar(10) DEFAULT NULL COMMENT '粉丝的UID',
      PRIMARY KEY (`id`),
      KEY `uid` (`uid`),
      CONSTRAINT `tb_weibo_followers_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `tb_weibo` (`infoid`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

>爬虫入口主函数
   
    scrapy.WebSpider.Spider.main
    
>注意

外部依赖需要对应的Chromedriver,对应的chrome版本为72


>通过分析得的微博用户的数据接口
    
    获取用户粉丝或者关注的通用接口，接口地址和参数如下：
    https://m.weibo.cn/api/container/getIndex?containerid=
    
    1.关注：231051_-_followers_-_1195242865
        其中1195242865为用户的唯一标识，只要更改这个标识即可获取不同用户的关注列表
    2.粉丝：231051_-_fans_-_1195242865
        为该用户的粉丝接口，好像每个用户可以查询的上限页数不一样，但是都只能查粉丝数的小部分
    3.页数（？）：since_id
        这个参数应该是与页数相同功能
    
    
     获取用户个人主页信息的接口：
     https://m.weibo.cn/profile/info?uid=3505635557
     参数uid为用户的唯一标识
     
     https://m.weibo.cn/profile/5872210260
     更改Uid查看用户相关
     
     
     
>关于代理IP的问题
    
        现在打算从西刺网爬取高匿，存活天数大于1天，且是有效的IP地址，目前爬取前49页，
    4900条ip地址只有17条是符合要求的，现在准备爬取至少300条有效的ip地址来支持爬虫的
    有序进行
    
>关于并发爬取关注时遇到的问题与解决方法
    
        1.并发爬取过程因为胃设置连接超时，有时候爬取会进入假死状态，刚开始采用的是数
        据库添加判断字段，数据爬取完成的博主信息，给一条true逻辑判断，在程序跑死的
        时候，删除数据库中，该分类博主判断标志位false,却已经在数据库落库的信息，再
        重跑。但是这种是治标不治本的方法，现在准备设置连接超时和读取数据，如果超时，
        轮询，再重新进行请求。
        2.数据量庞大，目前只是爬取了四个分类的博主，的关注列表就有838375条数据，
        预计整个项目爬取得数据信息量应该为千万级别，对数据处理还有数据存储应该做好准
        备。