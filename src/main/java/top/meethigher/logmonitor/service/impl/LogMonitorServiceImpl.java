package top.meethigher.logmonitor.service.impl;

import top.meethigher.logmonitor.constant.ResponseEnum;
import top.meethigher.logmonitor.dto.logmonitor.DownloadLogRequest;
import top.meethigher.logmonitor.dto.logmonitor.QueryLogRequest;
import top.meethigher.logmonitor.exception.CommonException;
import top.meethigher.logmonitor.service.LogMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author chenchuancheng
 * @since 2021/12/6 9:38
 */
@Service
public class LogMonitorServiceImpl implements LogMonitorService {

    private final Logger log = LoggerFactory.getLogger(LogMonitorServiceImpl.class);

    /**
     * 日志根目录、默认目录
     */
    @Value("${log.monitor.defaultPath}")
    private String logRootPath;

    /**
     * 获取路径
     *
     * @param logPath
     * @return
     */
    private String getLogPath(String logPath) throws CommonException {
        String path = null;
        if (ObjectUtils.isEmpty(logPath)) {
            path = logRootPath;
        } else {
            if (!logPath.contains(logRootPath)) {
                throw new CommonException(ResponseEnum.NO_ACCESS_FOR_THIS_PATH);
            }
            path = logPath;
        }
        return path;
    }

    /**
     * 按照时间查询日志
     *
     * @param startTime
     * @param endTime
     * @param dir
     * @return
     */
    private List<String> queryLogByTime(Long startTime, Long endTime, File dir) {
        List<String> files = new LinkedList<>();
        for (String s : Objects.requireNonNull(dir.list())) {
            File file = new File(dir, s);
            long lastModified = file.lastModified();
            if (startTime <= lastModified && endTime >= lastModified) {
                files.add(file.getAbsolutePath().replaceAll("\\\\", "/"));
            }
        }
        log.info("queryLogByTime");
        return files;
    }

    /**
     * 查询所有日志
     *
     * @param dir
     * @return
     */
    private List<String> queryLogWithoutTime(File dir) {
        List<String> files = new LinkedList<>();
        for (String s : Objects.requireNonNull(dir.list())) {
            File file = new File(dir, s);
            files.add(file.getAbsolutePath().replaceAll("\\\\", "/"));
        }
        log.info("queryLogWithoutTime");
        return files;
    }


    @Override
    public List<String> queryLog(QueryLogRequest request) throws CommonException {
        String logPath = getLogPath(request.getLogPath());
        File dir = new File(logPath);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new CommonException(ResponseEnum.DIR_NOT_EXIST_OR_DIR_IS_A_FILE);
        }
        if (!ObjectUtils.isEmpty(request.getStartTime()) && !ObjectUtils.isEmpty(request.getEndTime())) {
            return queryLogByTime(request.getStartTime(), request.getEndTime(), dir);
        } else {
            return queryLogWithoutTime(dir);
        }
    }

    @Override
    public String downloadLog(DownloadLogRequest request, HttpServletResponse response) throws CommonException {
        String logPath = getLogPath(request.getLogPath());
        File file = new File(logPath);
        if (!file.exists() || !file.isFile()) {
            throw new CommonException(ResponseEnum.FILE_NOT_EXIST_OR_FILE_IS_DIRECTORY);
        }
        // 实现文件下载
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(request.getDownloadName(), "UTF-8"));
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            log.info("下载文件成功!");
        } catch (Exception e) {
            log.info("下载文件失败!");
            throw new CommonException(ResponseEnum.DOWNLOAD_FILE_FAILED);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
