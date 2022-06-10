package com.marvin.demo;

import com.marvin.demo.common.rabbitmq.config.RabbitMqConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
//引入配置
@Import({RabbitMqConfig.class})
public class ApplicationAnnotationDelayMessage {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationAnnotationDelayMessage.class);
    }
}
