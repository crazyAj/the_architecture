package cn.aj.commons.filter;

import cn.aj.commons.constants.Constant;
import cn.aj.commons.exception.DefBusinessException;
import cn.aj.commons.exception.DefExceptionEnum;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Slf4j
@Provider
public class ApplicationVersionFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String uri = containerRequestContext.getUriInfo().getPath();
//        log.info("--------------- uri = " + uri);
//        log.info("-------- Constant.APP_VERSION = " + Constant.APP_VERSION);
        String[] appUri = uri.split("/");
        if (appUri==null || !Constant.APP_VERSION.equals(appUri[0])) {
            throw new DefBusinessException(DefExceptionEnum.SYSTEM_VERSION_EXCEPTION);
        }
    }

}
