# ShardingSphere - MySQL 单库分片 DEMO

> 参考资料：
> - [ShardingSphere 标准分片算法 - 行表达式分片算法](https://shardingsphere.apache.org/document/5.5.0/cn/user-manual/common-config/builtin-algorithm/sharding/#行表达式分片算法)
> - [ShardingSphere 标准分片算法 - 时间范围分片算法](https://shardingsphere.apache.org/document/5.5.0/cn/user-manual/common-config/builtin-algorithm/sharding/#时间范围分片算法)

## 内容概览

- [ ] 单表分片配置
    - [x] 按单个普通字段分片
    - [x] 按单个日期字段按月分片
    - [ ] 按多个字段分片
- [x] 不分片的表如何配置查询规则
- [ ] 单表分片 CRUD 单元测试
    - [x] [按单个普通字段分片](./src/test/java/org/shiloh/single/SingleTableNormalColumnShardingTests.java)
    - [x] [按单个日期字段按月分片](./src/test/java/org/shiloh/single/SingleTableDateColumnShardingTests.java)
    - [ ] 按多个字段分片