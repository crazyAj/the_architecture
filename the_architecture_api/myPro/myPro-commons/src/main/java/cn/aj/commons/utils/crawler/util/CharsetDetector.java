package cn.aj.commons.utils.crawler.util;

import cn.aj.commons.utils.StringUtil;
import org.mozilla.universalchardet.UniversalDetector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by itw_yangwj on 2018/7/4.
 */
public class CharsetDetector {

    /**
     * <meta http-equiv="content-type" content="text/html;charset=utf-8">
     * <meta charset="utf-8"/>
     */
    private static final Pattern META_PATTERN = Pattern.compile("<meta\\s+([^>]*http-equiv=(\"|')?content-type(\"|')?[^>]*)>",Pattern.CASE_INSENSITIVE);
    private static final Pattern CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*([a-z][_\\-0-9a-z]*)",Pattern.CASE_INSENSITIVE);
    private static final Pattern HTML5_CHARSET_PATTERN = Pattern.compile("<meta\\s+charset\\s*=\\s*[\"|']?([a-z][_\\-0-9a-z]*)[^>]*>",Pattern.CASE_INSENSITIVE);

    public static String guessEncoding(byte[] bytes) {
        String encoding = null;
        try {
            encoding = getEncodingByNutch(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtil.isEmpty(encoding)) {
            encoding = getEncodingByChardet(bytes);
        }
        return encoding;
    }

    /**
     * 判断html meta，文件前几个字节
     */
    public static String getEncodingByNutch(byte[] content){
        try {
            String str = new String(content, "ascii");
            String encoding = null;

            //非HTML5
            Matcher metaMatcher = META_PATTERN.matcher(str);
            if(metaMatcher.find()) {
                Matcher charsetMatcher = CHARSET_PATTERN.matcher(metaMatcher.group(1));
                if(charsetMatcher.find()) {
                    encoding = new String(charsetMatcher.group(1));
                }
            }

            //HTML5
            if(encoding == null){
                Matcher html5Matcher = HTML5_CHARSET_PATTERN.matcher(str);
                if (html5Matcher.find()) {
                    encoding = new String(html5Matcher.group(1));
                }
            }

            int length = Math.min(content.length, 2000);
            if (encoding == null) {
                //根据文件内容的头几个字节得到文件的编码
                if (length >= 3 && content[0] == (byte) 0xEF && content[1] == (byte) 0xBB && content[2] == (byte) 0xBF) {
                    encoding = "UTF-8";
                } else if (length >= 2) {
                    if (content[0] == (byte) 0xFF && content[1] == (byte) 0xFE) {
                        encoding = "UTF-16LE";
                    } else if (content[0] == (byte) 0xFE && content[1] == (byte) 0xFF) {
                        encoding = "UTF-16BE";
                    }
                }
            }
            return encoding;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据内容判断编码
     */
    public static String getEncodingByChardet(byte[] bytes){
        String encoding;
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes,0,bytes.length);
        detector.dataEnd();
        encoding = detector.getDetectedCharset();
        detector.reset();
        if(StringUtil.isEmpty(encoding))
            encoding = "UTF-8";
        return encoding;
    }

}