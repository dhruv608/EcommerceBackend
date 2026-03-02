package com.store.backend.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dgovkmpvf");
        config.put("api_key", "453741849375212");
        config.put("api_secret", "1yD1cgnZ5ULFiK1cWaz2vPXMTjw");

        return new Cloudinary(config);
    }
}
