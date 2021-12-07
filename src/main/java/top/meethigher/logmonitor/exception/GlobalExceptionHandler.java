package top.meethigher.logmonitor.exception;

import top.meethigher.logmonitor.constant.ResponseEnum;
import top.meethigher.logmonitor.dto.BaseResponse;
import top.meethigher.logmonitor.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author chenchuancheng
 * @since 2021/12/8 0:32
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(value = Exception.class)
    public BaseResponse handleException(Exception e) {
        log.error("exception :{} ", e);
        e.printStackTrace();
        BaseResponse response = new BaseResponse();
        response.setCode(ResponseEnum.SERVICE_INNER_ERROR.code);
        response.setDesc(ResponseEnum.SERVICE_INNER_ERROR.desc);

        return response;
    }

    /**
     * 数据校验异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        log.warn("exception :{} ", e);
        BaseResponse response = new BaseResponse();
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        response.setCode(ResponseEnum.PARAMS_CHECK_ERR.code);
        response.setDesc(message);
        return response;
    }

    @ExceptionHandler(CommonException.class)
    public BaseResponse handleCommonException(CommonException e) {
        e.printStackTrace();
        log.error("CommonException :{} ", e.getResponseEnum().desc);
        return ResponseUtils.getResponseCode(e.getResponseEnum());
    }



    @ExceptionHandler(CustomRuntimeException.class)
    public BaseResponse handleCustomRuntimeException(CustomRuntimeException e) {
        e.printStackTrace();
        if (ObjectUtils.isEmpty(e.getDesc())) {
            log.error("CommonException :{} ", e.getResponseEnum().desc);
            return ResponseUtils.getResponseCode(e.getResponseEnum());
        } else {
            log.error("CommonException :{} ", e.getDesc());
            return ResponseUtils.getErrorResponse(e.getResponseEnum().code, e.getDesc());
        }
    }

    @ExceptionHandler(CustomDescException.class)
    public BaseResponse handleCustomDescException(CustomDescException e) {
        e.printStackTrace();
        log.error("CustomDescException :{} ", e.getDesc());
        return ResponseUtils.getErrorResponse(e.getResponseEnum().code, e.getDesc());
    }

}

