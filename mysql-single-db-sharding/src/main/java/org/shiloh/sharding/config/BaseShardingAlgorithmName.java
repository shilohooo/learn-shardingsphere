package org.shiloh.sharding.config;

import org.apache.shardingsphere.infra.algorithm.core.config.AlgorithmConfiguration;

/**
 * @author shiloh
 * @date 2024/9/30 11:23
 */
public interface BaseShardingAlgorithmName {
    /**
     * 获取分片算法名称
     *
     * @return 分片算法名称
     * @author shiloh
     * @date 2024/9/30 11:26
     */
    String getAlgorithmName();

    /**
     * 获取分片算法配置
     *
     * @return 分片算法配置
     * @author shiloh
     * @date 2024/9/30 11:23
     */
    AlgorithmConfiguration getAlgorithmConfiguration();
}
