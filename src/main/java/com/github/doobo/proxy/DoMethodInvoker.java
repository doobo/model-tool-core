package com.github.doobo.proxy;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代理实现类
 */
public abstract class DoMethodInvoker {

    /**
     * 简单缓存实例
     */
    protected final static Map<Class<?>, Object> WEAK_MAP = new ConcurrentHashMap<>();

    public <T> T getInstance(Class<T> cls){
        if(!cls.isInterface()){
            throw new IllegalArgumentException("class not interface!");
        }
        if(WEAK_MAP.containsKey(cls)){
            //noinspection unchecked
            return (T) WEAK_MAP.get(cls);
        }
        Object newProxyInstance = Proxy.newProxyInstance(
                cls.getClassLoader(),
                new Class[] {cls},
                getMethodHandler());
        WEAK_MAP.put(cls, newProxyInstance);
        //noinspection unchecked
        return (T) newProxyInstance;
    }

    public abstract <T extends DoMethodProxy> T getMethodHandler();
}
