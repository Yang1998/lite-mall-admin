package com.yyt.axios.config;

import com.yyt.axios.enums.CodeEnum;
import com.yyt.axios.vo.BaseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(TypeMismatchException.class)
    public BaseVO handleTypeMismatchException(TypeMismatchException e) {
       log.error(e.getErrorCode(), e.getMessage());
       return new BaseVO().setState(CodeEnum.REQUEST_PARAMS_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseVO handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return new BaseVO().setState(CodeEnum.REQUEST_METHOD_NOT_SUPPORTED_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public BaseVO handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return new BaseVO().setState(CodeEnum.REQUEST_PARAMS_VALIDATION_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public BaseVO<Object> handleUnauthorizedException(UnauthorizedException e) {
        log.error(e.getMessage(), e);
        return new BaseVO<>().setState(CodeEnum.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public BaseVO<Object> handleUnauthenticatedException(UnauthenticatedException e) {
        log.error(e.getMessage(), e);
        return new BaseVO<>().setState(CodeEnum.UNLOGIN);
    }
}
