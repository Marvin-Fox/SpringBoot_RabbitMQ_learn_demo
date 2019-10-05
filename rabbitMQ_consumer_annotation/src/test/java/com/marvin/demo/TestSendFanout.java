package com.marvin.demo;

import com.alibaba.fastjson.JSONObject;
import com.marvin.demo.entity.UserBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationConsumerAnnotation.class)
public class TestSendFanout {

    @Autowired
    private AmqpTemplate amqpTemplate;
    public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Test
    public void testFanout() {
        UserBean userBean=new UserBean(1,"aa","aa");

        JSONObject msg=new JSONObject();
        msg.put("type","junitTest");
        msg.put("status","success");
        msg.put("desc","Fanout");
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
        //Fanout模式中routingKey设置为""空字符串，则表示发送给所有
        rabbitTemplate.convertAndSend("learn_annotation_FanoutExchange","",msg.toJSONString());
    }
}
