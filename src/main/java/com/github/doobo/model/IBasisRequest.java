package com.github.doobo.model;

/**
 * 公共请求体
 */
public interface IBasisRequest<Rsp> {

    /**
     * Rsp的类型
     */
    Class<Rsp> _rsType();
}
