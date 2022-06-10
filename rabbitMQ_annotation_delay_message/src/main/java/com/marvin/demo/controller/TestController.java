package com.marvin.demo.controller;

import com.marvin.demo.service.producer.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ProducerService producerService;

    /**
     * 发送消息到延时队列
     *
     * @return
     */
    @RequestMapping(value = "send_delay_message",method = RequestMethod.GET)
    public void sendDelayMessage(){
        producerService.sendMsgToDelayQueue();
    }
}
