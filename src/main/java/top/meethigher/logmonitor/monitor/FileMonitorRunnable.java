package top.meethigher.logmonitor.monitor;

import top.meethigher.logmonitor.utils.WebSocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author chenchuancheng
 * @since 2021/12/6 13:47
 */
public class FileMonitorRunnable implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FileMonitorRunnable.class);

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 100);

    private CharBuffer charBuffer = CharBuffer.allocate(1024 * 50);

    private CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();

    private boolean isRunning = true;

    private String sessionId;

    private String logPath;

    private Long monitorDelay;

    public FileMonitorRunnable(String sessionId, String logPath, Long monitorDelay) {
        this.sessionId = sessionId;
        this.logPath = logPath;
        this.monitorDelay = monitorDelay;
    }

    @Override
    public void run() {
        File file = new File(logPath);
        FileChannel channel = null;
        try {
            channel = new FileInputStream(file).getChannel();
            channel.position(channel.size());
        } catch (Exception e) {
            log.info("监控文件失败，检查路径是否正确");
            WebSocketUtils.endMonitor(sessionId);
            e.printStackTrace();
        }
        long lastModified = file.lastModified();
        //TODO: 初次连接将所有内容丢回去？这个考虑到数据如果很多先不丢
        while (isRunning) {
            long now = file.lastModified();
//            log.info("{}的连接正在通过线程{}监控{}文件",sessionId,Thread.currentThread().getName(),logPath);
            if (now != lastModified) {
                log.info("{}的连接正在通过线程{}监控{}的文件update", sessionId, Thread.currentThread().getName(), logPath);
                String newContent = getNewContent(channel);
                WebSocketUtils.sendMessageTo(sessionId, newContent);
                lastModified = now;
            }
            try {
                Thread.sleep(monitorDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
                WebSocketUtils.endMonitor(sessionId);
            }
            isRunning = WebSocketUtils.currentSessionAlive(sessionId);

        }
    }

    private String getNewContent(FileChannel channel) {
        try {
            byteBuffer.clear();
            charBuffer.clear();
            int length = channel.read(byteBuffer);
            if (length != -1) {
                byteBuffer.flip();
                decoder.decode(byteBuffer, charBuffer, true);
                charBuffer.flip();
                return charBuffer.toString();
            } else {
                channel.position(channel.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebSocketUtils.endMonitor(sessionId);
        }
        return null;
    }
}
