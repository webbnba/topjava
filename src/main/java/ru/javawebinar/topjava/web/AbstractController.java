package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public abstract class AbstractController {

    private static final Logger log = LoggerFactory.getLogger(AbstractController.class);

    @GetMapping("/")
    public String root() {
        log.info("root");
        return "index";
    }
}
