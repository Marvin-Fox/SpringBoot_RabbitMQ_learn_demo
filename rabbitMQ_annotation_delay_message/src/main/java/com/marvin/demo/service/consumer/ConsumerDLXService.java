package com.marvin.demo.service.consumer;

import com.marvin.demo.entity.UserBean;
import com.marvin.demo.model.UserRequestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import java.util.Date;

/**
 * 死信队列消息监听服务
 */
@Slf4j
@Component
@PropertySource("classpath:rabbitConfig.properties")
@RabbitListener(queues = "learn_annotation_delay_DirectQueue_DLX_Queue")
public class ConsumerDLXService {

    @RabbitHandler
    public void processDLX(@Payload UserRequestModel userRequestModel){
        log.info("Enter ConsumerDLXService --> processDLX()~~~~~~~~~~~~~~~~~~~");
        log.info("死信队列消费时间：【{}】", new Date());
        System.out.println("ConsumerDLXService queue1 msg:" + userRequestModel);
        System.out.println("ConsumerDLXService Object1 UserRequestModel:" + userRequestModel.toString());
        //获取真正的数据对象
        UserBean userBean = userRequestModel.getContent();
        System.out.println("ConsumerDLXService Object1 UserBean:" + (ObjectUtils.isEmpty(userBean) ? null : userBean.toString()));
    }




}
