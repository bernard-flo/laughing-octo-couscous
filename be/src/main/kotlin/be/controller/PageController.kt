package be.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
private class PageController {

    @RequestMapping(
        value = [
            "/",
            "/manager",
            "/manager-more",
            "/presenter",
        ]
    )
    fun page(): ModelAndView {

        return ModelAndView("index.html")
    }

}
