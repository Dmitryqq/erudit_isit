package kg.erudit.api.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class AppMvcConfig implements WebMvcConfigurer {
    private final AppMvcInterceptor appMvcInterceptor;

    public AppMvcConfig(AppMvcInterceptor appMvcInterceptor) {
        this.appMvcInterceptor = appMvcInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(appMvcInterceptor);
    }
}
