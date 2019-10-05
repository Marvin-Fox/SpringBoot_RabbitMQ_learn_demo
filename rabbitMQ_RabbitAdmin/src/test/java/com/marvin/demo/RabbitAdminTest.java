package com.marvin.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * RabbitAdmin用于创建、绑定、删除队列与交换机，发送消息等
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationRabbitAdmin.class)
public class RabbitAdminTest {

    @Autowired
    private RabbitAdmin rabbitAdmin;
    public void setRabbitAdmin(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    /**
     * 创建绑定Direct路由模式
     * routingKey 完全匹配
     * Binding(String destination, Binding.DestinationType destinationType, String exchange, String routingKey, Map<String, Object> arguments)
     * Binding(目的地, 目的地类型, exchange, routingKey, 参数)
     */
    @Test
    public void testDirect() {
        //切记命名不能重复复
        final String QUEUE_NAME="test.direct.queue";
        final String EXCHANGE_NAME="test.direct";
        //创建队列
        Queue directQueue=new Queue(QUEUE_NAME);
        rabbitAdmin.declareQueue(directQueue);
        //创建Direct交换机
        DirectExchange directExchange=new DirectExchange(EXCHANGE_NAME);
        rabbitAdmin.declareExchange(directExchange);
        //绑定交换机和队列（注意：绑定的时候，一定要确认绑定的双方都是存在的，否则会报IO异常，NOT_FOUND）
        Binding directBinding=new Binding(QUEUE_NAME, Binding.DestinationType.QUEUE, EXCHANGE_NAME, "mq.direct", null);
        rabbitAdmin.declareBinding(directBinding);
    }

    /**
     * 创建绑定Topic主题模式\通配符模式
     * routingKey 模糊匹配
     * BindingBuilder.bind(queue).to(exchange).with(routingKey)
     */
    @Test
    public void testTopic() {
        rabbitAdmin.declareQueue(new Queue("test.topic.queue", true, false, false));
        rabbitAdmin.declareExchange(new TopicExchange("test.topic", true, false));
        //如果注释掉上面两句实现声明，直接进行下面的绑定竟然不行，该版本amqp-client采用的是5.1.2,将上面两行代码放开，则运行成功
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("test.topic.queue", true, false, false)).to(new TopicExchange("test.topic", true, false)).with("mq.topic"));
    }

    /**
     * 创建绑定Fanout发布订阅模式
     * BindingBuilder.bind(queue).to(FanoutExchange)
     */
    @Test
    public void testFanout() {
        rabbitAdmin.declareQueue(new Queue("test.fanout.queue", true, false, false, null));
        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", true, false, null));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("test.fanout.queue", true, false, false)).to(new FanoutExchange("test.fanout", true, false)));
        rabbitAdmin.purgeQueue("test.direct.queue", false);//清空队列消息
    }
}
