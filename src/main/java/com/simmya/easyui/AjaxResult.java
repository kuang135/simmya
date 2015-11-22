package com.simmya.easyui;

/*
 * 用于ajax提交后返回数据的封装
 */
public class AjaxResult {

    private Integer status;//状态码
    private String message;//文本信息
    private Object data;//具体数据
    
    public AjaxResult() {
        super();
    }

    public AjaxResult(Integer status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public AjaxResult(Integer status, String message, Object data) {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
    }
    
    public static AjaxResult ok(){
        return new AjaxResult(200,"ok",null);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
    
}
