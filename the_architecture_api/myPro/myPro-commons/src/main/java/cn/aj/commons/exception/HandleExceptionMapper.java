package cn.aj.commons.exception;

import cn.aj.commons.utils.StringUtil;
import cn.aj.commons.resource.RESTResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by itw_yangwj on 2018/6/27.
 */
@Slf4j
@Provider
public class HandleExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        String code;
        String msg;
        String referUrl;
        if (e instanceof DefBusinessException) {//业务异常
            DefBusinessException defBusinessException = (DefBusinessException) e;
            code = defBusinessException.getCode();
            msg = defBusinessException.getMsg();
            referUrl = defBusinessException.getReferUrl();
            //按BusinessExceptionCode区分异常类型 TODO
        } else if (e instanceof DefRuntimeException) {//系统异常
            DefRuntimeException defRuntimeException = (DefRuntimeException) e;
            code = defRuntimeException.getCode();
            msg = defRuntimeException.getMsg();
            referUrl = defRuntimeException.getReferUrl();
        } else if (e instanceof ClientErrorException) {//客户端异常
            code = DefExceptionEnum.CLIENT_CALL_EXCEPTION.getCode();
            msg = DefExceptionEnum.CLIENT_CALL_EXCEPTION.getMsg();
            referUrl = DefExceptionEnum.CLIENT_CALL_EXCEPTION.getReferUrl();
        } else if (e instanceof IllegalArgumentException) {//非法参数异常
            code = DefExceptionEnum.SYSTEM_INVALID_PARAM.getCode();
            msg = DefExceptionEnum.SYSTEM_INVALID_PARAM.getMsg();
            referUrl = DefExceptionEnum.SYSTEM_INVALID_PARAM.getReferUrl();
        } else if (e instanceof SQLException) {//sql异常
            code = DefExceptionEnum.DATABASE_SQL_EXCEPTION.getCode();
            msg = DefExceptionEnum.DATABASE_SQL_EXCEPTION.getMsg();
            referUrl = DefExceptionEnum.DATABASE_SQL_EXCEPTION.getReferUrl();
        } else if (e instanceof PersistenceException) {//持久化异常
            code = DefExceptionEnum.DATABASE_PERSISTENCE_EXCEPTION.getCode();
            msg = DefExceptionEnum.DATABASE_PERSISTENCE_EXCEPTION.getMsg();
            referUrl = DefExceptionEnum.DATABASE_PERSISTENCE_EXCEPTION.getReferUrl();
        } else {//未定义的异常
            code = DefExceptionEnum.SYSTEM_UNDEFINDE_EXCEPTION.getCode();
            msg = DefExceptionEnum.SYSTEM_UNDEFINDE_EXCEPTION.getMsg();
            referUrl = DefExceptionEnum.SYSTEM_UNDEFINDE_EXCEPTION.getReferUrl();
        }
        log.error("---------------- Exception errorCode:{},errorMessage:{} ---------------- ", code, msg);
        log.error(code, e);//测试环境为减少日志刷新速度，暂时注释掉

        //构建响应对象
        RESTResponse response = new RESTResponse();
        response.setCode(code);
        response.setMsg("网络繁忙，请稍后再试");//返回前端易读错误提示
        response.setReferUrl(referUrl);

        if (StringUtil.isEmpty(referUrl)) {//响应前端数据
            return Response.status(Response.Status.OK).entity(response.toJSONString()).build();
        } else {//跳转页面
            try {
                return Response.status(Response.Status.FOUND).location(new URI(referUrl)).build();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
            return null;
        }

    }

}
