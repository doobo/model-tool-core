package com.github.doobo.handler;

import lombok.Data;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

/**
 * 接口功能描述
 *
 * @Description: model-tool-core
 * @User: diding
 * @Time: 2023-06-21 16:05
 */
public abstract class BHandler<R> implements DoListHandler<R>{
    /**
     * 执行方法名
     */
    public String sayName(){
        return this.getClass().getName();
    }

    public boolean matching(BRequest request) {
        if(Objects.isNull(request)){
            return false;
        }
        return Objects.equals(request.getName(), this.getClass().getSimpleName());
    }

    /**
     * 返回类型
     */
    public Class<R> _rsType(){
        //noinspection unchecked
        return (Class<R>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Data
    public static class BRequest {

        private String name;
    }
}
