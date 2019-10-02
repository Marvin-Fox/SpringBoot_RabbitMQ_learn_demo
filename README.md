# SpringBoot_RabbitMQ_learn_demo
springBoot整合rabbitMQ，包含生产者和消费者

## 项目结构：
rabbitMQ_consumer：消费者

rabbitMQ_provider：生产者


为什么交换机创建、队列创建、绑定关系要在消费者这边创建？

答：一般来说谁去使用这个资源就应该谁去创建。
当然，我们把配置类copy到生产者上面，我们的生产者启动的时候不会报任何错误，是因为rabbitMQ的服务器上面去重复声明相同属性的元素的时候不会报任何错误直接就返回成功


## spring配置类
com.marvin.demo.config--》RabbitConfig

我们只需要返回Queue 和 Exchange，然后加上注解@Bean
里面的所有@Bean都会在spring启动的时候初始化

初始化就意味着会在我们rabbitMQ服务器上面去创建这些队列
