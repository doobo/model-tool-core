package com.github.doobo.utils;

import com.github.doobo.model.DoBaseQuery;
import com.github.doobo.model.DoTemplate;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 返回模板工具类
 */
public abstract class DoResultUtils {

    public static <T extends Serializable> DoTemplate<T> of(T data) {
        return of(data, null);
    }

    public static <T extends Serializable> DoTemplate<List<T>> ofList(List<T> data) {
        DoTemplate<List<T>> tDoTemplate = new DoTemplate<>();
        tDoTemplate.setSuccess(true);
        tDoTemplate.setMessage("操作成功");
        tDoTemplate.setData(data);
        return tDoTemplate;
    }

    public static <T extends Serializable> DoTemplate<T> of(T data, String errorInfo) {
        DoTemplate<T> tDoTemplate = new DoTemplate<>();
        if (Objects.isNull(errorInfo)) {
            tDoTemplate.setSuccess(true);
            tDoTemplate.setMessage("操作成功");
            tDoTemplate.setData(data);
        } else {
            tDoTemplate.setSuccess(false);
            tDoTemplate.setState(500);
            tDoTemplate.setData(data);
            tDoTemplate.setMessage(errorInfo);
            tDoTemplate.setSubMsg(errorInfo);
        }
        return tDoTemplate;
    }

    /**
     * 对象转单例集合
     */
    public static <T extends Serializable> DoTemplate<List<T>> singleList(T data) {
        List<T> rs = data != null ? Collections.singletonList(data) : Collections.emptyList();
        return ofList(rs);
    }

    /**
     * 分页对象返回
     */
    public static <T extends Serializable> DoTemplate<List<T>> ofPage(List<T> data, DoBaseQuery page) {
        DoTemplate<List<T>> of = ofList(data);
        if(Objects.nonNull(page)){
            DoBaseQuery query = new DoBaseQuery();
            query.setPageNo(page.getPageNo());
            query.setPageSize(page.getPageSize());
            query.setTotal(page.getTotal());
            query.setCursor(page.getCursor());
            query.setSortList(page.getSortList());
            query.setSortMap(page.getSortMap());
            of.setPage(query);
        }
        return of;
    }

    /**
     * 带消息返回带成功结果
     */
    public static <T extends Serializable> DoTemplate<T> ofMsg(T data, String msg) {
        DoTemplate<T> tDoTemplate = new DoTemplate<>();
        tDoTemplate.setSuccess(true);
        tDoTemplate.setData(data);
        tDoTemplate.setMessage(msg);
        return tDoTemplate;
    }

    /**
     * 列表返回,可带错误信息
     */
    public static <T extends Serializable> DoTemplate<List<T>> ofList(List<T> data, String errorMsg) {
        DoTemplate<List<T>> tDoTemplate = new DoTemplate<>();
        tDoTemplate.setSuccess(true);
        tDoTemplate.setData(data);
        if(Objects.nonNull(errorMsg)){
            tDoTemplate.setSuccess(false);
            tDoTemplate.setState(500);
            tDoTemplate.setMessage(errorMsg);
            tDoTemplate.setSubMsg(errorMsg);
        }
        return tDoTemplate;
    }

    /**
     * 错误信息返回
     */
    public static <T> DoTemplate<T> ofFail(String msg) {
        DoTemplate<T> tDoTemplate = new DoTemplate<>();
        tDoTemplate.setSuccess(false);
        tDoTemplate.setState(500);
        tDoTemplate.setMessage(msg);
        tDoTemplate.setSubMsg(msg);
        return tDoTemplate;
    }

    /**
     * 错误信息返回
     */
    public static <T> DoTemplate<T> ofFail(String msg, String code) {
        DoTemplate<T> tDoTemplate = new DoTemplate<>();
        tDoTemplate.setSuccess(false);
        tDoTemplate.setState(500);
        tDoTemplate.setMessage(msg);
        tDoTemplate.setCode(code);
        tDoTemplate.setSubCode(code);
        tDoTemplate.setSubMsg(msg);
        return tDoTemplate;
    }

    /**
     * 错误信息返回
     */
    public static <T> DoTemplate<T> ofFail(String msg, int code) {
        DoTemplate<T> tDoTemplate = new DoTemplate<>();
        tDoTemplate.setSuccess(false);
        tDoTemplate.setState(code);
        tDoTemplate.setCode(String.valueOf(code));
        tDoTemplate.setMessage(msg);
        tDoTemplate.setSubMsg(msg);
        return tDoTemplate;
    }

	/**
	 * 错误信息返回
	 */
	public static <T> DoTemplate<T> ofSuccess(String msg, String code) {
		DoTemplate<T> tDoTemplate = new DoTemplate<>();
		tDoTemplate.setSuccess(true);
		tDoTemplate.setState(1);
		tDoTemplate.setMessage(msg);
		tDoTemplate.setCode(code);
		tDoTemplate.setSubMsg(msg);
		tDoTemplate.setSubCode(code);
		return tDoTemplate;
	}

    /**
     * 列表返回,可带提示信息
     */
    public static <T extends Serializable> DoTemplate<List<T>> ofListMsg(List<T> data, String msg) {
        DoTemplate<List<T>> tDoTemplate = new DoTemplate<>();
        tDoTemplate.setSuccess(true);
        tDoTemplate.setData(data);
        tDoTemplate.setMessage(msg);
        return tDoTemplate;
    }

    public static DoTemplate<Boolean> ofThrowable(Throwable throwable) {
        if (throwable == null) {
            return of(Boolean.TRUE);
        }
        return ofError(throwable, Boolean.FALSE);
    }

    public static <T extends Serializable> DoTemplate<T> ofError(Throwable throwable, T t) {
        if (throwable == null) {
            return of(t);
        }
        DoTemplate<T> tDoTemplate = new DoTemplate<>();
        tDoTemplate.setThrowable(throwable);
        tDoTemplate.setSuccess(false);
        tDoTemplate.setState(500);
        tDoTemplate.setData(t);
        return tDoTemplate;
    }

    public static DoTemplate<Boolean> ofInt(int effectedNum) {
        if (effectedNum > 0) {
            return of(Boolean.TRUE);
        }
        return of(Boolean.FALSE, "操作失败");
    }

    /**
     * 列表返回
     */
    public static <T> DoTemplate<List<T>> ofUnsafeList(List<T> data) {
        DoTemplate<List<T>> tDoTemplate = new DoTemplate<>();
        tDoTemplate.setSuccess(true);
        tDoTemplate.setMessage("操作成功");
        tDoTemplate.setData(data);
        return tDoTemplate;
    }

    /**
     * 分页对象返回
     */
    public static <T> DoTemplate<List<T>> ofUnsafePage(List<T> data, DoBaseQuery page) {
        DoTemplate<List<T>> of = ofUnsafeList(data);
        if(page != null){
            DoBaseQuery query = new DoBaseQuery();
            query.setPageNo(page.getPageNo());
            query.setPageSize(page.getPageSize());
            query.setTotal(page.getTotal());
            query.setCursor(page.getCursor());
            query.setSortMap(page.getSortMap());
            query.setSortList(page.getSortList());
            of.setPage(query);
        }
        return of;
    }

    /**
     * 单对象返回
     */
    public static <T> DoTemplate<T> ofUnsafe(T data) {
        DoTemplate<T> tDoTemplate = new DoTemplate<>();
        tDoTemplate.setSuccess(true);
        tDoTemplate.setMessage("操作成功");
        tDoTemplate.setData(data);
        return tDoTemplate;
    }
}
