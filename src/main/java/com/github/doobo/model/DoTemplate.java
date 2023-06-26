package com.github.doobo.model;

import java.io.Serializable;
import java.util.Objects;

public class DoTemplate<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer state = 1;

    private String message;

    private String code;

    private String subCode;

    private String subMsg;

    /**
     * 返回真实数据
     */
    private T data;

    /**
     * 分页参数,无分页时为空
     */
    private DoBaseQuery page;

    private String clueId;

    private transient Throwable throwable;

    public static DoTemplate<String> fail(String msg) {
        DoTemplate<String> response = new DoTemplate<>();
        response.setState(500);
        response.setMessage(msg);
        response.setSubMsg(msg);
        return response;
    }

    public static <K> DoTemplate<K> success(K data) {
        DoTemplate<K> response = new DoTemplate<>();
        response.setData(data);
        response.setMessage("操作成功");
        return response;
    }

    public static <K> DoTemplate<K> successMsg(String msg) {
        DoTemplate<K> response = new DoTemplate<>();
        response.setMessage(msg);
        return response;
    }

    /**
     * 执行状态
     */
    public boolean isSuccess(){
        return Objects.equals(state, 1);
    }

    /**
     * 设置执行状态
     */
    public void setSuccess(Boolean flag){
        this.state = Objects.equals(true, flag)? 1: 0;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getClueId() {
        return clueId;
    }

    public void setClueId(String clueId) {
        this.clueId = clueId;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

    public DoBaseQuery getPage() {
        return page;
    }

    public void setPage(DoBaseQuery page) {
        this.page = page;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public String toString() {
        return "DoTemplate{" +
                "state=" + state +
                ", message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", subCode='" + subCode + '\'' +
                ", subMsg='" + subMsg + '\'' +
                ", clueId='" + clueId + '\'' +
                '}';
    }
}
