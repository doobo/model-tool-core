package javax.doobo.handler;

import java.util.Collections;
import java.util.Set;

/**
 * 多Key列表处理器,适用于条件简单固定的场景
 *
 */
public interface DoMapHandler<R> {

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
        return true;
    }

    /**
     * 获取执行KEY
     */
    default String getMapKey(){
        return DoMapHandler.class.getName();
    }

    /**
     * 获取执行KEY集合
     */
    default Set<String> getMapKeys(){
        return Collections.emptySet();
    }

    /**
     * R的类型
     */
    Class<R> _rsType();
}
