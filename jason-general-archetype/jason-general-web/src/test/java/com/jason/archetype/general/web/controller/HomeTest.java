package com.jason.archetype.general.web.controller;

import com.jason.archetype.general.web.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * @author jasonCQ
 * @version 1.0
 * @date 2020/4/20 14:03
 */
@SpringBootTest(classes = Home.class)
public class HomeTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private Home home;

    private MockMvc mockMvc;

    @BeforeClass
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(home).build();
    }

    @Test
    void welcome() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/home/welcome");
        MockHttpServletResponse response =  mockMvc.perform(builder).andReturn().getResponse();
        Assert.assertEquals(response.getStatus(), 200);
    }


    @Test(dataProvider = "login")
    public void testLogin(MultiValueMap<String, String> params,int states) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/home/login").params(params);
        MockHttpServletResponse response =  mockMvc.perform(builder).andReturn().getResponse();
        Assert.assertEquals(response.getStatus(), states);
    }
    @DataProvider(name = "login")
    public Object[][] generateLogin(){
        Map<String, List<String>> paramMap = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("js");
        paramMap.put("name", list);
        return new Object[][]{
                {CollectionUtils.toMultiValueMap(new HashMap<>()),400},
                {CollectionUtils.toMultiValueMap(paramMap),200}
        };
    }

    @Test
    public void testException() {
        try{
            MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/home/exception");
            MockHttpServletResponse response =  mockMvc.perform(builder).andReturn().getResponse();
            Assert.fail();
        }catch (Exception ex){
            Assert.assertEquals(ex.getCause().getClass(), BusinessException.class);
        }
    }
}