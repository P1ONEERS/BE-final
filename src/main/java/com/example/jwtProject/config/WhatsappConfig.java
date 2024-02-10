package com.example.jwtProject.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "whatsapp.api")

//kepake semua config
public class WhatsappConfig {

    private String url;
    private String token;
}
