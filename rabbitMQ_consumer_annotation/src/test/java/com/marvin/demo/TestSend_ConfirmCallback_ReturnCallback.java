package com.marvin.demo;

import com.alibaba.fastjson.JSONObject;
import com.marvin.demo.entity.UserBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationConsumerAnnotation.class)
public class TestSend_ConfirmCallback_ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 回调函数: confirm确认
     * 消息发送到 Exchange 确认机制
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         * 当消息发送到交换机【exchange】时，该方法被调用.
         * 1.如果消息没有到exchange,则 ack=false
         * 2.如果消息到达exchange,则 ack=true
         * @param correlationData：唯一标识
         * @param ack：确认结果
         * @param cause：引起原因
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.out.println("confirm消息唯一标识："+correlationData);
            System.out.println("confirm确认结果："+ack);
            System.out.println("confirm引起原因："+cause);

            if(ack){
                //如果confirm返回成功 则进行更新
                System.out.println("confirm消息确认成功");
            } else {
                //（nack）失败则进行具体的后续操作:重试 或者补偿等手段
                System.out.println("confirm消息确认失败,异常处理...");
            }
        }
    };

    /**
     * 回调函数: return确认
     * 消息从 Exchange 发送到 Queue 确认机制
     * 注意：发送失败才调用该方法【成功不调用】
     */
    final RabbitTemplate.ReturnCallback returnCallback=new RabbitTemplate.ReturnCallback() {
        /**
         * 当消息从交换机到队列失败时，该方法被调用。（若成功，则不调用）
         * 需要注意的是：该方法调用后，MsgSendConfirmCallBack中的confirm方法也会被调用，且ack = true
         * @param message：传递的消息主体
         * @param replyCode：问题状态码
         * @param replyText：问题描述
         * @param exchange：使用的交换器
         * @param routingKey：使用的路由键
         */
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

            //失败则进行具体的后续操作:重试 或者补偿等手段。。。

            System.out.println("returnCallback消息主体 message : "+message);
            System.out.println("returnCallback消息回调 code replyCode : "+replyCode);
            System.out.println("returnCallback描述："+replyText);
            System.out.println("returnCallback消息使用的交换器 exchange : "+exchange);
            System.out.println("returnCallback消息使用的路由键 routing : "+routingKey);
        }
    };

    @Test
    public void testConfrim1() {
        UserBean userBean=new UserBean(1,"1","11");
        JSONObject msg=new JSONObject();
        msg.put("type","junitTest");
        msg.put("status","success");
        msg.put("desc","testConfrim1");
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
        rabbitTemplate.setReturnCallback(returnCallback);


        //参数事例：rabbitTemplate.convertAndSend(String exchange, String routingKey, Object object);
        //正确的目标
        rabbitTemplate.convertAndSend("learn_annotation_TopicExchange","TopicQueue1.bb",msg.toJSONString());

    }


    /**
     * 这个是发送错误情况的演示
     * 根据控制台输出结果查看出错的问题
     */
    @Test
    public void testConfrim2() {
        UserBean userBean=new UserBean(2,"2","22");
        JSONObject msg=new JSONObject();
        msg.put("type","junitTest");
        msg.put("status","success");
        msg.put("desc","testConfrim2");
        msg.put("content",userBean);

        //设置confirmCallback和returnCallback来确认消息是否正确发送到队列
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        //正确的Exchange，错误的routingKey
        rabbitTemplate.convertAndSend("learn_annotation_TopicExchange","aaTopicQueue1.bb",msg.toJSONString());

        //错误的Exchange，错误的routingKey
//        rabbitTemplate.convertAndSend("learn_annotation_TopicExchange_hahaha","aaTopicQueue1.bb",msg.toJSONString());

        //无权限或错误的Exchange
//        rabbitTemplate.convertAndSend("test_exchange_topic","goods.delete","商品....删除");

    }
}
