package pack;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 도메인이 다른 컴퓨터와 접속 가능
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("Get", "Post", "Put", "Delete")
                .allowedHeaders("*");
    }
}
