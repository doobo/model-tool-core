package com.github.doobo;

import com.alibaba.fastjson.JSON;
import com.github.doobo.map.DoLinkedMultiValueMap;
import com.github.doobo.model.WeightRandom;
import com.github.doobo.utils.*;
import junit.framework.TestCase;

import com.github.doobo.map.DoMultiValueMap;
import com.github.doobo.model.DoTemplate;

import java.util.Arrays;
import java.util.Date;
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
        System.out.println(JSON.toJSONString(DoResultUtils.of(1)));
        System.out.println(JSON.toJSONString(DoResultUtils.of(new byte[]{1,2,3})));
        System.out.println(JSON.toJSONString(DoResultUtils.of(new char[]{'a','b','c'})));
        System.out.println(JSON.toJSONString(DoResultUtils.of(new short[]{'A','B','C'})));
    }

    /**
     * 测试多值map
     */
    public void testMap(){
        DoMultiValueMap<String, Object> map = new DoLinkedMultiValueMap<>();
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
        System.out.println(DoStringUtils.sha256("abc"));
        System.out.println(DoStringUtils.sha256("abc"));
        System.out.println(DoStringUtils.md5Hex("bbc"));
        System.out.println(DoStringUtils.md5Hex("bbc"));
    }

    public void testNumeric(){
        System.out.println(DoStringUtils.isPointNumeric("1.123.2"));
        System.out.println(DoStringUtils.isPointNumeric("1.123"));
        System.out.println(DoStringUtils.isPointNumeric("1.123."));
    }

    public void testTime(){
        Date start = DoDateUtils.getDateTime("2023-05-28 08:00:00");
        //start的伦敦时间8点，对应的系统时间，北京时间16点
        Date date = DoDateUtils.convertToSystemTime(start, "UTC+0");
        System.out.println(DoDateUtils.formatDateByYmdHms(date));
    }
}
