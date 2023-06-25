package com.github.doobo.model;

import java.util.function.Consumer;

/**
 * 钩子型元组
 */
public interface DoHookTuple {

    /**
     * 下一步执行钩子
     */
    default <K, V> Consumer<V> beforeTuple(K k){
        return null;
    }

    /**
     * 结束型钩子
     */
    default <K, V> Consumer<V> endTuple(K k){
        return null;
    }

    /**
     * 异常型钩子
     */
     default <T> Consumer<T> errorTuple(Throwable e){
         return null;
     };
}
