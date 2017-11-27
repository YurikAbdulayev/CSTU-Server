package com.cstu.web.job;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class JobController {

    @RequestMapping(method = RequestMethod.GET)
    public String jobDetails(ModelMap map) {

        return "job";
    }
}
