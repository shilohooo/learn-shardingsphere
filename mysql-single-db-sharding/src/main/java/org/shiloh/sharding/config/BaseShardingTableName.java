package org.shiloh.sharding.config;

import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;

/**
 * @author shiloh
 * @date 2024/9/30 11:23
 */
public interface BaseShardingTableName {
    /**
     * 获取逻辑表名称
     *
     * @return 逻辑表名称
     * @author shiloh
     * @date 2024/9/30 11:24
     */
    String getLogicTableName();

    /**
     * 获取分片表配置
     *
     * @return 分片表配置
     * @author shiloh
     * @date 2024/9/30 11:24
     */
    ShardingTableRuleConfiguration getShardingTableRuleConfiguration();
}
