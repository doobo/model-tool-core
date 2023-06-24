package javax.doobo;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import javax.doobo.map.DoMultiValueMap;
import javax.doobo.map.LinkedMultiValueMap;
import javax.doobo.model.DoTemplate;
import javax.doobo.utils.DoResultUtils;

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
