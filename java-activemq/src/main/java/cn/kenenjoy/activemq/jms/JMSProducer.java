package cn.kenenjoy.activemq.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 测试使用JMS API连接ActiveMQ
 * Created by hefa on 2017/12/6.
 */
public class JMSProducer {
    public static void main(String[] args) {
        // 定义JMS-Active连接信息
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        Session session = null;
        Destination sendQueue = null;
        Connection connection = null;
        try {
            // 进行连接
            connection = connectionFactory.createConnection();
            connection.start();
            // 建立会话(设置一个带有事务特性的会话)
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
            // 建立Queue（如果已存在，就不会重复创建）
            sendQueue = session.createQueue("test!");
            // 建立消息发送者对象
            MessageProducer sender = session.createProducer(sendQueue);
            TextMessage outMessage = session.createTextMessage();
            outMessage.setText("这是要发送的消息内容");
            // 发送(JMS是支持事务的)
            sender.send(outMessage);
            // 设置持久化消息和非持久化消息
            sender.setDeliveryMode(DeliveryMode.PERSISTENT);// 持久化消息，消费成功后才被处理
            sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);// 非持久化消息，宕机情况下会丢失
            session.commit();
            // 关闭
            sender.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
