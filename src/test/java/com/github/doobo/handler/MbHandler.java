package com.github.doobo.handler;


/**
 * 接口功能描述
 *
 * @Description: model-tool-core
 * @User: diding
 * @Time: 2023-06-21 16:05
 */
public abstract class MbHandler implements DoMapHandler<MaHandler.ARequest>{
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
    public Class<MaHandler.ARequest> _rsType() {
        return MaHandler.ARequest.class;
    }

}
