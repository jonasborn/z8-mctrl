package z8.mctrl.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestRest {

    @GetMapping("/test")
    fun getEmpl():String {
        return "Working!";
    }
}