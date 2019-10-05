package com.marvin.demo.consumer;

import com.alibaba.fastjson.JSON;
import com.marvin.demo.entity.UserBean;
import com.marvin.demo.request.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
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
            exchange = @Exchange(value = "${learn_annotation_DirectExchange}",type = ExchangeTypes.DIRECT),
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
            exchange = @Exchange(value = "${learn_annotation_DirectExchange}",type = ExchangeTypes.DIRECT),
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


    /**
     * 使用convertAndSend传参时会自动序列化
     * 监听接值的方法参数一定要一致才会自动转换
     * @param userBean
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${learn_annotation_DirectQueue3}"),
            exchange = @Exchange(value = "${learn_annotation_DirectExchange}",type = ExchangeTypes.DIRECT),
            key = "direct3"
    ))
    public void processDirect3(UserBean userBean){
        log.info("enter ConsumerDirect-->processDirect3()~~~~~~~~~~~~~~~~~~~");
        //接收参数自动转换反序列化
        System.out.println("ConsumerDirect queue3 msg:"+userBean);
        System.out.println("ConsumerDirect Object3 UserBean:"+userBean.toString());
    }




}
