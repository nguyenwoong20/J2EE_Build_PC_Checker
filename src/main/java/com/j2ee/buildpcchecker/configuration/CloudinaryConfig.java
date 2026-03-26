package com.j2ee.buildpcchecker.configuration;

import com.cloudinary.Cloudinary;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CloudinaryConfig {

    @Value("${app.cloudinary.cloud-name}")
    String cloudName;

    @Value("${app.cloudinary.api-key}")
    String apiKey;

    @Value("${app.cloudinary.api-secret}")
    String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", "true");
        return new Cloudinary(config);
    }
}

