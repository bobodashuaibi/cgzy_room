package com.cgzy.common.exception;



import com.cgzy.common.lang.result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
/**
 * 有时候不可避免服务器报错的情况，如果不配置异常处理机制，
 * 就会默认返回tomcat或者nginx的5XX页面，
 * 对普通用户来说，不太友好，用户也不懂什么情况。这
 * 时候需要我们程序员设计返回一个友好简单的格式给前端。
 * */

/**
 * 通过使用@ControllerAdvice来进行统一异常处理，
 * @ExceptionHandler(value = RuntimeException.class)来指定捕获的Exception各个类型异常 ，
 * 这个异常的处理，是全局的，所有类似的异常，都会跑到这个地方处理。
 * */

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice   //异步 使用@ControllerAdvice来进行统一异常处理
public class GlobalExceptionHandler {
    // 捕捉shiro的异常
    /**
     * ShiroException：
     * shiro抛出的异常，比如没有权限，用户登录异常
     * UNAUTHORIZED(401, "Unauthorized"),401 没有权限
     * */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public result handle401(ShiroException e) {
        return result.Loginfail(e.getMessage());
    }

    /**
     * 处理Assert的异常
     * IllegalArgumentException：处理Assert的异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public result handler(IllegalArgumentException e) throws IOException {
        log.error("Assert断言异常:-------------->{}",e.getMessage());
        return result.Loginfail(e.getMessage());
    }
    /**
     * @Validated 校验错误异常处理
     * MethodArgumentNotValidException：处理实体校验的异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public result handler(MethodArgumentNotValidException e) throws IOException {
        log.error("运行时异常:-------------->",e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return result.Loginfail(objectError.getDefaultMessage());
    }

    /**
     * RuntimeException：捕捉其他异常
     * */
    @ResponseStatus(HttpStatus.BAD_REQUEST)		//给前端返回一个状态码
    @ExceptionHandler(value = ExpiredCredentialsException.class)
    public result handler(ExpiredCredentialsException e) throws IOException {
        return result.Loginfail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)		//给前端返回一个状态码
    @ExceptionHandler(value = RuntimeException.class)
    public result handler(RuntimeException e) throws IOException {
        log.error("运行时异常:-------------->",e);
        return result.Loginfail(e.getMessage());
    }
}
