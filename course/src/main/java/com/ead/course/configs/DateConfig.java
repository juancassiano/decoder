package com.ead.course.configs;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

// @Configuration
public class DateConfig {
  public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  public LocalDateTimeSerializer LOCAL_DATE_TIME_SERIALIZER = 
    new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT));

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
      JavaTimeModule module = new JavaTimeModule();
      module.addSerializer(LOCAL_DATE_TIME_SERIALIZER);
      return new ObjectMapper().registerModule(module);
    }
}
