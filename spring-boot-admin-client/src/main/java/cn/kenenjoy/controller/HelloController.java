package cn.kenenjoy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hefa on 2018/4/30.
 */
@RestController
public class HelloController {
    private final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/hello")
    public String index(@RequestParam String name) {
        logger.info("request one  name is "+name);
        return "hello "+name+"，this is first messge";
    }
}