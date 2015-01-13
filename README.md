# From JSP to Thymeleaf #

Template engines are getting more and more popular over the usage of JSP's. For Spring applications, mainly Thymeleaf is getting a lot of attention. In a lot of samples that are given by Spring, Thymeleaf is used as template engine.

JSP's have proven their strength in the past, but it also has weaknesses. The good thing about JSP's is that it is a standard and you can do almost anything to achieve your goal. We are of course talking about using the expression language and not scriptlets (avoid this).

When programming, we talk a lot about decoupling. Yet when we deploy to an application server (or servlet container), we rely on the fact that the container will run our JSP's as expected. For this, you need to ensure that the servlet container in production is exactly the same as when you are programming to be 100% sure everything works as expected (example: tags can behave differently in different tomcat versions). Fair enough, that makes somehow sense. But if you want to upgrade your server or you want to upgrade from a developer point of view, suddenly a lot of communication between the infrastructure team and development team needs to happen. So we get a tight coupling between the application and the application server itself. It would be nice to decouple this...

A template engine can offer more benefits than only decoupling. But to start, how do you configure Thymeleaf. Or better yet, how do you configure Thymeleaf next to existing views without breaking them. We want a gradual migration.

## Configuration ##
As an example, we will use a standard Spring Boot application.

    @SpringBootApplication
    public class AppConfig {

        @Value("${spring.view.prefix:}")
        private String prefix = "";

        @Value("${spring.view.suffix:}")
        private String suffix = "";

        @Value("${spring.view.view-names:}")
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

And following properties (application.properties):

    spring.view.prefix:/WEB-INF/
    spring.view.suffix:.jsp
    spring.view.view-names:jsp/*
    spring.thymeleaf.view-names:thymeleaf/*

So what happened? First we configured Spring Boot to use Thymeleaf by adding a dependency to "spring-boot-starter-thymeleaf". Thymeleaf will be used by default since this is in the autoconfiguration.
Now we add another viewresolver (InternalResourceViewResolver), the one that is used for the JSP's and we configure it with what is in the application properties.

For the JSP's, we configured the view resolver as: views (JSP's) are inside WEB-INF, but only handle files that start with "jsp/" (viewnames). So our structure for the JSP's looks like this: 

    > WEB-INF
    	> jsp
    		> home.jsp
Thymeleaf uses the same approach, by default all templates are stored inside the "templates" folder of your resources. To be able to easily distinguish between jsp and thymeleaf, we configured the viewnames for thymeleaf as "thymeleaf/*". So our thymeleaf structure is the following:

    > resources
    	> templates
    		> thymeleaf

**When we write a controller and we let it return a view starting with "jsp/", the jsp view resolver will be used. If you return a view starting with "thymeleaf/", the thymeleaf resolver is chosen.**

## Example ##
The following controller is using this logic. By default, the jsp view is returned. Additionally, a request parameter is added so that you can even switch viewresolver at runtime. You probably won't need this for your project, but it was a good example to demonstrate the behaviour:
 

    @Controller
    public class HomeController {
    
        @RequestMapping(value = "/", method = RequestMethod.GET)
        public ModelAndView showHome(@RequestParam(value = "viewResolver") Optional<String> viewResolver) {
            return getDefaultView(viewResolver);
        }
    
        private ModelAndView getDefaultView(Optional<String> viewResolver) {
            ModelAndView model = new ModelAndView(createView(viewResolver, "/home"));
            model.addObject("name", "Spring Squad");
            return model;
        }
    
        private String createView(Optional<String> viewResolver, String viewName) {
            String result = viewResolver.isPresent() ? viewResolver.get() : "jsp";
            result += viewName;
            return result;
        }
    }
Full code is available on github.

### Testing ###
One of the advantages on using a template engine that it can make testing controllers easier/more thorough. 
For example, when using mockmvc it really returns the rendered html page.
JSP version:

    MockHttpServletResponse:
                  Status = 200
           Error message = null
                 Headers = {}
            Content type = null
                    Body = 
           Forwarded URL = /WEB-INF/jsp/home.jsp
          Redirected URL = null
                 Cookies = []

Thymeleaf version:

    MockHttpServletResponse:
                  Status = 200
           Error message = null
                 Headers = {Content-Type=[text/html;charset=UTF-8]}
            Content type = text/html;charset=UTF-8
                    Body = <!DOCTYPE html>
    
    <html xmlns="http://www.w3.org/1999/xhtml">
    <head lang="en">
        <meta charset="UTF-8" />
        <title>Thymeleaf</title>
    </head>
    <body>
    <h1>Hello <span>Spring Squad</span> from Thymeleaf!</h1>
    </body>
    </html>
           Forwarded URL = null
          Redirected URL = null
                 Cookies = []
Source for testing the controller can be found on github.

But more on testing using Thymeleaf later... This is already showing what it returns, later on we will explore how we can benefit from this!

## Conclusion ##
Using this or a similar approach, one can really migrate from JSP's to Thymeleaf. This can be done gradually which makes it a good selling point. In this post we did not explore the real benefits of using a template engine or how to use it. Hence, this will be the subject of a next blogpost. So keep an eye on this blog!

## Links ##
 - Source code
 - [Thymeleaf + Spring](http://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html)

