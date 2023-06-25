package com.github.doobo;

import com.alibaba.fastjson.JSON;
import com.github.doobo.utils.DoResultUtils;
import junit.framework.TestCase;

import com.github.doobo.map.DoMultiValueMap;
import com.github.doobo.map.LinkedMultiValueMap;
import com.github.doobo.model.DoTemplate;

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
}
