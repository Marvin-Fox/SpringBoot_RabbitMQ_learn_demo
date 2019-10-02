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
//        amqpTemplate.convertAndSend("learn.direct.exchange","test",new UserBean(1,"aa","aa"));
        UserBean userBean=new UserBean(1,"aa","aa");
//        String msg= JSON.toJSONString(userBean);
//        amqpTemplate.convertAndSend("learn_annotation_DirectExchange","direct1",msg);

        JSONObject json=new JSONObject();
        json.put("type","junitTest");
        json.put("status","success");
        json.put("desc","direct1");
        json.put("content",userBean);
        amqpTemplate.convertAndSend("learn_annotation_DirectExchange","direct1",json.toJSONString());
    }



    @Test
    public void testDirect2() {
        UserBean userBean=new UserBean(2,"bb","bb");
//        String msg= JSON.toJSONString(userBean);
//        amqpTemplate.convertAndSend("learn_annotation_DirectExchange","direct2",msg);
        JSONObject json=new JSONObject();
        json.put("type","junitTest");
        json.put("status","success");
        json.put("desc","direct2");
        json.put("content",userBean);
        amqpTemplate.convertAndSend("learn_annotation_DirectExchange","direct2",json.toJSONString());
    }






}
