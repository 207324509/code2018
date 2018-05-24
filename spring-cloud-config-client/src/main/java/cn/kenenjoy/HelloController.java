package cn.kenenjoy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hefa on 2018/5/24.
 */
@RestController
public class HelloController {
    @Value("${app.name}")
    private String name;

    @RequestMapping("/name")
    public String getName() {
        return this.name;
    }
}
