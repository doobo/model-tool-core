package com.github.doobo.handler;

import lombok.Data;


/**
 * 接口功能描述
 *
 * @Description: model-tool-core
 * @User: diding
 * @Time: 2023-06-21 16:05
 */
public abstract class MaHandler implements DoMapHandler<MaHandler.ARequest>{
    /**
     * 执行方法名
     */
    public String sayName(){
        return this.getClass().getName();
    }

    @Override
    public String getMapKey() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Class<ARequest> _rsType() {
        return ARequest.class;
    }

    @Data
    public static class ARequest {

        private String name;

    }
}
