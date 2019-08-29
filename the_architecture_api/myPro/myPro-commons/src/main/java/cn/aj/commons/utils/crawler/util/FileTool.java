package cn.aj.commons.utils.crawler.util;

import cn.aj.commons.utils.crawler.bean.Page;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by itw_yangwj on 2018/7/4.
 */
@Slf4j
public class FileTool {

    private static String dirPath;

    /**
     * 创建目录
     */
    private static void mkdir() {
        File[] roots = File.listRoots();
        String path = Class.class.getClass().getResource("/").getPath();
        long maxFreeSpace = 0l;
        for (File file : roots) {
            if (file.getFreeSpace() > maxFreeSpace) {
                maxFreeSpace = file.getFreeSpace();
                path = file.getAbsolutePath();
            }
        }
        dirPath = path + "temp/";
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
    }

    /**
     * 根据url和网页类型生成需要保存的网页文件名，去除url中非文件名字符
     */
    private static String getFileNameByUrl(String url, String contentType) {
        Pattern pattern = Pattern.compile("http(s)?://(.*)");
        Matcher matcher = pattern.matcher(url);
        matcher.find();
        url = matcher.group(2);
        System.out.println("name = " + url);
        if (contentType.indexOf("html") != -1) {
            return url.replaceAll("[\\?/:*|<>\"]]", "_") + ".html";
        } else {
            return url.replaceAll("[\\?/:*|<>\"]]", "_") + "." + contentType.substring(contentType.lastIndexOf("/") + 1);
        }
    }

    /**
     * 保存网页字节数组到本地
     */
    private static void saveContent(Page page) {
        mkdir();
        String fileName = getFileNameByUrl(page.getUrl(), page.getContentType());
        String filePath = dirPath + fileName;
        byte[] content = page.getContent();
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(new File(filePath)));
            for (int i = 0; i < content.length; i++) {
                out.write(content[i]);
            }
            out.flush();
            log.info("--- 文件：" + fileName + " 已经被存储在：" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}