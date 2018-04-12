# shadow

基于mybatis的分库分表工具包，贼给力-_- ~~~

## TODO
 1. 批量插入更新删除，多库查询(优化)
 2. 尝试支持Mapper形式
 3. 支持事务的管理
 4. 目前传参用map，要考虑model，还有基本类型
 5. mybatis-config.xml配置的位置

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

分表：
1.间隔，如1-100到table1，200-300到table2
2.字段hash取余数
3.时间

分库：
1.路由库（多一次查询）
2.直接操作，但是在之后扩展的时候，需要进行数据迁移
（个人感觉基于mybatis插件的方式做分库不太现实,要在mybatis外面重新套）

现有两种分库方式
1.与分表一样，通过hash等，在方法中实现，缺点是如果增加库，则需要进行数据迁移

2.增加router库，每次先去此库查询对应的库，再去对应库获取数据。优点：增加库后，不需要进行数据迁移。缺点：每次数据库查询，需要查两次。可以通过redis等缓存进行优化

3.事务，先做成所有回滚的形式

# ShadowPlugin

插件的方式，进行分表，配置简单，自定义分表路由，十分灵活

mybatis-config.xml

```
<plugins>
    <plugin interceptor="org.devil.shadow.plugin.ShadowPlugin">
        <property name="tableStrategy" value="com/devil/shadow/plugTable/table_strategy.xml" />
    </plugin>
</plugins>
```

table_strategy.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE shadow PUBLIC "-//shadow.devil.com//DTD shadow 2.0//EN"
        "http://shardow.devil.com/dtd/tableRouter-config.dtd">
<shadow>
    <strategy name="test1" strategyClass="com.devil.shadow.plugTable.TestStra">
        <tables>test,user</tables>
    </strategy>
    <strategy name="test2" strategyClass="com.devil.shadow.plugTable.TestStra">
        <tables>test2</tables>
    </strategy>
</shadow>
```

TestStra.java

```
public class TestStra implements TableStrategy {
    @Override
    public String convertTable(String tableName, Object param, String mapperId) {
        Long id;
        if (param instanceof TestModel) {
            id = ((TestModel) param).getId();
        } else if (param instanceof UserModel) {
            id = ((UserModel) param).getId();
        } else if (param instanceof HashMap) {
            id = (Long) ((HashMap) param).get("id");
        } else {
            throw new RuntimeException("类型不正确");
        }

        if (null == id)
            throw new RuntimeException("id不存在");

        return tableName + "_" + id % 4;
    }
}
```