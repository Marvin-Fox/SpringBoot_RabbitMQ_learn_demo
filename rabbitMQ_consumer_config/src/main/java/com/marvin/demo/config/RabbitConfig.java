package com.marvin.demo.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:rabbitConfig.properties")
public class RabbitConfig {

    @Value("${learn.direct.queue}")
    private String directQueue;
    @Value("${learn.topic.queue}")
    private String topicQueue;
    @Value("${learn.fanout.queue}")
    private String fanoutQueue;


    @Value("${learn.direct.exchange}")
    private String directExchange;
    @Value("${learn.topic.exchange}")
    private String topicExchange;
    @Value("${learn.fanout.exchange}")
    private String fanoutExchange;

    //创建队列
    @Bean("vipDirectQueue")
    public Queue getDirectQueue(){
        return new Queue(directQueue);
    }
    @Bean("vipTopicQueue")
    public Queue getTopicQueue(){
        return new Queue(topicQueue);
    }
    @Bean("vipFanoutQueue")
    public Queue getFanoutQueue(){
        return new Queue(fanoutQueue);
    }

    //创建交换机
    @Bean("vipDirectExchange")
    public DirectExchange getDirectExchange(){
        return new DirectExchange(directExchange);
    }
    @Bean("vipTopicExchange")
    public TopicExchange getTopicExchange(){
        return new TopicExchange(topicExchange);
    }
    @Bean("vipFanoutExchange")
    public FanoutExchange getFanoutExchange(){
        return new FanoutExchange(fanoutExchange);
    }

    //绑定
    @Bean
    public Binding bindingDirectQueue(@Qualifier("vipDirectQueue") Queue queue, @Qualifier("vipDirectExchange")DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("test");
    }

}
