package javax.doobo.handler;

import junit.framework.TestCase;

import java.util.Arrays;

/**
 * 接口功能描述
 *
 * @Description: model-tool-core
 * @User: diding
 * @Time: 2023-06-21 16:25
 */
public class MapHandlerTests extends TestCase {

    public void testMaHandler(){
        MaHandlerFactory factory = new MaHandlerFactory();
        factory.addHandlerList(Arrays.asList(new Ma1Handler(), new Ma2Handler()));
        MaHandler.ARequest request = new MaHandler.ARequest();
        request.setName("Ma2Handler");
        String name = factory.executeHandler(request, MaHandler::sayName);
        System.out.println(name);
    }

    public void testBHandler(){
        MbHandlerFactory factory = new MbHandlerFactory();
        factory.addHandler(new Mb1Handler());
        factory.addHandler(new Mb2Handler());
        factory.addHandler(new Mb3Handler());
        MaHandler.ARequest request = new MaHandler.ARequest();
        request.setName("Mb2Handler");
        String name = factory.executeHandler(request, MbHandler::sayName);
        System.out.println(name);
    }
}
