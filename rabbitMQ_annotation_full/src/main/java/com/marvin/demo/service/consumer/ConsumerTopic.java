package com.marvin.demo.service.consumer;

import com.alibaba.fastjson.JSON;
//import com.marvin.demo.request.UserRequestModel;
import com.marvin.demo.entity.UserBean;
import com.marvin.demo.model.UserRequestModel;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

/**
 * topic模式消费者（手动ack模式下需要明文代码channel.basicAck(long deliveryTag, boolean multiple) throws IOException;）
 *
 * 注意topic模式，routingKey只能用“.”点来分隔【单词】,使用#或者*，进行模糊匹配路由
 * 重点：模糊匹配是按【单词】算的，“.”点是来分隔【单词】的，而不是字符，点之间的都被定义为一个【单词】
 *
 * 正确：routingKey = TopicQueue1.#(只有使用点符号分割才会被识别为模糊匹配)
 * 错误：routingKey = TopicQueue1_#(这个是完全匹配，不会识别为模糊匹配)
 *
 * 正确：routingKey = TopicQueue3.*.*.test
 * 错误：routingKey = TopicQueue3.*t(没有这样写的模糊匹配)
 */
@Slf4j
@Component
@PropertySource("classpath:rabbitConfig.properties")
public class ConsumerTopic {

    /**
     * 主题模式【routingKey = TopicQueue1.#】
     *
     * 监听junit测试场景：
     * 1、测试消息投递正常和异常情况（com.marvin.demo.TestSend_ConfirmCallback_ReturnCallback）
     * 2、测试消息routingKey的模糊投递模式
     * com.marvin.demo.TestSendTopic#testTopic1_a()；
     * com.marvin.demo.TestSendTopic#testTopic1_b()；
     *
     * @param userRequestModel：通过 @Payload 可以解析为POJO【我们需要的数据】
     * @param channel：获取          channel 目的是为了手动ack【此参数rabbitMQ会自动传入】
     * @param deliveryTag：通过      @Header 获取 deliveryTag 目的是为了手动ack【此参数rabbitMQ会自动传入】
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${learn_annotation_TopicQueue1}"),
            exchange = @Exchange(value = "${learn_annotation_TopicExchange}", type = "topic"),
            key = "TopicQueue1.#"
    ))
    public void processTopic1(@Payload UserRequestModel userRequestModel, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        log.info("Enter ConsumerTopic --> processTopic1()~~~~~~~~~~~~~~~~~~~");
        System.out.println("ConsumerTopic queue1 msg:" + userRequestModel);
        System.out.println("ConsumerTopic Object1 UserRequestModel:" + userRequestModel.toString());
        //获取真正的数据对象
        UserBean userBean = userRequestModel.getContent();
        System.out.println("ConsumerTopic Object1 UserBean:" + (ObjectUtils.isEmpty(userBean) ? null : userBean.toString()));
        //手动ack确认
        channel.basicAck(deliveryTag, false);
        //手动ack否认（消息被拒绝后，重回队列，然后不断重新发送给消费者，可能会导致死循环）
//        channel.basicNack(deliveryTag, false, true);
    }


    /**
     * 主题模式【routingKey = TopicQueue2.#】
     *
     * 监听junit测试场景：
     * 1、测试消息routingKey的模糊投递模式
     * com.marvin.demo.TestSendTopic#testTopic2_a()；
     *
     * @param userRequestModel：通过 @Payload 可以解析为POJO【我们需要的数据】
     * @param channel：获取          channel 目的是为了手动ack【此参数rabbitMQ会自动传入】
     * @param deliveryTag：通过      @Header 获取 deliveryTag 目的是为了手动ack【此参数rabbitMQ会自动传入】
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${learn_annotation_TopicQueue2}"),
            exchange = @Exchange(value = "${learn_annotation_TopicExchange}", type = "topic"),
            key = "TopicQueue2.#"
    ))
    public void processTopic2(@Payload UserRequestModel userRequestModel, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        log.info("Enter ConsumerTopic --> processTopic2()~~~~~~~~~~~~~~~~~~~");
        System.out.println("ConsumerTopic queue2 msg:" + userRequestModel);
        System.out.println("ConsumerTopic Object2 UserRequestModel:" + userRequestModel.toString());
        //获取真正的数据对象
        UserBean userBean = userRequestModel.getContent();
        System.out.println("ConsumerTopic Object2 UserBean:" + (ObjectUtils.isEmpty(userBean) ? null : userBean.toString()));
        //手动ack确认
        channel.basicAck(deliveryTag, false);
    }


    /**
     * 主题模式【routingKey = TopicQueue3.*】
     *
     * 监听junit测试场景：
     * 1、测试消息routingKey的模糊投递模式
     * com.marvin.demo.TestSendTopic#testTopic3_a()；
     * com.marvin.demo.TestSendTopic#testTopic3_b()；
     * com.marvin.demo.TestSendTopic#testTopic3_c();
     *
     * @param userRequestModel：通过 @Payload 可以解析为POJO【我们需要的数据】
     * @param channel：获取          channel 目的是为了手动ack【此参数rabbitMQ会自动传入】
     * @param deliveryTag：通过      @Header 获取 deliveryTag 目的是为了手动ack【此参数rabbitMQ会自动传入】
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${learn_annotation_TopicQueue3}"),
            exchange = @Exchange(value = "${learn_annotation_TopicExchange}", type = "topic"),
            key = "TopicQueue3.*"
    ))
    public void processTopic3(@Payload UserRequestModel userRequestModel, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        log.info("Enter ConsumerTopic --> processTopic3()~~~~~~~~~~~~~~~~~~~");
        System.out.println("ConsumerTopic queue3 msg:" + userRequestModel);
        System.out.println("ConsumerTopic Object3 UserRequestModel:" + userRequestModel.toString());
        //获取真正的数据对象
        UserBean userBean = userRequestModel.getContent();
        System.out.println("ConsumerTopic Object3 UserBean:" + (ObjectUtils.isEmpty(userBean) ? null : userBean.toString()));
        //手动ack确认
        channel.basicAck(deliveryTag, false);
    }


}
