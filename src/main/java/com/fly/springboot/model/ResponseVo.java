package com.fly.springboot.model;

import java.io.Serializable;

/**
 * @author fly
 * @date 2018/5/13 21:52
 * @description     返回对象
 **/
public class ResponseVo implements Serializable {

    private static final long serialVersionUID = -5109796051839269576L;
    /**
     * 操作结果标志
     */
    private boolean success;
    /**
     * 处理后的信息提示
     */
    private String message;

    /**
     * 返回后的数据
     */
    private Object body;

    public ResponseVo(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResponseVo(boolean success, String message, Object body) {
        this.success = success;
        this.message = message;
        this.body = body;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
