package bg.softuni.computerStore.config;

import bg.softuni.computerStore.web.interceptors.StatsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  private final StatsInterceptor statsInterceptor;

  public WebConfig(StatsInterceptor statsInterceptor){
    this.statsInterceptor = statsInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(statsInterceptor);
  }
}

