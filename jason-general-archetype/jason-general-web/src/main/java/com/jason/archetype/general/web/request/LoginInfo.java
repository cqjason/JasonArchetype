package com.jason.archetype.general.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author jasonCQ
 * @version 1.0
 * @date 2020/4/20 10:30
 */
@ApiModel(description = "user info ")
public class LoginInfo {
    @ApiModelProperty("user name")
    @NotNull
    @NotBlank
    private String name;

    @ApiModelProperty("user password")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
