package com.github.doobo.handler;

import com.github.doobo.model.IBasisRequest;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * 包装返回体
 */
public abstract class DoBasisFactory<Rsp> implements IBasisRequest<Rsp>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回类型
     */
    public Class<Rsp> _rsType(){
        //noinspection unchecked
        return (Class<Rsp>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
