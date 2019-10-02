package com.marvin.demo.consumer;


import com.alibaba.fastjson.JSON;
import com.marvin.demo.entity.UserBean;
import com.marvin.demo.request.UserRequest;
import com.marvin.demo.service.SMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * 似乎只有加了@RabbitListener才会创建配置类里面的Queue 和 Exchange
 */
@Slf4j
@Component
@PropertySource("classpath:rabbitConfig.properties")
@RabbitListener(queues = "${learn.direct.queue}")
public class FirstConsumer {

    @Autowired
    private SMSService smsService;
    public void setSmsService(SMSService smsService) {
        this.smsService = smsService;
    }

    /**
     * @RabbitHandler：一旦有消息到达这个队列时会触发这个方法的执行
     * @param message:使用了传递的String类型数据
     */
    @RabbitHandler
    public void process(String message){
        log.info("enter FirstConsumer-->process()~~~~~~~~~~~~~~~~~~~");
        System.out.println("FirstConsumer queue msg:"+message);
        //接收并将消息转换为pojo
        UserRequest userRequest=JSON.parseObject(message, UserRequest.class);
        System.out.println("FirstConsumer Object UserRequest:"+userRequest.toString());
        System.out.println("FirstConsumer Object UserBean:"+userRequest.getContent().toString());
        //去发送短信
        smsService.doSendSMS(userRequest.getContent());

    }


    /**
     * @RabbitHandler：一旦有消息到达这个队列时会触发这个方法的执行
     * @param userBean
     */
//    @RabbitHandler
//    public void process(@Payload UserBean userBean){
//        System.out.println("FirstConsumer queue msg:"+userBean.getUsername());
//    }


}
