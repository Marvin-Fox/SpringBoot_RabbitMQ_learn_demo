package com.marvin.demo;

import com.alibaba.fastjson.JSON;
import com.marvin.demo.entity.UserBean;
import com.marvin.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationProvider.class)
public class TestSend {

    @Autowired
    private AmqpTemplate amqpTemplate;
    public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }
    @Test
    public void testDirect() {
//        amqpTemplate.convertAndSend("learn.direct.exchange","test",new UserBean(1,"aa","aa"));
        UserBean userBean=new UserBean(1,"aa","aa");
        String msg= JSON.toJSONString(userBean);
        amqpTemplate.convertAndSend("learn.direct.exchange","test",msg);
    }


    @Autowired
    private UserService userService;
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Test
    public void testDirectService() {
        try {
            userService.add(new UserBean(2,"bb","bb"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
