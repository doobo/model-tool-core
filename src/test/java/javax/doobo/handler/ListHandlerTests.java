package javax.doobo.handler;

import junit.framework.TestCase;

/**
 * 接口功能描述
 *
 * @Description: model-tool-core
 * @User: diding
 * @Time: 2023-06-21 16:25
 */
public class ListHandlerTests extends TestCase {

    public void testAHandler(){
        AHandlerFactory factory = new AHandlerFactory();
        factory.addHandler(new A1Handler());
        factory.addHandler(new A2Handler());
        AHandler.ARequest request = new AHandler.ARequest();
        request.setName("A2Handler");
        String name = factory.executeHandler(request, AHandler::sayName);
        System.out.println(name);
    }

    public void testBHandler(){
        BHandlerFactory factory = new BHandlerFactory();
        factory.addHandler(new B1Handler());
        factory.addHandler(new B2Handler());
        BHandler.BRequest request = new BHandler.BRequest();
        request.setName("B1Handler");
        String name = factory.executeHandler(request, BHandler::sayName);
        System.out.println(name);
    }
}
