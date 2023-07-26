package ru.viz.clinic.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VizConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
