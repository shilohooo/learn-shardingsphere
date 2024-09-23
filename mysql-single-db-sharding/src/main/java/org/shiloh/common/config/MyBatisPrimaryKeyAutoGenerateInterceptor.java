package org.shiloh.common.config;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.shiloh.entity.BaseEntity;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * mybatis 数据库主键自动生成拦截器
 *
 * @author shiloh
 * @date 2024/9/21 11:18
 */
@Slf4j
@Component
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class MyBatisPrimaryKeyAutoGenerateInterceptor implements Interceptor {
    private static final short WORKER_ID = 1;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("拦截器执行 - 雪花ID主键自动生成");
        final MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
        final Object parameter = invocation.getArgs()[1];
        if (!SqlCommandType.INSERT.equals(statement.getSqlCommandType())) {
            return invocation.proceed();
        }

        final BaseEntity baseEntity;
        if (parameter instanceof MapperMethod.ParamMap) {
            // void insert(@Param("user") User user); - 使用了 @Param 注解后，parameter 是一个 Map
            // 该 Map 包含两个 key，一个是 key 为 @Param 注解的 value，另一个 key 为 "param1"（固定的名称）
            // 这两个 key 都指向同一个实例，也就是要插入的实体对象
            final MapperMethod.ParamMap<?> paramMap = (MapperMethod.ParamMap<?>) parameter;
            final Object entity = paramMap.values().iterator().next();
            if (!(entity instanceof BaseEntity)) {
                return invocation.proceed();
            }
            baseEntity = (BaseEntity) entity;
        } else if (parameter instanceof BaseEntity) {
            // void insert(User user); - 没有使用 @Param 注解，parameter 的类型即为实体类
            baseEntity = (BaseEntity) parameter;
        } else {
            return invocation.proceed();
        }

        if (baseEntity.getId() != null) {
            return invocation.proceed();
        }

        baseEntity.setId(YitIdHelper.nextId());
        log.info("设置雪花ID主键后的实体信息：{}", baseEntity);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        log.info("MyBatisPrimaryKeyAutoGenerateInterceptor.plugin: {}", target);
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        log.info("MyBatisPrimaryKeyAutoGenerateInterceptor.properties: {}", properties);
        // 雪花 ID 参数配置
        final IdGeneratorOptions options = new IdGeneratorOptions(WORKER_ID);
        YitIdHelper.setIdGenerator(options);
    }
}
