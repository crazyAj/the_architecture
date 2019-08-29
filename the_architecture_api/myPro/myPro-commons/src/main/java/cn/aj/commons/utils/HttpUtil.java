package cn.aj.commons.utils;

import cn.aj.commons.utils.httpclient.HttpClientUtil;
import cn.aj.commons.utils.httpclient.builder.HCB;
import cn.aj.commons.utils.httpclient.common.HttpConfig;
import cn.aj.commons.utils.httpclient.common.HttpHeader;
import cn.aj.commons.utils.httpclient.common.SSLs;
import cn.aj.commons.utils.httpclient.exception.HttpProcessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static String getIpAddr(HttpServletRequest request) {
        //X-Forwarded-For：Squid 服务代理
        String ip = request.getHeader("x-forwarded-for");
        //Proxy-Client-IP：apache 服务代理
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        //WL-Proxy-Client-IP：weblogic 服务代理
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        //HTTP_CLIENT_IP：大型网络服务器代理
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        //HTTP_X_FORWARDED_FOR：大型网络服务器代理
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {//本机调用
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (inet != null) {
                    ip = inet.getHostAddress();
                }
            }
        }

        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    /**
     * http发送json格式的字符串
     */
    public static String sendJsonHttp(String json, String url, String encoding, int timeout) {
        log.info("------------ HttpJSON request -------------- " + json);
        String responseStr = null;
        try {
            Header[] header = HttpHeader.custom().other("Content-type", "application/json;charset=" + encoding + "").build();
            HttpClient client = HCB.custom().timeout(timeout).build();
            HttpConfig config = HttpConfig.custom();
            config.headers(header);
            config.json(json);
            config.encoding(encoding);
            config.url(url);
            config.client(client);
            responseStr = HttpClientUtil.post(config);
            log.info("------------ HttpJSON response -------------- " + responseStr);
        } catch (HttpProcessException e) {
            log.error("------------ Http Exception --------------{}", e);
        }
        return responseStr;
    }

    /**
     * https发送json格式的字符串
     */
    public static String sendJsonHttps(String json, String url, String encoding, int timeout) {
        log.info("------------ HttpJSON request -------------- " + json);
        String responseStr = null;
        try {
            Header[] header = HttpHeader.custom().other("Content-type", "application/json;charset=" + encoding + "").build();
            HttpClient client = HCB.custom().sslpv(SSLs.SSLProtocolVersion.TLSv1).timeout(timeout).build();
            HttpConfig config = HttpConfig.custom();
            config.headers(header);
            config.json(json);
            config.encoding(encoding);
            config.url(url);
            config.client(client);
            responseStr = HttpClientUtil.post(config);
            log.info("------------ HttpJSON response -------------- " + responseStr);
        } catch (HttpProcessException e) {
            log.error("------------ Https Exception --------------{}", e);
        }
        return responseStr;
    }

    /**
     * http发送xml格式的字符串
     */
    public static String sendXmlHttp(String xml, String url, String encoding, int timeout) {
        log.info("------------ HttpXML request -------------- " + xml);
        String responseStr = null;
        try {
            Header[] header = HttpHeader.custom().other("Content-type", "text/xml;charset=" + encoding + "").build();
            HttpClient client = HCB.custom().timeout(timeout).build();
            HttpConfig config = HttpConfig.custom();
            config.headers(header);
            config.xml(xml);
            config.encoding(encoding);
            config.url(url);
            config.client(client);
            responseStr = HttpClientUtil.post(config);
            log.info("------------ HttpXML response -------------- " + responseStr);
        } catch (HttpProcessException e) {
            log.error("------------ Http Exception --------------{}", e);
        }
        return responseStr;
    }

    /**
     * https发送xml格式的字符串
     */
    public static String sendXmlHttps(String xml, String url, String encoding, int timeout) {
        log.info("------------ HttpXML request -------------- " + xml);
        String responseStr = null;
        try {
            Header[] header = HttpHeader.custom().other("Content-type", "text/xml;charset=" + encoding + "").build();
            HttpClient client = HCB.custom().sslpv(SSLs.SSLProtocolVersion.TLSv1).timeout(timeout).build();
            HttpConfig config = HttpConfig.custom();
            config.headers(header);
            config.xml(xml);
            config.encoding(encoding);
            config.url(url);
            config.client(client);
            responseStr = HttpClientUtil.post(config);
            log.info("------------ HttpXML response -------------- " + responseStr);
        } catch (HttpProcessException e) {
            log.error("------------ Https Exception --------------{}", e);
        }
        return responseStr;
    }

}