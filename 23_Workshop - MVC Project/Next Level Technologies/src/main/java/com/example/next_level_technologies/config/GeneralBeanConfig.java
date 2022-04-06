package com.example.next_level_technologies.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralBeanConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
