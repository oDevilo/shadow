# shadow

route 接口,用于分库

实现方案：读写分离，db还是在mybatis-config配置
shadowconfig配置文件如下

1.<dbStrategy>一个类，配置db的分库策略，配置db的list
2.<tableStrategy>多个表的分表策略，配置对应的table的list

库需要有group，相同group中的建表ddl相同
如果group中增加新的dbserver，
那么必须要保证之前的数据路由是到之前的数据库


db的配置还是在mybatis-config
获取db的id来进行router分配

分表