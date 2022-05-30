package com.marvin.demo;

import com.marvin.demo.common.rabbitmq.config.RabbitMqConfig;
import com.marvin.demo.entity.UserBean;
import com.marvin.demo.model.UserRequestModel;
import lombok.extern.slf4j.Slf4j;
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
 * 测试Topic模式：消息在手动ack模式下的消息完整性
 * 注意：测试的时候由于junit也是一个启动服务，可能发送的消息一部分会被junit的服务消费者消费，看日志打印时也需要检查junit的消费日志
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationAnnotationFull.class)
@Import({RabbitMqConfig.class})
@Slf4j
public class TestSendTopic {

    private final String TOPIC_EXCHANGE = "learn_annotation_TopicExchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 测试topic #
     * 匹配零个或多个【单词】
     * routingKey：TopicQueue1.#
     * 消费者监听方法：com.marvin.demo.service.consumer.ConsumerTopic#processTopic1(com.marvin.demo.model.UserRequestModel)
     */
    @Test
    public void testTopic1_a() {
        String routingKey = "TopicQueue1.aa.aa";

        UserBean userBean = new UserBean(1, "1_a", "aa");
        UserRequestModel userRequest = new UserRequestModel();
        userRequest.setType("junitTest");
        userRequest.setStatus("success");
        userRequest.setDesc(String.format("method【testTopic1_a()】routingKey【%s】", routingKey));
        userRequest.setContent(userBean);

        //设置消息的唯一标识（根据情况可加可不加）
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(uuid);
        //参数事例：rabbitTemplate.convertAndSend(String exchange, String routingKey, final Object object,CorrelationData correlationData);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, routingKey, userRequest, correlationData);
        log.info("send method【testTopic1_a()】routingKey【{}】", routingKey);
    }

    @Test
    public void testTopic1_b() {
        String routingKey = "TopicQueue1.bb";

        UserBean userBean = new UserBean(1, "1_b", "bb");
        UserRequestModel userRequest = new UserRequestModel();
        userRequest.setType("junitTest");
        userRequest.setStatus("success");
        userRequest.setDesc(String.format("method【testTopic1_b()】routingKey【%s】", routingKey));
        userRequest.setContent(userBean);

        //设置消息的唯一标识（根据情况可加可不加）
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, routingKey, userRequest, correlationData);
        log.info("send method【testTopic1_b()】routingKey【{}】", routingKey);
    }


    /**
     * 测试topic #
     * 匹配零个或多个【单词】
     * routingKey：TopicQueue2.#
     * 消费者监听方法：com.marvin.demo.service.consumer.ConsumerTopic#processTopic2(com.marvin.demo.model.UserRequestModel)
     */
    @Test
    public void testTopic2_a() {
        String routingKey = "TopicQueue2";

        UserBean userBean = new UserBean(2, "2_a", "aa");
        UserRequestModel userRequest = new UserRequestModel();
        userRequest.setType("junitTest");
        userRequest.setStatus("success");
        userRequest.setDesc(String.format("method【testTopic2_a()】routingKey【%s】", routingKey));
        userRequest.setContent(userBean);

        //设置消息的唯一标识（根据情况可加可不加）
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, routingKey, userRequest, correlationData);
        log.info("send method【testTopic2_a()】routingKey【{}】", routingKey);
    }


    /**
     * 测试topic *
     * 匹配一个【单词】
     * routingKey：TopicQueue3.*
     * 消费者监听方法：com.marvin.demo.service.consumer.ConsumerTopic#processTopic3(com.marvin.demo.model.UserRequestModel)
     */
    @Test
    public void testTopic3_a() {
        String routingKey = "TopicQueue3.a";

        UserBean userBean = new UserBean(3, "3_a", "aa");
        UserRequestModel userRequest = new UserRequestModel();
        userRequest.setType("junitTest");
        userRequest.setStatus("success");
        userRequest.setDesc(String.format("method【testTopic3_a()】routingKey【%s】", routingKey));
        userRequest.setContent(userBean);

        //设置消息的唯一标识（根据情况可加可不加）
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, routingKey, userRequest, correlationData);
        log.info("send method【testTopic3_a()】routingKey【{}】", routingKey);
    }

    @Test
    public void testTopic3_b() {
        String routingKey = "TopicQueue3.bbb";

        UserBean userBean = new UserBean(3, "3_b", "bb");
        UserRequestModel userRequest = new UserRequestModel();
        userRequest.setType("junitTest");
        userRequest.setStatus("success");
        userRequest.setDesc(String.format("method【testTopic3_b()】routingKey【%s】", routingKey));
        userRequest.setContent(userBean);

        //设置消息的唯一标识（根据情况可加可不加）
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, routingKey, userRequest, correlationData);
        log.info("send method【testTopic3_b()】routingKey【{}】", routingKey);
    }

    @Test
    public void testTopic3_c() {
        //这个key是不匹配的，所以应该不会有对应队列被触发
        String routingKey = "TopicQueue3.cc.cc";

        UserBean userBean = new UserBean(3, "3_c", "cc");
        UserRequestModel userRequest = new UserRequestModel();
        userRequest.setType("junitTest");
        userRequest.setStatus("success");
        userRequest.setDesc(String.format("method【testTopic3_c()】routingKey【%s】", routingKey));
        userRequest.setContent(userBean);

        //设置消息的唯一标识（根据情况可加可不加）
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, routingKey, userRequest, correlationData);
        log.info("send method【testTopic3_c()】routingKey【{}】", routingKey);
    }


}
