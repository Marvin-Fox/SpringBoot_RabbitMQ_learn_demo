package com.marvin.demo;

import com.alibaba.fastjson.JSONObject;
import com.marvin.demo.entity.UserBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationConsumerAnnotation.class)
public class TestSendTopic {

    private final String TOPIC_EXCHANGE="learn_annotation_TopicExchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    /**
     * 测试#
     * 匹配零个或多个【单词】
     * routingKey：TopicQueue1.#
     * 调用方法：processTopic1（）
     */
    @Test
    public void testTopic1_a() {
        String routingKey="TopicQueue1.aa.aa";

        UserBean userBean=new UserBean(1,"1_a","aa");
        JSONObject msg=new JSONObject();
        msg.put("type","junitTest");
        msg.put("status","success");
        msg.put("desc",routingKey);
        msg.put("content",userBean);

        //确认消息是否到达broker服务器，也就是只确认是否正确到达exchange中即可，只要正确的到达exchange中，broker即可确认该消息返回给客户端ack。
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String s) {
                if (ack) {
                    System.out.println("消息确认成功");
                } else {
                    //处理丢失的消息（nack）
                    System.out.println("消息确认失败");
                }
            }
        });
        //rabbitTemplate.convertAndSend(String exchange, String routingKey, Object object);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE,routingKey,msg.toJSONString());
    }
    @Test
    public void testTopic1_b() {
        String routingKey="TopicQueue1.bb";

        UserBean userBean=new UserBean(1,"1_b","bb");
        JSONObject msg=new JSONObject();
        msg.put("type","junitTest");
        msg.put("status","success");
        msg.put("desc",routingKey);
        msg.put("content",userBean);
        //rabbitTemplate.convertAndSend(String exchange, String routingKey, Object object);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE,routingKey,msg.toJSONString());
    }




    /**
     * 测试topic #
     * 匹配零个或多个【单词】
     * routingKey：TopicQueue2.#
     * 调用方法：processTopic2（）
     */
    @Test
    public void testTopic2_a() {
        String routingKey="TopicQueue2";

        UserBean userBean=new UserBean(2,"2_a","aa");
        JSONObject msg=new JSONObject();
        msg.put("type","junitTest");
        msg.put("status","success");
        msg.put("desc",routingKey);
        msg.put("content",userBean);
        //rabbitTemplate.convertAndSend(String exchange, String routingKey, Object object);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE,routingKey,msg.toJSONString());
    }



    /**
     * 测试topic *
     * 匹配一个【单词】
     * routingKey：TopicQueue3.*
     * 调用方法：processTopic3（）
     */
    @Test
    public void testTopic3_a() {
        String routingKey="TopicQueue3.a";

        UserBean userBean=new UserBean(3,"3_a","aa");
        JSONObject msg=new JSONObject();
        msg.put("type","junitTest");
        msg.put("status","success");
        msg.put("desc",routingKey);
        msg.put("content",userBean);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE,routingKey,msg.toJSONString());
    }
    @Test
    public void testTopic3_b() {
        String routingKey="TopicQueue3.bbb";

        UserBean userBean=new UserBean(3,"3_b","bb");
        JSONObject msg=new JSONObject();
        msg.put("type","junitTest");
        msg.put("status","success");
        msg.put("desc",routingKey);
        msg.put("content",userBean);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE,routingKey,msg.toJSONString());
    }
    @Test
    public void testTopic3_c() {
        //这个key是不匹配的，所以应该不会有对应队列被触发
        String routingKey="TopicQueue3.cc.cc";

        UserBean userBean=new UserBean(3,"3_c","cc");
        JSONObject msg=new JSONObject();
        msg.put("type","junitTest");
        msg.put("status","success");
        msg.put("desc",routingKey);
        msg.put("content",userBean);
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE,routingKey,msg.toJSONString());
    }




}
