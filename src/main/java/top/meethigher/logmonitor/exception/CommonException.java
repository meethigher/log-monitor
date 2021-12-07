package top.meethigher.logmonitor.exception;


import top.meethigher.logmonitor.constant.ResponseEnum;

/**
 * @author chenchuancheng
 * @since 2021/12/8 0:32
 */
public class CommonException extends Exception {
    private ResponseEnum responseEnum;

    public CommonException(ResponseEnum responseEnum) {
        this.responseEnum=responseEnum;
    }

    public CommonException(String message) {
        super(message);
    }

    public ResponseEnum getResponseEnum() {
        return responseEnum;
    }

    public void setResponseEnum(ResponseEnum responseEnum) {
        this.responseEnum = responseEnum;
    }
}
