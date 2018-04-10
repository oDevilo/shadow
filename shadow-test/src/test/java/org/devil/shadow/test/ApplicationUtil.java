package org.devil.shadow.test;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApplicationUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBeanByType(Class<T> clazz) {
        Map<String, T> beans = applicationContext.getBeansOfType(clazz);

        if (beans != null && !beans.isEmpty()) {
            if (beans.size() > 1) {
                throw new RuntimeException("there are " + beans.size() + " implements for " + clazz);
            }
            return beans.values().iterator().next();
        }
        return null;
    }

    public static <T> List<T> getBeansByType(Class<T> clazz) {
        Map<String, T> beans = applicationContext.getBeansOfType(clazz);

        if (beans != null && !beans.isEmpty()) {
            if (beans.size() > 1) {
                throw new RuntimeException("there are " + beans.size() + " implements for " + clazz);
            }
            return new ArrayList<T>(beans.values());
        }
        return null;
    }
}
