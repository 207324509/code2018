package cn.kenenjoy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * Created by hefa on 2018/4/30.
 *
 * @EnableDiscoveryClient :启用服务注册与发现
 * @EnableFeignClients：启用feign进行远程调用
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerHystrixApplication {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerHystrixApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsumerHystrixApplication.class, args);
    }
}
