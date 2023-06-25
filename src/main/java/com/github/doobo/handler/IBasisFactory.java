package com.github.doobo.handler;

/**
 * 公共请求体
 */
public interface IBasisFactory<Rsp> {

    /**
     * Rsp的类型
     */
    Class<Rsp> _rsType();
}
