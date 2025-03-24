package edu.prz.bomsystem.system.configuration;

import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateTimeConfiguration {

  @Value("${app.date.format:dd.MM.yyyy}")
  private String dateFormat;

  @Bean
  public DateTimeFormatter dateTimeFormatter() {
    return DateTimeFormatter.ofPattern(dateFormat);
  }
}