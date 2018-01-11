package cn.kenenjoy.activemq.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 测试使用JMS API连接ActiveMQ
 * Created by hefa on 2017/12/6.
 */
public class JMSConsumer {
    public static void main(String[] args) {
        // 定义JMS-Active连接信息
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61618");
        Session session = null;
        Destination sendQueue = null;
        Connection connection = null;
        try {
            // 进行连接
            connection = connectionFactory.createConnection();
            connection.start();
            // 建立会话(设置为自动ACK)
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 建立Queue（如果已存在，就不会重复创建）
            sendQueue = session.createQueue("test!");
            // 建立消息发送者对象
            MessageConsumer consumer = session.createConsumer(sendQueue);
            consumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    // 接收到消息后，不需要手工发送ACK了。
                    System.out.println("Message = " + message);
                }
            });

            synchronized (JMSConsumer.class) {
                try {
                    JMSConsumer.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 关闭
            consumer.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
