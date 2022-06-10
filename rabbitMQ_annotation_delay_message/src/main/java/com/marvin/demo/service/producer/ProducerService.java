package com.marvin.demo.service.producer;

import com.marvin.demo.entity.UserBean;
import com.marvin.demo.model.UserRequestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@PropertySource("classpath:rabbitConfig.properties")
public class ProducerService {

    @Autowired
    private Environment env;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 发送消息到延时队列【订单延时关闭场景】
     */
    public void sendMsgToDelayQueue() {

        //订单交换机
        String orderExchange = env.getProperty("learn_annotation_delay_OrderTopicExchange");
        //延时队列路由key（消息路由到订单延时关闭队列）
        String routingKey = env.getProperty("learn_annotation_delay_OrderTopictQueue_DelayCancelQueue_key");

        UserBean userBean = new UserBean(1, "1_a", "aa");
        UserRequestModel userRequest = new UserRequestModel();
        userRequest.setType("junitTest");
        userRequest.setStatus("success");
        userRequest.setDesc(String.format("method【sendMsgToDelayQueue()】routingKey【%s】", routingKey));
        userRequest.setContent(userBean);

        //设置消息的唯一标识（根据情况可加可不加）
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(uuid);

        /**
         * 参数事例
         * rabbitTemplate.convertAndSend(String exchange, String routingKey, final Object message, final MessagePostProcessor messagePostProcessor, @Nullable CorrelationData correlationData)
         *
         * @param exchange 交换机
         * @param routingKey 路由key
         * @param message 消息
         * @param messagePostProcessor 消息处理器
         * @param correlationData 消息的唯一标识（根据情况可加可不加）
         */
        rabbitTemplate.convertAndSend(orderExchange, routingKey, userRequest, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置消息过期时间 10 秒
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        }, correlationData);
        log.info("Send method【sendMsgToDelayQueue()】routingKey【{}】", routingKey);
        log.info("向延时队列发送时间：【{}】", new Date());

    }
}
