package z8.mctrl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomServletContextInitializer implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        sc.setInitParameter("org.butterfaces.provideBootstrap", "false");
        sc.setInitParameter("org.butterfaces.ajaxDisableRenderRegionsOnRequest", "true");
        sc.setInitParameter("org.butterfaces.provideJQuery", "true");
        sc.setInitParameter("org.butterfaces.provideBootstrap", "false");
    }

}