# From JSP to Thymeleaf #

Template engines are getting more and more popular over the usage of JSP's. For Spring applications, mainly Thymeleaf is getting a lot of attention. In a lot of samples that are given by Spring, Thymeleaf is used as template engine.

JSP's have proven their strength in the past, but it also has weaknesses. The good thing about JSP's is that it is a standard and you can do almost anything to achieve your goal. We are of course talking about using the expression language and not scriptlets (avoid this).

When programming, we talk a lot about decoupling. Yet when we deploy to an application server (or servlet container), we rely on the fact that the container will run our JSP's as expected. For this, you need to ensure that the servlet container in production is exactly the same as when you are programming to be 100% sure everything works as expected. Fair enough, that makes somehow sense. But if you want to upgrade your server or you want to upgrade from a developer point of view, suddenly a lot of communication between the infrastructure team and development team needs to happen. So we get a tight coupling between the application and the application server itself. It would be nice to decouple this...

Using a template engine can offer more benefits than only decoupling. But to start, how do you configure Thymeleaf. Or better yet, how do you configure Thymeleaf **without** breaking existing views. We want a gradual migration.

As an example, we will use a standard Spring Boot application.

    @Configuration
    @EnableAutoConfiguration
    @ComponentScan
    public class AppConfig {

        @Value("${spring.view.prefix:}")
        private String prefix = "";

        @Value("${spring.view.suffix:}")
        private String suffix = "";

        @Bean
        public InternalResourceViewResolver internalResourceViewResolver() {
            InternalResourceViewResolver resolver = new InternalResourceViewResolver();
            resolver.setPrefix(prefix);
            resolver.setSuffix(suffix);
            resolver.setViewClass(JstlView.class);
            resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
            return resolver;
        }
    }

With following dependencies:

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-jasper</artifactId>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
    </dependency>

So what happened? First off we configured Spring Boot to use Thymeleaf by adding a dependency to "spring-boot-starter-thymeleaf". Thymeleaf will be used by default since this is in the autoconfiguration.
Now we add another viewresolver, the one that is used for the JSP's and we give it the highest priority:

    resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);

Now, you can start using Thymeleaf thymeleaf. But all other views (JSP's) continue to work. Even better, if you by accident create a view with the same name, the JSP still has a higher priority. So from now you can start using Thymeleaf, or start migrating other JSP's to Thymeleaf.

