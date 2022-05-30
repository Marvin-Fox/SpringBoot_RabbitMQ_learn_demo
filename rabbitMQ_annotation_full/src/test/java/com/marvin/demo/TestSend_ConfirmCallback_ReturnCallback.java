package com.marvin.demo;

import com.marvin.demo.common.rabbitmq.config.RabbitMqConfig;
import com.marvin.demo.entity.UserBean;
import com.marvin.demo.model.UserRequestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * 测试消息投递正常和异常情况【保证消息投递可靠性传输】
 * 保证client生产者消息在能正确投递到rabbitmq server【避免投递丢失】
 * 注意：测试的时候由于junit也是一个启动服务，可能发送的消息一部分会被junit的服务消费者消费，看日志打印时也需要检查junit的消费日志
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationAnnotationFull.class)
//引入配置
@Import({RabbitMqConfig.class})
public class TestSend_ConfirmCallback_ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 正常投递测试
     * 主要是验证回调消息 com.marvin.demo.common.rabbitmq.MessageCallBack
     * 消费者监听方法：com.marvin.demo.service.consumer.ConsumerTopic#processTopic1(com.marvin.demo.model.UserRequestModel)
     */
    @Test
    public void testConfrim1() {
        UserBean userBean = new UserBean(1, "1", "11");
        UserRequestModel userRequest = new UserRequestModel();
        userRequest.setType("junitTest");
        userRequest.setStatus("success");
        userRequest.setDesc("method【testConfrim1()】");
        userRequest.setContent(userBean);

        //设置消息的唯一标识
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(uuid);
        //参数事例：rabbitTemplate.convertAndSend(String exchange, String routingKey, final Object object,CorrelationData correlationData);
        rabbitTemplate.convertAndSend("learn_annotation_TopicExchange", "TopicQueue1.bb", userRequest, correlationData);
    }


    /**
     * 投递异常情况的演示
     * 根据控制台输出结果查看出错的问题
     * 主要是验证回调消息 com.marvin.demo.common.rabbitmq.MessageCallBack
     * 消费者监听方法：com.marvin.demo.service.consumer.ConsumerTopic#processTopic1(com.marvin.demo.model.UserRequestModel)
     */
    @Test
    public void testConfrim2() {
        UserBean userBean = new UserBean(2, "2", "22");
        UserRequestModel userRequest = new UserRequestModel();
        userRequest.setType("junitTest");
        userRequest.setStatus("success");
        userRequest.setDesc("method【testConfrim2()】");
        userRequest.setContent(userBean);

        //设置消息的唯一标识
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(uuid);

        //正确的Exchange，错误的routingKey
        rabbitTemplate.convertAndSend("learn_annotation_TopicExchange", "aaTopicQueue1.bb", userRequest, correlationData);

        //错误的Exchange，错误的routingKey
//        rabbitTemplate.convertAndSend("learn_annotation_TopicExchange_hahaha", "aaTopicQueue1.bb", userRequest, correlationData);

        //无权限或错误的Exchange
//        rabbitTemplate.convertAndSend("test_exchange_topic", "goods.delete", "商品....删除", correlationData);

    }
}
