package com.marvin.demo.consumer;

import com.alibaba.fastjson.JSON;
import com.marvin.demo.request.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 */
@Slf4j
@Component
@PropertySource("classpath:rabbitConfig.properties")
public class SecondConsumer {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("myQueue"),
            exchange = @Exchange("myExchange")
    ))
    public void process(String message){
        log.info("enter SecondConsumer-->process()~~~~~~~~~~~~~~~~~~~");
        System.out.println("SecondConsumer queue msg:"+message);
        //接收并将消息转换为pojo
        UserRequest userRequest= JSON.parseObject(message, UserRequest.class);
        System.out.println("SecondConsumer Object UserRequest:"+userRequest.toString());
        System.out.println("SecondConsumer Object UserBean:"+userRequest.getContent().toString());
    }


}
