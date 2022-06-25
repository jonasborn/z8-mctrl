package z8.mctrl.controller.nav

import org.springframework.web.context.annotation.RequestScope
import javax.inject.Named

@Named("DefaultNavController")
@RequestScope
open class DefaultNavController {

    var admin = true


}