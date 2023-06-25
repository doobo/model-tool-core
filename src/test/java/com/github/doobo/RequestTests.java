package com.github.doobo;

import com.github.doobo.model.DoBasisRequest;
import junit.framework.TestCase;

/**
 * 测试包装类型
 *
 * @Description: model-tool-core
 * @User: diding
 * @Time: 2023-06-21 14:50
 */
public class RequestTests extends TestCase {

    /**
     * 获取请求体的返回体类型
     */
    public void testClass(){
        ARequest request = new ARequest();
        Class<BResponse> aClass = request._rsType();
        System.out.println(aClass);
    }

    /**
     * 请求体
     */
    public static class ARequest extends DoBasisRequest<BResponse> {
    }

    /**
     * 返回体
     */
    public static class BResponse {
    }
}
