# model-tool-core

#### 介绍
常用的基本模型工具类

#### 软件架构
简单的出入参模型，列表执行器模型，基本的工具类，主要是不想引入太多的依赖，简单实现一些常用的功能

#### 使用说明
```
 <dependency>
   <groupId>com.github.doobo</groupId>
   <artifactId>model-tool-core</artifactId>
   <version>1.0</version>
 </dependency>
```

#### 在项目中使用
```code
 public void testBHandler(){
        MbHandlerFactory factory = new MbHandlerFactory();
        factory.addHandler(new Mb1Handler());
        factory.addHandler(new Mb2Handler());
        factory.addHandler(new Mb3Handler());
        MaHandler.ARequest request = new MaHandler.ARequest();
        request.setName("Mb2Handler");
        String name = factory.executeHandler(request, MbHandler::sayName);
        System.out.println(name);//输出:Mb3Handler,看test用例
    }
```
