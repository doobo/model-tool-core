package com.github.doobo.model;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 包装返回体
 */
public abstract class DoBasisRequest<Rsp> implements IBasisRequest<Rsp>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回类型
     */
    public Class<Rsp> _rsType(){
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new IllegalArgumentException("No Rsp info in DoBasisRequest");
        } else {
            //noinspection unchecked
            return (Class<Rsp>) ((ParameterizedType)superClass).getActualTypeArguments()[0];
        }
    }
}
