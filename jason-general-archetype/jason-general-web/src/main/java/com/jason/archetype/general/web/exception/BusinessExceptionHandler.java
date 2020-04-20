package com.jason.archetype.general.web.exception;

import com.jason.archetype.general.web.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

/**
 * @author jasonCQ
 * @version 1.0
 */
@ControllerAdvice
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {
    static Logger logger = LoggerFactory.getLogger(BusinessExceptionHandler.class);

    public BusinessExceptionHandler() {
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex) {
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException)ex;
            ApiResponse apiResponse = !StringUtils.isEmpty(businessException.getCode()) ? ApiResponse.error(businessException.getCode() + "", businessException.getMessage()) : ApiResponse.businessError(businessException.getMessage());
            return new ResponseEntity(apiResponse, HttpStatus.OK);
        } else {
            logger.error("Controller Exception: ", ex);
            ApiResponse apiResponse = ApiResponse.error("500", "Internal Server Error");
            return new ResponseEntity(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ObjectError> ors = ex.getBindingResult().getAllErrors();
        ApiResponse apiResponseBody = ApiResponse.error("400", validErrorStr(ors));
        return new ResponseEntity(apiResponseBody, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ObjectError> ors = ex.getBindingResult().getAllErrors();
        ApiResponse apiResponseBody = ApiResponse.error("400", validErrorStr(ors));
        return new ResponseEntity(apiResponseBody, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponse apiResponseBody = ApiResponse.error("400", ex.getMessage());
        return new ResponseEntity(apiResponseBody, headers, status);
    }

    private String validErrorStr(List<ObjectError> ors) {
        StringBuilder sb = new StringBuilder("Params valid failed：");
        Iterator<ObjectError> it = ors.iterator();
        sb.append('[');
        for (; ; ) {
            ObjectError or = it.next();
            if (or instanceof FieldError) {
                sb.append(((FieldError) or).getField() + ":" + ((FieldError) or).getDefaultMessage());
            } else {
                sb.append(or.getDefaultMessage());
            }
            if (!it.hasNext()) {
                sb.append("]");
                break;
            } else {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
