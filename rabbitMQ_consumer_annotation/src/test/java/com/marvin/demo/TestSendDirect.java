package com.marvin.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.marvin.demo.entity.UserBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationConsumerAnnotation.class)
public class TestSendDirect {

    @Autowired
    private AmqpTemplate amqpTemplate;
    public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }
    @Test
    public void testDirect1() {
        UserBean userBean=new UserBean(1,"aa","aa");

        JSONObject msg=new JSONObject();
        msg.put("type","junitTest");
        msg.put("status","success");
        msg.put("desc","direct1");
        msg.put("content",userBean);
        amqpTemplate.convertAndSend("learn_annotation_DirectExchange","direct1",msg.toJSONString());
    }



    @Test
    public void testDirect2() {
        UserBean userBean=new UserBean(2,"bb","bb");

        JSONObject msg=new JSONObject();
        msg.put("type","junitTest");
        msg.put("status","success");
        msg.put("desc","direct2");
        msg.put("content",userBean);
        amqpTemplate.convertAndSend("learn_annotation_DirectExchange","direct2",msg.toJSONString());
    }






}
