package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * 自定义业务异常类
 */
@Slf4j
//@RestControllerAdvice
public class CustomException extends RuntimeException{

    public CustomException(String message){
        super(message);
    }

    /**
     * 异常处理方法
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Result<String> exceptionHandle(CustomException ex){
        log.error(ex.getMessage());

        return Result.error(ex.getMessage());
    }
}
