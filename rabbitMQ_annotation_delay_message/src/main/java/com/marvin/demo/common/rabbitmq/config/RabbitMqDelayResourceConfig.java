package com.marvin.demo.common.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * 延迟队列配置【创建所需交换机、队列，并且绑定】
 */
@Configuration
@PropertySource("classpath:rabbitConfig.properties")
public class RabbitMqDelayResourceConfig {

    //读取资源文件信息
    @Autowired
    private Environment env;


    //定义订单交换机【就是一个普通的交换机】
    @Bean("orderExchange")
    public Exchange orderExchange() {
        return ExchangeBuilder.topicExchange(env.getProperty("learn_annotation_delay_OrderTopicExchange")).build();
    }

    //定义延时队列【订单超时自动关闭队列】
    /**
     * 定义延时队列 And 设置死信消息的转发配置【方法一】
     *
     * 1、创建一个延时队列【该队列不能有消费者，队列消息才会是死信消息】
     * 2、设置该队列产生死信时发送到指定死信交换机
     * 3、死信队列的routing key
     *
     * @return
     */
    @Bean("delayOrderCancelQueue")
    public Queue delayOrderCancelQueue() {
        return QueueBuilder.durable(env.getProperty("learn_annotation_delay_OrderTopictQueue_DelayCancelQueue"))
                // x-dead-letter-exchange 声明了队列里的死信消息转发到的DLX名称，
                .withArgument("x-dead-letter-exchange", env.getProperty("learn_annotation_delay_DirectExchange_DLX"))
                // x-dead-letter-routing-key 声明了这些死信消息在转发时携带的 routing-key 名称。
                .withArgument("x-dead-letter-routing-key", env.getProperty("learn_annotation_delay_DirectQueue_DLX_Queue_key")).build();
    }
//    /**
//     * 定义延时队列 And 设置死信消息的转发配置【方法二】
//     */
//    @Bean("delayQueue")
//    public Queue delayQueue() {
//        //设置死信交换机和路由key
//        Map<String, Object> params = new HashMap<>();
//        // x-dead-letter-exchange 声明了队列里的死信转发到的DLX名称，
//        params.put("x-dead-letter-exchange", env.getProperty("learn_annotation_delay_DirectExchange_DLX"));
//        // x-dead-letter-routing-key 声明了这些死信在转发时携带的 routing-key 名称。
//        params.put("x-dead-letter-routing-key", env.getProperty("learn_annotation_delay_DirectQueue_DLX_Queue_key"));
//        return new Queue(env.getProperty("learn_annotation_delay_OrderTopictQueue_DelayCancelQueue"), true, false, false, params);
//    }

    //绑定延时队列与订单交换机
    @Bean("delayOrderCancelBinding")
    public Binding delayOrderCancelBinding(@Qualifier("orderExchange")Exchange orderExchange, @Qualifier("delayOrderCancelQueue")Queue delayOrderCancelQueue) {
        return BindingBuilder.bind(delayOrderCancelQueue).to(orderExchange).with(env.getProperty("learn_annotation_delay_OrderTopictQueue_DelayCancelQueue_key")).noargs();
    }



    //定义死信交换机
    @Bean("dlxExchange")
    public Exchange dlxExchange() {
        return ExchangeBuilder.directExchange(env.getProperty("learn_annotation_delay_DirectExchange_DLX")).build();
    }

    //定义死信队列
    @Bean("dlxQueue")
    public Queue dlxQueue() {
        return QueueBuilder.durable(env.getProperty("learn_annotation_delay_DirectQueue_DLX_Queue")).build();
    }

    //绑定死信队列与交换机
    @Bean("dlxBinding")
    public Binding dlxBinding(@Qualifier("dlxExchange")Exchange dlxExchange, @Qualifier("dlxQueue")Queue dlxQueue) {
        return BindingBuilder.bind(dlxQueue).to(dlxExchange).with(env.getProperty("learn_annotation_delay_DirectQueue_DLX_Queue_key")).noargs();
    }
}
