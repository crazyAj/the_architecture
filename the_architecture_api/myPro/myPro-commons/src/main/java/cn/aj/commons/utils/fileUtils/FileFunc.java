package cn.aj.commons.utils.fileUtils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by itw_yangwj on 2018/7/11.
 */
@Slf4j
public class FileFunc {

    private static Map<String, Properties> propsCache = new ConcurrentHashMap<>();
    private static FileMonitor fileMonitor;

    //初始化文件监听
    static {
        fileMonitor = new FileMonitor(1000L);
    }

    public FileFunc() {
    }

    /**
     * 动态加载属性文件
     */
    public static String getPropValue(String fileName, String key) {
        //读取文件
        File file = new File(fileName);
        //把文件加入监听器
        fileMonitor.addFile(file);
        //文件加入缓存
        if (!propsCache.containsKey(fileName)) {
            cacheProps(file);
        }
        Properties props = propsCache.get(fileName);
        return props == null ? "" : props.getProperty(key, "");
    }

    /**
     * 缓存配置文件
     */
    public static void cacheProps(File file) {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            props.load(in);
            propsCache.put(file.getPath(), props);
        } catch (Exception e) {
            System.out.println("----- FileFunc 读取配置文件异常 -----");
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                System.out.println("----- FileFunc 关闭流异常 -----");
            }
        }
    }

}
