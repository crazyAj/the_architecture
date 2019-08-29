package cn.aj.commons.exception;

/**
 * Created by itw_yangwj on 2018/6/28.
 */
public class DefBusinessException extends DefException {

    public DefBusinessException(){}

    public DefBusinessException(DefExceptionEnum defException) {
        super(defException);
    }

    public DefBusinessException(DefExceptionEnum defException, Throwable cause) {
        super(defException, cause);
    }

}
