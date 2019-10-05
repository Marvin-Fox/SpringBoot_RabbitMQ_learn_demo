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

@Slf4j
@Component
@PropertySource("classpath:rabbitConfig.properties")
public class ConsumerFanout {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${learn_annotation_FanoutQueue_Email}"),
            exchange = @Exchange(value = "${learn_annotation_FanoutExchange}",type = "fanout" )
    ))
    public void processEmail(String message){
        log.info("enter ConsumerFanout-->processEmail()~~~~~~~~~~~~~~~~~~~");
        System.out.println("ConsumerDirect queueEmail msg:"+message);
        //接收并将消息转换为pojo
        UserRequest userRequest= JSON.parseObject(message, UserRequest.class);
        System.out.println("ConsumerFanout ObjectEmail UserRequest:"+userRequest.toString());
        System.out.println("ConsumerFanout ObjectEmail UserBean:"+userRequest.getContent().toString());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${learn_annotation_FanoutQueue_SMS}"),
            exchange = @Exchange(value = "${learn_annotation_FanoutExchange}",type = "fanout" )
    ))
    public void processMSM(String message){
        log.info("enter ConsumerFanout-->processMSM()~~~~~~~~~~~~~~~~~~~");
        System.out.println("ConsumerDirect queueMSM msg:"+message);
        //接收并将消息转换为pojo
        UserRequest userRequest= JSON.parseObject(message, UserRequest.class);
        System.out.println("ConsumerFanout ObjectMSM  UserRequest:"+userRequest.toString());
        System.out.println("ConsumerFanout ObjectMSM  UserBean:"+userRequest.getContent().toString());
    }
}
