package cn.kenenjoy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hefa on 2018/5/24.
 */
@RefreshScope
@RestController
public class HelloController {
    @Value("${app.name}")
    private String name;

    @Value("${app.version}")
    private String version;

    @RequestMapping("/name")
    public String getName() {
        return this.name + ":" + this.version;
    }
}
