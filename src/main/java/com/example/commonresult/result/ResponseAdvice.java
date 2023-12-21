package com.example.commonresult.result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 如果引入了swagger或knife4j的文档生成组件，这里需要仅扫描自己项目的包，否则文档无法正常生成
 *
 * @author lichengcan
 */
@RestControllerAdvice(basePackages = "com.example.commonresult")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseAdvice.class);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果不需要进行封装的，可以添加一些校验手段，比如添加标记排除的注解
        return true;
    }


    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 提供一定的灵活度，如果body已经被包装了，就不进行包装
        if (body instanceof Result) {
            return body;
        }
        return Result.success(body);
    }

    /**
     * 全局异常处理
     *
     * @param err 异常类
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(Exception err) {

        // 处理 @Validated 参数校验失败异常
        if (err instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) err;
            BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
            String defaultMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return Result.failed(ResultEnum.SYSTEM_EXCEPTION.getCode(), defaultMessage);
        }
        // Request Valid 异常处理
        if (err instanceof BindException) {
            String defaultMessage = ((BindException) err).getAllErrors().get(0).getDefaultMessage();
            return Result.failed(ResultEnum.SYSTEM_EXCEPTION.getCode(), defaultMessage);
        }
        if (err instanceof BeanInstantiationException) {
            Throwable cause = ((BeanInstantiationException) err).getCause();
            return Result.failed(ResultEnum.SYSTEM_EXCEPTION.getCode(), cause.getMessage());
        }
        err.printStackTrace();
        // 其他异常统一返回 UNKNOWN_EXCEPTION 错误码
        // 只有开发环境才会返回具体错误信息
        LOG.error(err.getMessage());
        if ("dev".equals("dev")) {
            return Result.failed(ResultEnum.SYSTEM_EXCEPTION.getCode(), err.getMessage());
        }
        return Result.failed(ResultEnum.SYSTEM_EXCEPTION.getCode(), null);
    }
}
