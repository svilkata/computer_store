package bg.softuni.computerStore.config;

import bg.softuni.computerStore.web.interceptors.StatsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  private final StatsInterceptor statsInterceptor;
  private LocaleChangeInterceptor localeChangeInterceptor;

  public WebConfig(StatsInterceptor statsInterceptor, LocaleChangeInterceptor localeChangeInterceptor){
    this.statsInterceptor = statsInterceptor;
    this.localeChangeInterceptor = localeChangeInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(statsInterceptor);
    registry.addInterceptor(localeChangeInterceptor);
  }
}

