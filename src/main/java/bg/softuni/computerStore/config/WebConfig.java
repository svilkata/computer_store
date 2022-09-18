package bg.softuni.computerStore.config;

import bg.softuni.computerStore.web.interceptors.StatsInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final StatsInterceptor statsInterceptor;
    private LocaleChangeInterceptor localeChangeInterceptor;

    public WebConfig(StatsInterceptor statsInterceptor, LocaleChangeInterceptor localeChangeInterceptor) {
        this.statsInterceptor = statsInterceptor;
        this.localeChangeInterceptor = localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(statsInterceptor);
        registry.addInterceptor(localeChangeInterceptor);
    }

    //For displaying the html pages in a Heroku deployed project
    @Bean
    public ClassLoaderTemplateResolver myTemplateResolver(){
        ClassLoaderTemplateResolver configurer = new ClassLoaderTemplateResolver();
        configurer.setPrefix("templates/");
        configurer.setSuffix(".html");
        configurer.setTemplateMode(TemplateMode.HTML);
        configurer.setCharacterEncoding("UTF-8");
        configurer.setOrder(0);
        configurer.setCacheable(false);
        configurer.setCheckExistence(true);

        return  configurer;
    }
}

