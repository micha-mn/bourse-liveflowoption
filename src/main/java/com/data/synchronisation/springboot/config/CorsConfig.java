package com.data.synchronisation.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/flow/**")
        		.allowedOrigins("https://localhost", "https://libvol.com", "https://www.libvol.com")
                .allowedMethods("POST", "GET", "PUT", "DELETE") // Specify the allowed HTTP methods
                .allowCredentials(true); // Allow sending of cookies
    }
}