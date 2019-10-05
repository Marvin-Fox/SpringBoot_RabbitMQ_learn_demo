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

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationConsumerAnnotation.class)
public class TestSendConfirm {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("correlationData: " + correlationData);

            if(ack){
                //如果confirm返回成功 则进行更新
                System.out.println("消息确认成功");
            } else {
                //（nack）失败则进行具体的后续操作:重试 或者补偿等手段
                System.out.println("消息确认失败,异常处理...");
            }
        }
    };

    @Test
    public void testConfrim() {
        UserBean userBean=new UserBean(1,"1","11");

        JSONObject msg=new JSONObject();
        msg.put("type","junitTest");
        msg.put("status","success");
        msg.put("desc","testConfrim");
        msg.put("content",userBean);

        //确认消息是否到达broker服务器，也就是只确认是否正确到达exchange中即可，只要正确的到达exchange中，broker即可确认该消息返回给客户端ack。
//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            @Override
//            public void confirm(CorrelationData correlationData, boolean ack, String s) {
//                if (ack) {
//                    System.out.println("消息确认成功");
//                } else {
//                    //处理丢失的消息（nack）
//                    System.out.println("消息确认失败");
//                }
//            }
//        });

        //此处将上面的代码抽出来写在了全局变量中
        rabbitTemplate.setConfirmCallback(confirmCallback);


        //参数事例：rabbitTemplate.convertAndSend(String exchange, String routingKey, Object object);
        //正确的目标
        rabbitTemplate.convertAndSend("learn_annotation_TopicExchange","TopicQueue1.bb",msg.toJSONString());
        //无权限或错误的目标
        rabbitTemplate.convertAndSend("test_exchange_topic","goods.delete","商品....删除");
    }
}
