package org.shiloh.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 工具类
 *
 * @author shiloh
 * @date 2024/10/17 14:41
 */
@Component
public final class SpringUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    /**
     * 根据类型获取 Bean 实例
     *
     * @param clazz 类型
     * @param <T>   类型泛型
     * @return Bean 实例
     * @author shiloh
     * @date 2024/10/17 14:43
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 根据 Bean 名称和类型获取 Bean 实例
     *
     * @param beanName Bean 名称
     * @param clazz    类型
     * @param <T>      类型泛型
     * @return Bean 实例
     * @author shiloh
     * @date 2024/10/17 14:44
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        return applicationContext.getBean(beanName, clazz);
    }
}
