package com.marvin.demo.consumer;

import com.alibaba.fastjson.JSON;
import com.marvin.demo.request.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Direct
 */
@Slf4j
@Component
@PropertySource("classpath:rabbitConfig.properties")
public class ConsumerDirect {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${learn_annotation_DirectQueue1}"),
            exchange = @Exchange(value = "${learn_annotation_DirectExchange}",type = "direct"),
            key = "direct1"
        )
    )
    public void processDirect1(String message){
        log.info("enter ConsumerDirect-->processDirect1()~~~~~~~~~~~~~~~~~~~");
        System.out.println("ConsumerDirect queue1 msg:"+message);
        //接收并将消息转换为pojo
        UserRequest userRequest= JSON.parseObject(message, UserRequest.class);
        System.out.println("ConsumerDirect Object1 UserRequest:"+userRequest.toString());
        System.out.println("ConsumerDirect Object1 UserBean:"+userRequest.getContent().toString());
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${learn_annotation_DirectQueue2}"),
            exchange = @Exchange(value = "${learn_annotation_DirectExchange}",type = "direct"),
            key = "direct2"
        )
    )
    public void processDirect2(String message){
        log.info("enter ConsumerDirect-->processDirect2()~~~~~~~~~~~~~~~~~~~");
        System.out.println("ConsumerDirect queue2 msg:"+message);
        //接收并将消息转换为pojo
        UserRequest userRequest= JSON.parseObject(message, UserRequest.class);
        System.out.println("ConsumerDirect Object2 UserRequest:"+userRequest.toString());
        System.out.println("ConsumerDirect Object2 UserBean:"+userRequest.getContent().toString());
    }


}
