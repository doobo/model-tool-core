package com.github.doobo.handler;

/**
 * 多列表处理器,适用条件复杂,执行器不固定的场景
 *
 */
public interface DoListHandler<R> {

    /**
     * 优先级
     */
    default int getPhase() {
        return Long.SIZE;
    }

    /**
     * 简单判断任务类型是否匹配
     */
    default boolean matching(R request){
        return false;
    }

    /**
     * R的类型
     */
    Class<R> _rsType();
}
