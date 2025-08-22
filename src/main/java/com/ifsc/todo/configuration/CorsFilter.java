package com.ifsc.todo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Notation to inform which is a configuration.
@Configuration
public class CorsFilter{
    @Bean
    public WebMvcConfigurer corsConfig(){
        return new WebMvcConfigurer(){
        //Define which is overwrite a method.
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**")
                .allowedOrigins("https://hoppscotch.io")
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("*");
            }
        };
    }
}
