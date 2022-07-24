package com.limu.yygh.common.handler;

import com.limu.yygh.common.exception.YyghException;
import com.limu.yygh.common.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R error(Exception e) {
        log.error(e.getMessage());
        return R.error();
    }

    @ExceptionHandler(ArithmeticException.class)
    public R error(ArithmeticException e){
        log.error(e.getMessage());
        return R.error().message("执行了特定异常");
    }

    @ExceptionHandler(YyghException.class)
    public R error(YyghException e) {
        log.error(e.getMessage());
        return R.error().code(e.getCode()).message(e.getMessage());
    }
}
