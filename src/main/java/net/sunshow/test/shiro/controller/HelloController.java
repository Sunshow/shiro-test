package net.sunshow.test.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/")
    //@RequiresUser
    public String index() {
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject.isAuthenticated());
        System.out.println(subject.isRemembered());
        return "Greetings from Spring Boot!";
    }

}
