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
   <version>1.3</version>
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

/**
 * 测试随机列表
 */
public void testRandom(){
    WeightRandom<String> weightRandom = WeightRandomUtils.ofList(Arrays.asList("a", "b", "c", "d", "e", "f", "g"));
    System.out.println(weightRandom.getElementsByFixed(3));
    System.out.println(weightRandom.getElementsByFixed(3));
    System.out.println(weightRandom.getElementsByFixed(3));
    System.out.println(weightRandom.getElementsByFixed(3));
    System.out.println(weightRandom.getElementsByFixed(3));
}

/**
* 公共返回模型
*/
public void testTemplate(){
    DoTemplate<Boolean> template = DoResultUtils.of(true);
    System.out.println(JSON.toJSONString(template));
}

/**
 * 测试日期工具
 */
public void testDateSplit(){
    List<DoDateSplitUtils.DateSplit> dateSplits = DoDateSplitUtils.splitByDay(
            DoDateUtils.getDate("2023-05-28")
            , DoDateUtils.getDate("2023-06-28")
            , 2
    );
    dateSplits.forEach(m ->{
        System.out.println(m.getStartDateTimeStr() + "-->" + m.getEndDateTimeStr());
    });
}
```
