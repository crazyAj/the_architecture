package cn.aj.commons.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by itw_yangwj on 2018/6/27.
 * <p>
 * 异常原则：便于运维人员统计系统服务或者接口调用异常，减少不必要异常抛出
 * 1.运行时异常，比如调用支付通道、数据库访问失败、缓存访问失败要记录并且抛出
 * 2.业务异常，验证签名失败只需要记录，不需要抛出
 * <p>
 * 异常体系：
 * DefException
 * DefRuntimeException	    记录并且抛出
 * DefBusinessException    记录，但不抛出，错误信息返回前端
 */
@Slf4j
public class DefException extends RuntimeException {

    private DefExceptionEnum defException;

    public DefException() {
        super();
    }

    public DefException(DefExceptionEnum defException) {
        super(defException.getMsg());
        this.defException = defException;
    }

    public DefException(DefExceptionEnum defException, Throwable cause) {
        super(defException.getMsg(), cause);
        this.defException = defException;
    }

    @Override
    public String getMessage() {
        return this.getMsg();
    }

    @Override
    public String toString() {
        return this.getMsg();
    }

    /**
     * 获取最终错误编码
     */
    public String getCode() {
        return this.defException != null ? this.defException.getCode() : null;
    }

    /**
     * 获取最终错误编码
     */
    public String getMsg() {
        return this.defException != null ? this.defException.getMsg() : super.getMessage();
    }

    /**
     * 获取最终链接
     */
    public String getReferUrl() {
        return this.defException != null ? defException.getReferUrl() : null;
    }

}
