package javax.doobo.handler;


import java.util.Collections;
import java.util.Set;

/**
 * 接口功能描述
 *
 * @Description: model-tool-core
 * @User: diding
 * @Time: 2023-06-21 16:05
 */
public class Mb3Handler extends MbHandler{
    /**
     * 执行方法名
     */
    public String sayName(){
        return this.getClass().getName();
    }

    @Override
    public Set<String> getMapKeys() {
        return Collections.singleton(Mb2Handler.class.getSimpleName());
    }

    @Override
    public int getPhase() {
        return Integer.SIZE;
    }
}
