package org.cryptotrader.api.library.component;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext context;
    
    @Override
    public void setApplicationContext(ApplicationContext context) {
        SpringContext.context = context;
    }
    
    public static ApplicationContext getContext() {
        return context;
    }
    
    public static <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }
}
