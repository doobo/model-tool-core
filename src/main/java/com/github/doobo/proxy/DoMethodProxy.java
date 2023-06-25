package com.github.doobo.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理具体的方法
 */
@Slf4j
public abstract class DoMethodProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果传进来是一个已实现的default方法
        if (method.isDefault()) {
            final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                    .getDeclaredConstructor(Class.class, int.class);
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            final Class<?> declaringClass = method.getDeclaringClass();
            return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
                    .unreflectSpecial(method, declaringClass)
                    .bindTo(proxy).invokeWithArguments(args);
        }else {
            //如果传进来的是一个接口
            return run(method, args);
        }
    }

    /**
     * 实现接口的核心方法
     */
    public abstract Object run(Method method, Object[] args);
}
