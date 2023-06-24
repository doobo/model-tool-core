package javax.doobo.handler;

import lombok.Data;

import java.util.Objects;

/**
 * 接口功能描述
 *
 * @Description: model-tool-core
 * @User: diding
 * @Time: 2023-06-21 16:05
 */
public abstract class AHandler implements DoListHandler<AHandler.ARequest>{
    /**
     * 执行方法名
     */
    public String sayName(){
        return this.getClass().getName();
    }

    @Override
    public boolean matching(ARequest request) {
        if(Objects.isNull(request)){
            return false;
        }
        return Objects.equals(request.getName(), this.getClass().getSimpleName());
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
