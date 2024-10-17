package org.shiloh.sharding.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.shiloh.sharding.config.BaseShardingTableName;
import org.shiloh.sharding.config.ShardingAlgorithmName;
import org.shiloh.sharding.config.ShardingSphereDataSourceConfig;
import org.shiloh.sharding.config.ShardingTableName;
import org.shiloh.sharding.constant.ShardingSphereConstant;
import org.shiloh.sharding.manager.ShardingTableManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分库分表动态配置
 *
 * @author shiloh
 * @date 2024/10/12 14:45
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ShardingSphereConfigUpdateRunner implements CommandLineRunner {
    private final DataSource dataSource;
    private final ShardingTableManager shardingTableManager;

    @Override
    public void run(String... args) throws Exception {
        this.shardingTableManager.refreshShardingTableCache();
        this.updateShardingRulesConfig();
    }

    /**
     * 动态更新分库分表规则
     *
     * @throws IllegalAccessException 反射访问失败时抛出
     * @author shiloh
     * @date 2024/10/14 15:30
     */
    private void updateShardingRulesConfig() throws IllegalAccessException {
        // 动态更新分库分表规则
        if (!(this.dataSource instanceof ShardingSphereDataSource)) {
            return;
        }

        final ShardingSphereDataSource shardingSphereDataSource = (ShardingSphereDataSource) dataSource;
        final Field field = ReflectionUtils.findField(
                shardingSphereDataSource.getClass(), "contextManager", ContextManager.class
        );
        if (field == null) {
            return;
        }

        field.setAccessible(true);
        final Object object = field.get(shardingSphereDataSource);
        if (!(object instanceof ContextManager)) {
            return;
        }

        final ContextManager contextManager = (ContextManager) object;
        final ShardingRuleConfiguration ruleConfiguration = new ShardingRuleConfiguration();
        // 添加分片表
        final List<ShardingTableRuleConfiguration> tableRuleConfigurations = Arrays.stream(ShardingTableName.values())
                .map(
                        BaseShardingTableName::getShardingTableRuleConfiguration
                )
                .collect(Collectors.toList());
        ruleConfiguration.getTables().addAll(tableRuleConfigurations);
        // 添加分布式序列生成器
        ruleConfiguration.getKeyGenerators()
                .put(ShardingSphereConstant.KEY_GENERATOR_NAME, ShardingSphereDataSourceConfig.createKeyGenerator());
        // 添加分片算法
        Arrays.stream(ShardingAlgorithmName.values()).forEach(
                item -> ruleConfiguration
                        .getShardingAlgorithms()
                        .put(item.getAlgorithmName(), item.getAlgorithmConfiguration())
        );

        contextManager.getConfigurationContextManager().alterRuleConfiguration(
                ShardingSphereConstant.LOGIC_DB_NAME, ruleConfiguration
        );
    }
}
