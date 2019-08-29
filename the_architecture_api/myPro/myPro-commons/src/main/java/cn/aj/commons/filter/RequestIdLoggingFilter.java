package cn.aj.commons.filter;

import cn.aj.commons.utils.Guid;
import cn.aj.commons.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 每一个请求增加requestId cookie来生成API的调用链，便于定位问题
 * 如果request包含requestId cookie，取该值设置MDC
 * 否则在response中增加requestId cookie，并设置设置MDC
 *
 * logback设置格式为
 * <pattern>%X{requestId} %X{remoteAddr} %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} -%msg%n</pattern>
 */
@Slf4j
public class RequestIdLoggingFilter implements Filter {

    private final String REQUEST_ID = "requestId";
    private final String REMOTE_ADDR = "remoteAddr";

    /**
     * Default constructor.
     */
    public RequestIdLoggingFilter() {
    }

    /**
     * init Filter
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }

    /**
     * destroy Filter
     */
    public void destroy() {
    }

    /**
     * doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest;
        HttpServletResponse httpResponse;
        Cookie[] cookies;
        boolean isExisting = false;
        String remoteHost;

        //判断当前cookie是否包含 REQUEST_ID，如果包含，则设置MDC
        if (request instanceof HttpServletRequest) {
            httpRequest = (HttpServletRequest) request;
            remoteHost = HttpUtil.getIpAddr(httpRequest);
            MDC.put(REMOTE_ADDR, remoteHost);

            cookies = httpRequest.getCookies();
            if (ArrayUtils.isNotEmpty(cookies)) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equalsIgnoreCase(REQUEST_ID)) {
                        isExisting = true;
                        MDC.put(REQUEST_ID, cookie.getValue());
                        break;
                    }
                }
            }
        }

        //当前request如果不包含REQUEST_ID，从response中设置
        if (response instanceof HttpServletResponse) {
            httpResponse = (HttpServletResponse) response;
            if (!isExisting) {
                final String requestId = Guid.CreateId();
                Cookie requestIdCookie = new Cookie(REQUEST_ID, requestId);
                httpResponse.addCookie(requestIdCookie);
                MDC.put(REQUEST_ID, requestId);
            }
        }

        //把REQUEST_ID从MDC中移除
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID);
            MDC.remove(REMOTE_ADDR);
        }
    }

}
