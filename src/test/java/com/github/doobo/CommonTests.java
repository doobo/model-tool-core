package com.github.doobo;

import com.alibaba.fastjson.JSON;
import com.github.doobo.model.WeightRandom;
import com.github.doobo.utils.*;
import junit.framework.TestCase;

import com.github.doobo.map.DoMultiValueMap;
import com.github.doobo.map.LinkedMultiValueMap;
import com.github.doobo.model.DoTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * 接口功能描述
 *
 * @Description: model-tool-core
 * @User: diding
 * @Time: 2023-06-21 14:03
 */
public class CommonTests extends TestCase {

    public void testTemplate(){
        DoTemplate<Boolean> template = DoResultUtils.of(true);
        System.out.println(JSON.toJSONString(template));
    }

    /**
     * 测试多值map
     */
    public void testMap(){
        DoMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("a", 1);
        map.add("a", 2);
        map.add("a", 3);
        System.out.println(map.get("a"));
    }

    /**
     * 测试随机列表
     */
    public void testRandom(){
        WeightRandom<String> weightRandom = WeightRandomUtils.ofList(Arrays.asList("a", "b", "c", "d", "e", "f", "g"));
        System.out.println(weightRandom.getElementsByFixed(3));
        System.out.println(weightRandom.getElementsByFixed(3));
        System.out.println(weightRandom.getElementsByFixed(3));
        System.out.println(weightRandom.getNoRepeatElementsByFixed(3));
        System.out.println(weightRandom.getNoRepeatElementsByDecrement(3));
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

    public void testSha(){
        System.out.println(DoStringUtils.sha("abc"));
        System.out.println(DoStringUtils.sha("abc"));
        System.out.println(DoStringUtils.sha("bbc"));
    }
}
