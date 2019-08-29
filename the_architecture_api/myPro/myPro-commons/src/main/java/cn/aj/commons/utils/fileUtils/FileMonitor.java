package cn.aj.commons.utils.fileUtils;

import java.io.File;
import java.util.*;

/**
 * Created by itw_yangwj on 2018/7/11.
 */
public class FileMonitor {

    private Timer timer = new Timer(true);
    private static Map<File, Long> fileCache = new HashMap<>();

    public FileMonitor() {
    }

    public FileMonitor(long pollingInterval) {
        timer.schedule(new FileMonitor.FileMonitorTimer(), 0L, pollingInterval);
    }

    /**
     * 在监听器里面添加文件
     */
    public void addFile(File file) {
        if (!fileCache.containsKey(file)) {
            Long modifiedTime = file.exists() ? new Long(file.lastModified()) : -1L;
            fileCache.put(file, modifiedTime);
        }
    }

    /**
     * 定义监听任务
     */
    private class FileMonitorTimer extends TimerTask {
        public FileMonitorTimer() {
        }

        public void run() {
            Iterator<File> it = fileCache.keySet().iterator();
            while (it.hasNext()) {
                File file = it.next();
                long lastModifiedTime = fileCache.get(file).longValue();
                long nowModifiedTime = file.exists() ? file.lastModified() : -1L;
                if (lastModifiedTime == nowModifiedTime) continue;
                fileCache.put(file, new Long(nowModifiedTime));
                FileFunc.cacheProps(file);
            }
        }

    }

}
