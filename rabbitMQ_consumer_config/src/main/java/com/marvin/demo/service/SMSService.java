package com.marvin.demo.service;

import com.marvin.demo.entity.UserBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SMSService {
    /**
     * 真正发送短信的工作
     * @param userBean
     */
    public void doSendSMS(UserBean userBean){
        log.info("enter SMSService-->doSendSMS()~~~~~~~~~~~~~~~~~~~");
        System.out.println("发送短信:"+userBean.toString());
    }
}
