package org.shiloh.sharding.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.infra.algorithm.core.config.AlgorithmConfiguration;
import org.shiloh.entity.User;
import org.shiloh.sharding.algorithm.AttendanceHintShardingAlgorithm;
import org.shiloh.sharding.constant.ShardingSphereConstant.ShardingAlgorithmType;

import java.util.Properties;

/**
 * 分片算法枚举
 *
 * @author shiloh
 * @date 2024/9/30 11:07
 */
@Getter
@RequiredArgsConstructor
public enum ShardingAlgorithmName implements BaseShardingAlgorithmName {
    // 用户信息表分片算法配置
    USER_INLINE("t_user_inline") {
        /**
         * 分片算法表达式 key
         */
        private static final String ALGORITHM_EXPRESSION_KEY = "algorithm-expression";

        /**
         * 获取分片算法配置
         *
         * @return 分片算法配置
         * @author shiloh
         * @date 2024/9/30 11:23
         */
        @Override
        public AlgorithmConfiguration getAlgorithmConfiguration() {
            final Properties properties = new Properties();
            // 分片算法表达式：部门 ID % 2
            final String algorithmExpr = String.format("%s_${%s %% 2}", ShardingTableName.USER.getLogicTableName(),
                    User.SHARDING_COLUMN);
            properties.setProperty(ALGORITHM_EXPRESSION_KEY, algorithmExpr);
            return new AlgorithmConfiguration(ShardingAlgorithmType.INLINE, properties);
        }
    },

    // 订单表分片算法配置
    ORDER_INTERVAL("t_order_interval") {
        /**
         * 分片键的时间戳格式，必须遵循 Java DateTimeFormatter 的格式。例如：yyyy-MM-dd HH:mm:ss，yyyy-MM-dd 或 HH:mm:ss 等。
         */
        private static final String DATETIME_PATTERN_KEY = "datetime-pattern";

        /**
         * 时间分片下界值，格式与 datetime-pattern 定义的时间戳格式一致，必须指定，否则查询报错
         */
        private static final String DATETIME_LOWER_KEY = "datetime-lower";

        /**
         * 分片键时间间隔单位，必须遵循 Java ChronoUnit 的枚举值：按天 = DAYS，按月 = MONTHS
         */
        private static final String DATETIME_INTERVAL_UNIT_KEY = "datetime-interval-unit";

        /**
         * 分片数据源或真实表的后缀格式，必须遵循 Java DateTimeFormatter 的格式，必须和 datetime-interval-unit 保持一致。
         */
        private static final String SHARDING_SUFFIX_PATTERN_KEY = "sharding-suffix-pattern";

        /**
         * 获取分片算法配置
         *
         * @return 分片算法配置
         * @author shiloh
         * @date 2024/9/30 11:23
         */
        @Override
        public AlgorithmConfiguration getAlgorithmConfiguration() {
            final Properties properties = new Properties();
            properties.setProperty(DATETIME_PATTERN_KEY, "yyyyMM");
            properties.setProperty(DATETIME_LOWER_KEY, "202001");
            properties.setProperty(DATETIME_INTERVAL_UNIT_KEY, "MONTHS");
            properties.setProperty(SHARDING_SUFFIX_PATTERN_KEY, "yyyyMM");
            return new AlgorithmConfiguration(ShardingAlgorithmType.INTERVAL, properties);
        }
    },

    // 打卡记录表分片算法配置
    ATTENDANCE_RECORD_HINT("t_attendance_record_hint") {
        private static final String STRATEGY_KEY = "strategy";
        private static final String ALGORITHM_CLASS_NAME_KEY = "algorithmClassName";

        /**
         * 获取分片算法配置
         * <p/>
         * 自定义类分片算法不用指定分片字段
         *
         * @return 分片算法配置
         * @author shiloh
         * @date 2024/9/30 11:23
         */
        @Override
        public AlgorithmConfiguration getAlgorithmConfiguration() {
            final Properties properties = new Properties();
            properties.setProperty(STRATEGY_KEY, "HINT");
            properties.setProperty(ALGORITHM_CLASS_NAME_KEY, AttendanceHintShardingAlgorithm.class.getName());
            return new AlgorithmConfiguration(ShardingAlgorithmType.HINT_CLASS_BASED, properties);
        }
    };

    /**
     * 分片算法名称
     */
    private final String algorithmName;
}
