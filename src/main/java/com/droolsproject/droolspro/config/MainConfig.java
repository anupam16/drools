package com.droolsproject.droolspro.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class MainConfig {

    @Bean
    public RestTemplate modelMapper(){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
    
    @Bean
    public BeanDefinitionRegistry beanDefinitionRegistry() {
        return new DefaultListableBeanFactory();
    }
}