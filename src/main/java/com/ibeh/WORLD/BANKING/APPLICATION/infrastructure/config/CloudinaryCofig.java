package com.ibeh.WORLD.BANKING.APPLICATION.infrastructure.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration

public class CloudinaryCofig {

    private final String CLOUD_NAME = "dbxunpwkw";

    private final String API_KEY = "214824388971267";

    private final String API_SECRET = "hapq939mri5pCbyE2MK9kQbhLsU";

    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();

        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);

        return new Cloudinary(config);
    }



}
