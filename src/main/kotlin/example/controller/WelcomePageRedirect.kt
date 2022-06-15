package example.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WelcomePageRedirect : WebMvcConfigurer {

  override fun addViewControllers(registry: ViewControllerRegistry) {
    registry.addViewController("/").setViewName("forward:/index.jsp");
    registry.addViewController("/welcome").setViewName("forward:/welcome.xhtml");
    registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
  }
}
