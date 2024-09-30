package org.shiloh.sharding.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.algorithm.core.config.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.mode.ModeConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.single.api.config.SingleRuleConfiguration;
import org.shiloh.sharding.constant.ShardingSphereConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * Sharding Sphere 数据源配置
 *
 * @author shiloh
 * @date 2024/9/30 9:59
 * @see <a href="https://shardingsphere.apache.org/document/5.5.0/cn/user-manual/shardingsphere-jdbc/java-api/">
 * 参考文档
 * </a>
 */
@Configuration
@EnableConfigurationProperties({DataSourceProperties.class})
public class ShardingSphereDataSourceConfig {
    /**
     * Sharding Sphere 数据源配置
     *
     * @return 数据源
     * @throws SQLException 配置出错时抛出
     * @author shiloh
     * @date 2024/9/30 9:59
     */
    @Bean
    @ConditionalOnExpression("!'${spring.datasource.driver-class-name}'.equals('org.apache.shardingsphere.driver.ShardingSphereDriver')")
    public DataSource shardingDataSource(DataSourceProperties dataSourceProperties) throws SQLException {
        return ShardingSphereDataSourceFactory.createDataSource(
                // 逻辑数据库名称
                ShardingSphereConstant.LOGIC_DB_NAME,
                // 模式配置，未配置就会使用默认的模式（Standalone）
                createModeConfig(),
                // 数据源配置，可以有多个
                createDataSourceMap(dataSourceProperties),
                // 分片规则配置，可以有多个
                createShardingRules(),
                // 属性配置，比如输出 SQL 日志
                createShardingProps()
        );
    }

    /**
     * 模式配置，不配置则默认单机模式
     *
     * @return 模式配置
     * @author shiloh
     * @date 2024/9/30 10:20
     * @see <a href="https://shardingsphere.apache.org/document/5.5.0/cn/user-manual/shardingsphere-jdbc/java-api/mode/">
     * 参考文档
     * </a>
     */
    private static ModeConfiguration createModeConfig() {
        return new ModeConfiguration(ShardingSphereConstant.MODE, null);
    }

    /**
     * 创建 Sharding Sphere 可用的数据源，可以有多个
     * <p>
     * 每个数据源都需要有一个唯一的名称
     *
     * @return 数据源 Map
     * @author shiloh
     * @date 2024/9/30 10:30
     * @see <a href="https://shardingsphere.apache.org/document/5.5.0/cn/user-manual/shardingsphere-jdbc/java-api/data-source/">
     * 参考文档
     * </a>
     */
    private static Map<String, DataSource> createDataSourceMap(DataSourceProperties dataSourceProperties) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dataSourceProperties.getDriverClassName());
        hikariConfig.setJdbcUrl(dataSourceProperties.getUrl());
        hikariConfig.setUsername(dataSourceProperties.getUsername());
        hikariConfig.setPassword(dataSourceProperties.getPassword());
        final HikariDataSource masterDataSource = new HikariDataSource(hikariConfig);

        return Collections.singletonMap(ShardingSphereConstant.MASTER_DATA_SOURCE_NAME, masterDataSource);
    }

    /**
     * 创建分片规则配置
     *
     * @return 分片规则配置列表
     * @author shiloh
     * @date 2024/9/30 10:37
     * @see <a href="https://shardingsphere.apache.org/document/5.5.0/cn/user-manual/shardingsphere-jdbc/java-api/rules/sharding/">
     * 参考文档
     * </a>
     * @see <a href="https://shardingsphere.apache.org/document/5.5.0/cn/user-manual/shardingsphere-jdbc/yaml-config/rules/single/">
     * 未分片的表如何配置
     * </a>
     */
    private static List<RuleConfiguration> createShardingRules() {
        final List<RuleConfiguration> ruleConfigurations = new ArrayList<>(2);
        final ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();

        // 添加分片表
        Arrays.stream(ShardingTableName.values()).forEach(
                shardingTableName -> shardingRuleConfiguration
                        .getTables()
                        .add(shardingTableName.getShardingTableRuleConfiguration())
        );
        // 添加分布式序列生成器
        shardingRuleConfiguration.getKeyGenerators()
                .put(ShardingSphereConstant.KEY_GENERATOR_NAME, createKeyGenerator());
        // 添加分片算法
        Arrays.stream(ShardingAlgorithmName.values()).forEach(
                shardingAlgorithmName -> shardingRuleConfiguration
                        .getShardingAlgorithms()
                        .put(shardingAlgorithmName.getAlgorithmName(), shardingAlgorithmName.getAlgorithmConfiguration())
        );

        ruleConfigurations.add(shardingRuleConfiguration);

        // 未分片的表查询规则
        ruleConfigurations.add(new SingleRuleConfiguration(
                Collections.singleton(String.format("%s.*", ShardingSphereConstant.MASTER_DATA_SOURCE_NAME)),
                ShardingSphereConstant.MASTER_DATA_SOURCE_NAME
        ));

        return ruleConfigurations;
    }

    /**
     * 创建分布式序列生成器
     *
     * @return 分布式序列生成器
     * @author shiloh
     * @date 2024/9/30 11:06
     */
    private static AlgorithmConfiguration createKeyGenerator() {
        final Properties keyGeneratorProps = new Properties();
        keyGeneratorProps.setProperty("worker-id", "1");
        keyGeneratorProps.setProperty("datacenter-id", "1");
        return new AlgorithmConfiguration(
                ShardingSphereConstant.KEY_GENERATOR_NAME.toUpperCase(), keyGeneratorProps
        );
    }

    /**
     * 创建 Sharding Sphere 属性配置
     *
     * @return 属性配置
     * @author shiloh
     * @date 2024/9/30 10:34
     * @see <a href="https://shardingsphere.apache.org/document/5.5.0/cn/user-manual/common-config/props/">参考文档</a>
     */
    private static Properties createShardingProps() {
        final Properties properties = new Properties();
        // 开启 SQL 日志
        properties.setProperty("sql-show", Boolean.TRUE.toString());
        return properties;
    }
}
