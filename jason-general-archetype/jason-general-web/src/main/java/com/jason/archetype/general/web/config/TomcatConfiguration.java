package com.jason.archetype.general.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Jason
 */
@Configuration
public class TomcatConfiguration {
    @Autowired
    private MobileFilter mobileFilter;

    @Bean
    public TomcatServletWebServerFactory servletContainerFactory() {
        TomcatServletWebServerFactory servletContainerFactory = new TomcatServletWebServerFactory();
        return servletContainerFactory;
    }

    @Bean
    public FilterRegistrationBean mobileFilterRegist() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(mobileFilter);
        registrationBean.addUrlPatterns("/home/*");
        registrationBean.setOrder(0);
        return registrationBean;
    }
}
