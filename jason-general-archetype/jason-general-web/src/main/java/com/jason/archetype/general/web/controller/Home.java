package com.jason.archetype.general.web.controller;

import com.jason.archetype.general.web.exception.BusinessException;
import com.jason.archetype.general.web.request.LoginInfo;
import com.jason.archetype.general.web.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Jason
 * @version 1.0
 */
@Api(value = "Home",tags = "Home")
@RestController
@RequestMapping(path = "/home")
public class Home {
    @ApiOperation(value = "welcome", notes = "welcome")
    @RequestMapping(path = "welcome", method = RequestMethod.GET)
    public ApiResponse<String> welcome() {
        return ApiResponse.success("welcome");
    }

    @ApiOperation(value = "login", notes = "login")
    @RequestMapping(path = "login", method = RequestMethod.GET)
    public ApiResponse<String> login(@Valid LoginInfo loginInfo) {
        return ApiResponse.success("login");
    }

    @ApiOperation(value = "exception", notes = "exception")
    @RequestMapping(path = "exception", method = RequestMethod.GET)
    public ApiResponse<String> exception() {
        throw new BusinessException("1","error");
    }
}
