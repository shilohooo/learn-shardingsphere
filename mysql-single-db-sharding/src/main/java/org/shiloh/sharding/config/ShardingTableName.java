package org.shiloh.sharding.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.HintShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.shiloh.entity.User;
import org.shiloh.sharding.constant.ShardingSphereConstant;

/**
 * 分片表枚举
 *
 * @author shiloh
 * @date 2024/9/29 15:37
 */
@Getter
@RequiredArgsConstructor
public enum ShardingTableName implements BaseShardingTableName {
    // 用户信息表分片配置
    USER("t_user") {
        /**
         * 获取分片表配置
         *
         * @return 分片表配置
         * @author shiloh
         * @date 2024/9/30 11:24
         */
        @Override
        public ShardingTableRuleConfiguration getShardingTableRuleConfiguration() {
            final String actualDataNodes = String.format(
                    "%s.%s_${0..1}", ShardingSphereConstant.MASTER_DATA_SOURCE_NAME, this.getLogicTableName()
            );
            final ShardingTableRuleConfiguration configuration = new ShardingTableRuleConfiguration(
                    this.getLogicTableName(), actualDataNodes
            );
            // 设置表分片策略
            configuration.setTableShardingStrategy(new StandardShardingStrategyConfiguration(
                    User.SHARDING_COLUMN, ShardingAlgorithmName.USER_INLINE.getAlgorithmName()
            ));
            // 设置分布式序列生成策略
            configuration.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration(
                    ShardingSphereConstant.KEY_COLUMN, ShardingSphereConstant.KEY_GENERATOR_NAME
            ));
            return configuration;
        }
    },

    // 订单表分片配置
    ORDER("t_order") {
        /**
         * 分片列名称
         */
        private static final String SHARDING_COLUMN = "order_time";

        /**
         * 获取分片表配置
         *
         * @return 分片表配置
         * @author shiloh
         * @date 2024/9/30 11:24
         */
        @Override
        public ShardingTableRuleConfiguration getShardingTableRuleConfiguration() {
            final String actualDataNodes = String.format(
                    "%s.%s_${202408..202409}", ShardingSphereConstant.MASTER_DATA_SOURCE_NAME, this.getLogicTableName()
            );
            final ShardingTableRuleConfiguration configuration = new ShardingTableRuleConfiguration(
                    this.getLogicTableName(), actualDataNodes
            );
            // 设置表分片策略
            configuration.setTableShardingStrategy(new StandardShardingStrategyConfiguration(
                    SHARDING_COLUMN, ShardingAlgorithmName.ORDER_INTERVAL.getAlgorithmName()
            ));
            // 设置分布式序列生成策略
            configuration.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration(
                    ShardingSphereConstant.KEY_COLUMN, ShardingSphereConstant.KEY_GENERATOR_NAME
            ));
            return configuration;
        }
    },

    // 打卡记录表分片配置
    ATTENDANCE_RECORD("t_attendance_record") {
        /**
         * 获取分片表配置
         *
         * @return 分片表配置
         * @author shiloh
         * @date 2024/9/30 11:24
         */
        @Override
        public ShardingTableRuleConfiguration getShardingTableRuleConfiguration() {
            final String actualDataNodes = String.format(
                    "%s.%s_1_${202408..202409}", ShardingSphereConstant.MASTER_DATA_SOURCE_NAME, this.getLogicTableName()
            );
            final ShardingTableRuleConfiguration configuration = new ShardingTableRuleConfiguration(
                    this.getLogicTableName(), actualDataNodes
            );
            // 设置表分片策略
            configuration.setTableShardingStrategy(new HintShardingStrategyConfiguration(
                    ShardingAlgorithmName.ATTENDANCE_RECORD_HINT.getAlgorithmName()
            ));
            // 设置分布式序列生成策略
            configuration.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration(
                    ShardingSphereConstant.KEY_COLUMN, ShardingSphereConstant.KEY_GENERATOR_NAME
            ));
            return configuration;
        }
    };

    /**
     * 逻辑表名称
     */
    private final String logicTableName;
}
