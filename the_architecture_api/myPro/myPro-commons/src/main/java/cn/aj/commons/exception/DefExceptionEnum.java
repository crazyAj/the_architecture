package cn.aj.commons.exception;

import cn.aj.commons.resource.PropertyConfigurer;
import cn.aj.commons.utils.StringUtil;

/**
 * Created by itw_yangwj on 2018/6/27.
 */
public enum DefExceptionEnum {

    ////////////////// 系统 //////////////////
    SYSTEM_UNDEFINDE_EXCEPTION("ERR00000", PropertyConfigurer.getProperty("ERR00000"), "", PropertyConfigurer.getProperty("ERR00000_url")),
    SYSTEM_SERVICE_UNAVAILABLE("ERR00001", PropertyConfigurer.getProperty("ERR00001"), "", PropertyConfigurer.getProperty("ERR00001_url")),
    DATABASE_PERSISTENCE_EXCEPTION("ERR00002", PropertyConfigurer.getProperty("ERR00002"), "", PropertyConfigurer.getProperty("ERR00002_url")),
    SYSTEM_INVALID_PARAM("ERR00003", PropertyConfigurer.getProperty("ERR00003"), "", PropertyConfigurer.getProperty("ERR00003_url")),
    DATABASE_SQL_EXCEPTION("ERR00004", PropertyConfigurer.getProperty("ERR00004"), "", PropertyConfigurer.getProperty("ERR00004_url")),
    SYSTEM_INTERFACE_FAIL("ERR00005", PropertyConfigurer.getProperty("ERR00005"), "", PropertyConfigurer.getProperty("ERR00005_url")),
    CLIENT_CALL_EXCEPTION("ERR00006", PropertyConfigurer.getProperty("ERR00006"), "", PropertyConfigurer.getProperty("ERR00006_url")),
    SYSTEM_VERSION_EXCEPTION("ERR00007", PropertyConfigurer.getProperty("ERR00007"), "", PropertyConfigurer.getProperty("ERR00007_url")),
    ////////////////// 业务 //////////////////
    SUCCESS("BUS00000", PropertyConfigurer.getProperty("BUS00000"), "", PropertyConfigurer.getProperty("BUS00000_url"));

    private String code;
    private String msg;
    private Object data;
    private String referUrl;

    DefExceptionEnum(String code, String msg, Object data, String referUrl) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.referUrl = referUrl;
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

    public static DefExceptionEnum getByCode(String code) {
        for (DefExceptionEnum e : DefExceptionEnum.values()) {
            if (StringUtil.isEquals(e.code, code)) {
                return e;
            }
        }
        return null;
    }

}
