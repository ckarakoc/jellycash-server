package nl.ckarakoc.jellycash.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppConfig {

  @Bean
  @Primary
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean(name = "skipNullMapper")
  public ModelMapper modelMapperSkipNull() {
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setSkipNullEnabled(true);
    return mapper;
  }
}
