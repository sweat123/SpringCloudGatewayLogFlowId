package com.laomei.test.gatewaytest;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 用于Hystrix和Gateway之间不同线程的流水号传递，本质是不同线程上下文传递
 * @author luobo.hwz on 2022/12/08 10:48 AM
 */
@Component
public class MdcHystrixConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        HystrixPlugins.getInstance().registerConcurrencyStrategy(new MdcHystrixConcurrencyStrategy());
    }

    public static class MdcHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {
        @Override
        public <T> Callable<T> wrapCallable(Callable<T> callable) {
            return new MdcCallable<>(callable, MDC.getCopyOfContextMap());
        }
    }

    public static class MdcCallable<T> implements Callable<T> {

        private final Callable<T> delegate;

        private final Map<String, String> context;

        public MdcCallable(Callable<T> delegate, Map<String, String> context) {
            this.delegate = delegate;
            this.context = context;
        }

        @Override
        public T call() throws Exception {
            try {
                MDC.setContextMap(context);
                return delegate.call();
            } finally {
                MDC.clear();
            }
        }
    }
}
