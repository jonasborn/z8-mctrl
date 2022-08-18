import org.springframework.beans.factory.config.CustomScopeConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.SimpleThreadScope
import org.springframework.web.context.WebApplicationContext

@Configuration
class TestConfig {

    @Bean
    public fun customScopeConfigurer(): CustomScopeConfigurer {
        val scopeConfigurer = CustomScopeConfigurer()

        val scopes: HashMap<String, Any> = HashMap<String, Any>()
        scopes.put(
            WebApplicationContext.SCOPE_REQUEST,
            SimpleThreadScope()
        )
        scopes.put(
            WebApplicationContext.SCOPE_SESSION,
            SimpleThreadScope()
        )
        scopeConfigurer.setScopes(scopes)

        return scopeConfigurer

    }

}