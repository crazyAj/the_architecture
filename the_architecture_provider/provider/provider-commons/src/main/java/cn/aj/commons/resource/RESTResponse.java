package cn.aj.commons.resource;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by itw_yangwj on 2018/6/27.
 */
public class RESTResponse implements Serializable {

    private String code;
    private String msg;
    private Object data;
    private String referUrl;

    public RESTResponse() {
    }

    public RESTResponse(String code, String msg, Object data, String referUrl) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.referUrl = referUrl;
    }

    public RESTResponse(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public RESTResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String toJSONString() {
        JSONObject jObj = new JSONObject();
        jObj.put("code", code);
        jObj.put("msg", msg);
        jObj.put("data", data);
        jObj.put("referUrl", referUrl);
        return jObj.toJSONString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getReferUrl() {
        return referUrl;
    }

    public void setReferUrl(String referUrl) {
        this.referUrl = referUrl;
    }
}
