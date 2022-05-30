package com.marvin.demo.common.rabbitmq.config;

import com.marvin.demo.common.rabbitmq.MessageCallBack;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Configuration: 说明是一个配置类
 * @EnableRabbit: 开启基于注解的RabbitMQ模式
 */
@Configuration
@EnableRabbit
public class RabbitMqConfig {

    /**
     * RabbitMQ的使用入口
     *
     * @param connectionFactory ： 连接信息
     * @param messageCallBack ：自定义回调类
     * @return
     */
    @Bean
    //定义生命周期为prototype
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageCallBack messageCallBack) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //指定消息转换器
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        //confirm确认：当消息发送到broker服务器交换机【exchange】时，该方法被调用.
        rabbitTemplate.setConfirmCallback(messageCallBack);

        //return确认：触发setReturnCallback回调必须设置mandatory=true, 否则Exchange没有找到Queue就会丢弃掉消息, 而不会触发回调
        rabbitTemplate.setMandatory(true);
        //return确认：当消息从交换机（Exchange）路由到队列（Queue）失败时，该方法被调用【若成功，则不调用】
        rabbitTemplate.setReturnCallback(messageCallBack);
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}

