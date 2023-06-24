package javax.doobo.handler;

import java.util.Objects;

/**
 * 接口功能描述
 *
 * @Description: model-tool-core
 * @User: diding
 * @Time: 2023-06-21 16:05
 */
public class MbHandlerFactory extends DoMapHandlerFactory<MaHandler.ARequest, MbHandler> {

    @Override
    public String getMapKey(MaHandler.ARequest request) {
        return Objects.isNull(request)? MaHandler.class.getSimpleName(): request.getName();
    }
}
