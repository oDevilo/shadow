# shadow

## TODO
 1. 批量插入更新删除，多库查询
 2. 尝试支持Mapper形式
 3. 支持事务的管理
 4. 目前传参用map，要考虑model，还有基本类型

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