package com.marvin.demo.ControllerProvider;

import com.alibaba.fastjson.JSONObject;
import com.marvin.demo.entity.UserBean;
import com.marvin.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Provider {

    @Autowired
    private AmqpTemplate amqpTemplate;
    public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Autowired
    private UserService userService;
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 添加用户，成功后通过MQ向用户发送短信
     * @return
     */
    @RequestMapping("/adduser")
    public String addUser(){
        log.info("enter Provider-->addUser()~~~~~~~~~~~~~~~~~~~");
        UserBean userBean=new UserBean(2,"bb","bb");

        try {
            //添加到DB
            int number=userService.add(userBean);
            //判断是否添加成功
            if(number!=0){
                log.info("enter Provider-->添加成功~~~~~~~~~~~~~~~~~~~");
                //添加成功后发送MQ消息
                log.info("enter Provider-->准备发送MQ~~~~~~~~~~~~~~~~~~~");
                JSONObject json=new JSONObject();
                json.put("type","add");
                json.put("status","success");
                json.put("desc","添加用户");
                json.put("content",userBean);
                amqpTemplate.convertAndSend("learn.direct.exchange","test",json.toJSONString());
                log.info("enter Provider-->发送MQ完毕~~~~~~~~~~~~~~~~~~~");
                return "添加成功，稍后会给您发送短信";
            }
            log.info("enter Provider-->添加失败~~~~~~~~~~~~~~~~~~~");
            return "添加失败";

        } catch (Exception e) {
            e.printStackTrace();
            log.info("enter Provider-->添加失败，出现异常~~~~~~~~~~~~~~~~~~~");
            return "添加失败";
        }
    }

}
