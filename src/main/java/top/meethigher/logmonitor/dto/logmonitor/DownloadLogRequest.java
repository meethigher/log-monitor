package top.meethigher.logmonitor.dto.logmonitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author chenchuancheng
 * @since 2021/12/6 10:41
 */
@ApiModel(value = "DownloadLogRequest")
public class DownloadLogRequest {

    /**
     * 下载路径
     */
    @ApiModelProperty(value = "logPath")
    @NotBlank
    private String logPath;

    /**
     * 下载后转成的自定义名字
     */
    @ApiModelProperty(value = "downloadName")
    @NotBlank
    private String downloadName;

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }
}
