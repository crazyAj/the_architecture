package cn.aj.commons.utils.crawler.bean;

import cn.aj.commons.utils.StringUtil;
import cn.aj.commons.utils.crawler.util.CharsetDetector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by itw_yangwj on 2018/8/15.
 */
public class Page {
    private byte[] content;
    private String html;//网页源码字符串
    private Document doc;//网页Dom文档
    private String charset;//字符编码
    private String url;//url路径
    private String contentType;//内容类型

    public Page() {
    }

    public Page(byte[] content, String url, String contentType) {
        this.content = content;
        this.url = url;
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public String getCharset() {
        return charset;
    }

    public String getUrl() {
        return url;
    }

    public String getContentType() {
        return contentType;
    }

    /**
     * 获取网页源码字符串
     */
    public String getHtml() {
        if (StringUtil.isNotEmpty(html))
            return html;

        if (content == null)
            return null;

        if (StringUtil.isEmpty(charset))
            charset = CharsetDetector.guessEncoding(content);

        try {
            html = new String(content, charset);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * html字符串转文档
     */
    public Document getDoc(){
        if (doc != null) {
            return doc;
        }

        try {
            doc = Jsoup.parse(getHtml(), url);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
