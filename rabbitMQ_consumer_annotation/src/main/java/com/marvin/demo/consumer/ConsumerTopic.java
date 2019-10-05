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
 * 注意topic模式，routingKey只能用“.”点来分隔【单词】,使用#或者*，进行模糊匹配路由
 * 重点：模糊匹配是按【单词】算的，“.”点是来分隔【单词】的，而不是字符，点之间的都被定义为一个【单词】
 *
 * 正确：routingKey = TopicQueue1.#(只有使用点符号分割才会被识别为模糊匹配)
 * 错误：routingKey = TopicQueue1_#(这个是完全匹配，不会识别为模糊匹配)
 *
 * 正确：routingKey = TopicQueue3.*.*.test
 * 错误：routingKey = TopicQueue3.*t(没有这样写的模糊匹配)
 */
@Slf4j
@Component
@PropertySource("classpath:rabbitConfig.properties")
public class ConsumerTopic {


    /**
     * routingKey=TopicQueue1.#
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${learn_annotation_TopicQueue1}"),
            exchange = @Exchange(value = "${learn_annotation_TopicExchange}",type = "topic"),
            key = "TopicQueue1.#"
    ))
    public void processTopic1(String message){
        log.info("enter ConsumerTopic-->processTopic1()~~~~~~~~~~~~~~~~~~~");
        System.out.println("ConsumerTopic queue1 msg:"+message);
        //接收并将消息转换为pojo
        UserRequest userRequest= JSON.parseObject(message, UserRequest.class);
        System.out.println("ConsumerTopic Object1 UserRequest:"+userRequest.toString());
        System.out.println("ConsumerTopic Object1 UserBean:"+userRequest.getContent().toString());
    }

    /**
     * routingKey=TopicQueue2.#
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${learn_annotation_TopicQueue2}"),
            exchange = @Exchange(value = "${learn_annotation_TopicExchange}",type = "topic"),
            key = "TopicQueue2.#"
    ))
    public void processTopic2(String message){
        log.info("enter ConsumerTopic-->processTopic2()~~~~~~~~~~~~~~~~~~~");
        System.out.println("ConsumerTopic queue2 msg:"+message);
        //接收并将消息转换为pojo
        UserRequest userRequest= JSON.parseObject(message, UserRequest.class);
        System.out.println("ConsumerTopic Object2 UserRequest:"+userRequest.toString());
        System.out.println("ConsumerTopic Object2 UserBean:"+userRequest.getContent().toString());
    }


    /**
     * routingKey=TopicQueue3.*
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${learn_annotation_TopicQueue3}"),
            exchange = @Exchange(value = "${learn_annotation_TopicExchange}",type = "topic"),
            key = "TopicQueue3.*"
    ))
    public void processTopic3(String message){
        log.info("enter ConsumerTopic-->processTopic3()~~~~~~~~~~~~~~~~~~~");
        System.out.println("ConsumerTopic queue3 msg:"+message);
        //接收并将消息转换为pojo
        UserRequest userRequest= JSON.parseObject(message, UserRequest.class);
        System.out.println("ConsumerTopic Object3 UserRequest:"+userRequest.toString());
        System.out.println("ConsumerTopic Object3 UserBean:"+userRequest.getContent().toString());
    }


}
