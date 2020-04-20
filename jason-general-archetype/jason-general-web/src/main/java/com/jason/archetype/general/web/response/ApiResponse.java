package com.jason.archetype.general.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author jasonCQ
 * @version 1.0
 */

@ApiModel(description = "Api返回结果")
public class ApiResponse<T> {
    @ApiModelProperty("操作状态")
    private boolean state;
    @ApiModelProperty("数据")
    private T data;
    @ApiModelProperty("错误信息")
    private ErrorInfo error;

    public ApiResponse() {
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorInfo getError() {
        return this.error;
    }

    public void setError(ErrorInfo error) {
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> apiReponse = new ApiResponse();
        apiReponse.setState(true);
        apiReponse.setData(data);
        return apiReponse;
    }

    public static <T> ApiResponse<T> businessError(String errorMessage) {
        return error("602", errorMessage);
    }

    public static <T> ApiResponse<T> error(String errorCode, String errorMessage) {
        ApiResponse<T> apiReponse = new ApiResponse();
        apiReponse.setState(false);
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setCode(errorCode);
        errorInfo.setMessage(errorMessage);
        apiReponse.setError(errorInfo);
        return apiReponse;
    }
}

@ApiModel(
        description = "Api错误信息"
)
class ErrorInfo {
    @ApiModelProperty("错误代码")
    private String code;
    @ApiModelProperty("错误信息")
    private String message;

    public ErrorInfo() {
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
