package com.marvin.demo.common.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ 消息回调
 */
@Slf4j
@Component
public class MessageCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

//    final private MQErrorLogRepository mqErrorLogRepository;
//
//    MessageCallBack(MQErrorLogRepository mqErrorLogRepository) {
//        this.mqErrorLogRepository = mqErrorLogRepository;
//    }
//

    /**
     * 回调函数: confirm确认：当消息发送到broker服务器交换机【exchange】时，该方法被调用.
     * 1.如果消息没有到exchange,则 ack=false
     * 2.如果消息到达exchange,则 ack=true
     *
     * @param correlationData：消息的唯一标识
     * @param ack：确认结果【true：发送成功｜false：发送失败】
     * @param cause：失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            //投递成功
            log.info("【成功】【confirmCallback】Client消息发送到Exchange ==================== correlationData【{}】, cause: 【{}】", correlationData, cause);
        } else {
            //投递失败
            log.error("【失败】【confirmCallback】Client消息发送到Exchange ==================== correlationData【{}】, cause: 【{}】", correlationData, cause);
            //失败记录写入DB
//            MQErrorLog mqErrorLog = new MQErrorLog();
//            mqErrorLog.setErrorType(MQConstants.ERROR_TYPE_CALL_BACK_CONFIRM);
//            mqErrorLog.setExtraData(correlationData.getId());
//            mqErrorLog.setCreateTime(new Date());
//            mqErrorLogRepository.save(mqErrorLog);
            //（nack）失败则进行具体的后续操作:重试 或者补偿等手段
        }
    }

    /**
     * 回调函数: return确认：当消息从交换机（Exchange）路由到队列（Queue）失败时，该方法被调用【若成功，则不调用】
     * 需要注意的是：该方法调用后，MsgSendConfirmCallBack中的confirm方法也会被调用，且ack = true
     *
     * @param message：传递的消息主体
     * @param replyCode：问题状态码
     * @param replyText：问题描述
     * @param exchange：使用的交换器
     * @param routingKey：使用的路由键
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        Integer messageLength = message.toString().length();
        if (messageLength < 500) {
            log.error("【失败】【returnCallback】Exchange消息发送到Queue ==================== messge:【{}】replyCode:【{}】replyText:【{}】exchange:【{}】routingKey:【{}】",
                    message, replyCode, replyText, exchange, routingKey);
        }else{
            log.error("【失败】【returnCallback】Exchange消息发送到Queue ==================== replyCode:【{}】replyText:【{}】exchange:【{}】routingKey:【{}】",
                     replyCode, replyText, exchange, routingKey);
        }
        //临时使用交换机名称进行过滤(排除不需要写DB日志的消息)
        if (!exchange.toUpperCase().contains("NOT_WRITE_DB_LOG")) {
            Map<String, Object> extraData = new HashMap<>();
            extraData.put("replyCode", replyCode);
            extraData.put("replyText", replyText);
            extraData.put("exchange", exchange);
            extraData.put("routingKey", routingKey);
            //失败记录写入DB
//            MQErrorLog mqErrorLog = new MQErrorLog();
//            mqErrorLog.setErrorType(MQConstants.ERROR_TYPE_CALL_BACK_RETURN);
//            mqErrorLog.setMessage(message.toString().substring(0, messageLength >= 2000 ? 2000 : messageLength));
//            mqErrorLog.setExtraData(JSON.toJSONString(extraData));
//            mqErrorLog.setCreateTime(new Date());
//            mqErrorLogRepository.save(mqErrorLog);
            //失败则进行具体的后续操作:重试 或者补偿等手段。。。
        }


    }
}
