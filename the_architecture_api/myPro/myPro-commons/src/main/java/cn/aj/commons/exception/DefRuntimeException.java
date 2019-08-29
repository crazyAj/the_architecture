package cn.aj.commons.exception;

/**
 * Created by itw_yangwj on 2018/6/28.
 */
public class DefRuntimeException extends DefException{

    public DefRuntimeException(){}

    public DefRuntimeException(DefExceptionEnum defException) {
        super(defException);
    }

    public DefRuntimeException(DefExceptionEnum defException, Throwable cause) {
        super(defException, cause);
    }

}
