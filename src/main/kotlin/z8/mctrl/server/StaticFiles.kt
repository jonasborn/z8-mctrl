package z8.mctrl.server

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import z8.mctrl.exception.*
import java.util.*

@Configuration
@EnableWebMvc
class StaticFiles : WebMvcConfigurer {


    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/fonts/**")
            .addResourceLocations("/WEB-INF/fonts/")
    }
}