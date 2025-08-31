package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName CorsConfig
 * @Author
 * @Version 1.0
 * @Description CorsConfig
 **/
@Configuration
public class CorsConfig implements WebMvcConfigurer {


    @Value("${website.domain}")
    private String domain;


    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins("http://localhost:63342") //SpringBoot2.4.0版本之前，如果是2.4.0以上版本官方推荐下面的方法
                .allowedOriginPatterns("*") //可以配置具体域名例如：httpL//www.baidu.com，可以配置*表示所有域名均可，
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(60 * 60);
    }

}
