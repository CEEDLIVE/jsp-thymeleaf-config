package be.c4j.springsquad;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@SpringBootApplication
public class AppConfig {

    @Value("${spring.view.prefix}")
    private String prefix = "";

    @Value("${spring.view.suffix}")
    private String suffix = "";

    @Value("${spring.view.view-names}")
    private String viewNames = "";

    @Bean
    InternalResourceViewResolver jspViewResolver() {
        final InternalResourceViewResolver vr = new InternalResourceViewResolver();
        vr.setPrefix(prefix);
        vr.setSuffix(suffix);
        vr.setViewClass(JstlView.class);
        vr.setViewNames(viewNames);
        return vr;
    }
}
