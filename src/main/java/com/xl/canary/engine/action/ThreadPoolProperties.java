package com.xl.canary.engine.action;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by gqwu on 2018/3/20.
 * 线程池配置
 */
@ConfigurationProperties(prefix = "canary.thread.pool")
public class ThreadPoolProperties {

    private String poolName;

    private int corePoolSize;

    private int maxPoolSize;

    private int queueCapacity;

    private int keepAliveSeconds;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }
}
