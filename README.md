# shadow

route 接口,用于分库

实现方案：读写分离

库需要有group，相同group中的建表ddl相同
如果group中增加新的dbserver，
那么必须要保证之前的数据路由是到之前的数据库


分表