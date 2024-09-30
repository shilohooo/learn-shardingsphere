package org.shiloh.sharding.constant;

/**
 * Sharding Sphere 相关常量
 *
 * @author shiloh
 * @date 2024/9/30 10:29
 */
public final class ShardingSphereConstant {
    private ShardingSphereConstant() {
    }

    /**
     * Sharding Sphere 数据库名称
     */
    public static final String LOGIC_DB_NAME = "logic_db";

    /**
     * 运行模式类型。可选配置：Standalone（单机）、Cluster（集群）
     */
    public static final String MODE = "Standalone";

    /**
     * 主数据源名称
     */
    public static final String MASTER_DATA_SOURCE_NAME = "master";

    /**
     * 分布式序列生成器名称
     */
    public static final String KEY_GENERATOR_NAME = "snowflake";

    /**
     * 分布式序列列名称
     */
    public static final String KEY_COLUMN = "id";

    /**
     * 分片算法类型常量
     *
     * @author shiloh
     * @date 2024/9/30 11:57
     */
    public static final class ShardingAlgorithmType {
        private ShardingAlgorithmType() {
        }

        /**
         * 标准分片算法 - 行表达式分片算法
         */
        public static final String INLINE = "INLINE";

        /**
         * 标准分片算法 - 时间范围分片算法
         */
        public static final String INTERVAL = "INTERVAL";
        
        /**
         * HINT 分片算法 - 自定义类分片算法
         */
        public static final String HINT_CLASS_BASED = "CLASS_BASED";

    }
}
