package cn.aj.commons.resource;

import cn.aj.commons.exception.HandleExceptionMapper;
import cn.aj.commons.filter.ApplicationVersionFilter;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by itw_yangwj on 2018/6/26.
 */
public class RESTApplication extends ResourceConfig {

    public RESTApplication(){
        //服务类包路径
        packages("cn.aj.api");
        //注册JSON解析器
        register(JacksonJsonProvider.class);
        //托管appVersion过滤器
        register(ApplicationVersionFilter.class);
        //注册异常类
        register(HandleExceptionMapper.class);
    }

}
