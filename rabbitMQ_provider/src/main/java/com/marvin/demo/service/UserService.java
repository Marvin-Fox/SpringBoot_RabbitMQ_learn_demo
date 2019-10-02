package com.marvin.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.marvin.demo.entity.UserBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

//    @Autowired
//    private AmqpTemplate amqpTemplate;
//
//    public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
//        this.amqpTemplate = amqpTemplate;
//    }

//    /**
//     *
//     * @param userBean
//     * @return 返回1是添加成功
//     * @throws Exception
//     */
//    public int add(UserBean userBean)throws Exception{
//        log.info("enter UserService-->add()~~~~~~~~~~~~~~~~~~~");
//        //写入DB操作省略。。。
//
//        //写入完成发送消息
//        JSONObject json=new JSONObject();
//        json.put("type","add");
//        json.put("status","success");
//        json.put("desc","添加用户");
//        json.put("content",userBean);
//        amqpTemplate.convertAndSend("learn.direct.exchange","test",json.toJSONString());
//        return 1;
//    }

    /**
     *
     * @param userBean
     * @return 返回1是添加成功
     * @throws Exception
     */
    public int add(UserBean userBean)throws Exception{
        log.info("enter UserService-->add()~~~~~~~~~~~~~~~~~~~");
        //写入DB操作省略。。。
        return 1;
    }
}
