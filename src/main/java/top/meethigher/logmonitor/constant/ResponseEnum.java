package top.meethigher.logmonitor.constant;

/**
 * @author chenchuancheng
 * @since 2021/12/8 0:32
 */
public enum ResponseEnum {

    /**
     * 常规异常
     */
    SUCCESS(0, "成功"),
    FAILURE(10001, "失败"),
    PARAM_ERROR(10002, "参数错误"),
    PARAMS_CHECK_ERR(10003, "参数校验错误"),
    SERVICE_INNER_ERROR(10004, "服务内部错误"),

    /**
     * 自定义异常
     */
    DIR_NOT_EXIST_OR_DIR_IS_A_FILE(10005,"目录不存在或者目录是一个文件"),
    FILE_NOT_EXIST_OR_FILE_IS_DIRECTORY(10006,"文件不存在或者不是一个文件类型"),
    DOWNLOAD_FILE_FAILED(10007,"下载文件失败"),
    NO_ACCESS_FOR_THIS_PATH(10008,"无权访问该路径"),
    ;
    public final int code;
    public final String desc;

    ResponseEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
