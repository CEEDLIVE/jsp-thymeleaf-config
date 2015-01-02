package be.c4j.springsquad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AppConfig.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AppConfig.class, args);
    }

}
