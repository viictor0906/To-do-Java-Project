package com.ifsc.todo.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsFilter{
    @Bean
    public WebMvcConfigurer corsConfig(){
        return new WebMvcConfigurer(){
            @SuppressWarnings("null")
            @Override
            public void addCorsMappings(CorsRegistry corsRegistry){
                corsRegistry.addMapping("/**")
                .allowedOrigins("https://hoppscotch.io")
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("*");
            }
        };
    }
}
