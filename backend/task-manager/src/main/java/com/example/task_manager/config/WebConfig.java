package com.example.task_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry corsRegistry) {
                corsRegistry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000") //frontend domain
                    .allowedMethods("GET", "PUT", "POST", "DELETE")
                    .allowedHeaders("*") //allows all headers
                    .maxAge(3600); //CORS response has 1 hour
            }
        };
    }
}
