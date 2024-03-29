# SpringBoot_RabbitMQ_learn_demo
springBoot整合rabbitMQ，包含生产者和消费者

注意：一般情况下我们会将创建和绑定关系放在消费者这方

为什么交换机创建、队列创建、绑定关系要在消费者这边创建？<br>
答：一般来说谁去使用这个资源就应该谁去创建。<br>
当然，我们把配置类copy到生产者上面，我们的生产者启动的时候不会报任何错误，是因为rabbitMQ的服务器上面去重复声明相同属性的元素的时候不会报任何错误直接就返回成功


## 项目结构：
- rabbitMQ_model：声明实体pojo对象，被其他引用，作为消息数据传递
- rabbitMQ_provider：生产者（为了模拟真实场景，在MVC中生产消息，rabbitMQ_consumer_config是这个的消费者）
- rabbitMQ_consumer_config：消费者(使用config类通过@Bean来在启动时向MQ服务器创建Queue\Exchange,和绑定关系)
- rabbitMQ_consumer_annotation：消费者（使用注解方式 @RabbitListener在消费类上声明创建Queue\Exchange,和绑定关系）自包含Junit测试类，可单独测试
- rabbitMQ_RabbitAdmin：使用RabbitAdmin来管理和创建Queue\Exchange\RoutingKey(单独为了学习RabbitAdmin创建，可单独自运行junit)
- rabbitMQ_annotation_full：springboot使用注解方式来完成一套完整的消息发送接收流程【包含配置、异常回调（保证消息发送完整性）、手动ACK（保证消息消费完整性）、消息发送、消息监听】 （@RabbitListener在消费类上声明创建Queue\Exchange,和绑定关系）自包含Junit测试类，可单独测试
- rabbitMQ_annotation_delay_message：注解方式来完成延时消息的实现【核心思想：死信队列、延时队列】涉及场景：订单超时关闭。自包含Controller接口请求测试类，可单独测试


## rabbitMQ_consumer_config
目的：通过配置类来创建和绑定MQ关系

spring配置类<br>
com.marvin.demo.config--》RabbitConfig

我们只需要返回Queue 和 Exchange，然后加上注解@Bean<br>
里面的所有@Bean都会在spring启动的时候初始化

初始化就意味着会在我们rabbitMQ服务器上面去创建这些队列

## rabbitMQ_consumer_annotation
目的：通过全注解方式来创建和绑定MQ关系

此model包含了junit测试：<br>
测试中写了消息生产来验证消费者是否会按预计消费（测试方法：先将这个model的springboot跑起来，在执行junit）<br>
TestSend_ConfirmCallback_ReturnCallback【消息发送确认机制】<br>
TestSendDirect【路由模式】<br>
TestSendFanout【发布订阅模式】<br>
TestSendTopic【主题模式】<br>


## rabbitMQ_provider
消息生产者，模拟真实场景通过mvc的请求来生产消息，目标是发送给rabbitMQ_consumer_config监听接收