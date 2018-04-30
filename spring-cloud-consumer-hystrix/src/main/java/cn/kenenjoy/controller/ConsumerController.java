package cn.kenenjoy.controller;

import cn.kenenjoy.remote.HelloRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hefa on 2018/4/30.
 */
@RestController
public class ConsumerController {

    private final Logger logger = LoggerFactory.getLogger(ConsumerController.class);

    @Autowired
    HelloRemote helloRemote;

    /**
     * http://127.0.0.1:9004/hello/zhangsan
     * @param name
     * @return
     */
    @RequestMapping("/hello/{name}")
    public String index(@PathVariable("name") String name) {
        logger.info("name:" + name);
        return helloRemote.hello(name);
    }

}
