package cn.kenenjoy.activemq.stomp;

import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;

import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;

/**
 * Created by hefa on 2017/12/5.
 */
public class TestConsumer {
    public static void main(String[] args) {
        try {
            // 建立Stomp协议的连接
            StompConnection connection = new StompConnection();
            Socket socket = new Socket("127.0.0.1", 61613);
            connection.open(socket);
            // 注意：协议版本可以是1.1，也可以是1.2
            connection.setVersion("1.2");
            // 用户名和密码
            connection.connect("admin", "admin");
            String ack = "client";
            // 接收消息
            connection.subscribe("test!", ack);
            for (; ; ) {

                StompFrame frame = null;
                try {
                    // 注意：如果没有接收消息到消息，
                    // 那么这个消费者线程会停在这里，直到本次等待超时。
                    frame = connection.receive();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    continue;
                }
                // 打印本地收到的消息
                System.out.println("frame.getAction() = " + frame.getAction());
                Map<String, String> headers = frame.getHeaders();
                String message_id = headers.get("message-id");
                System.out.println("frame.getBody() = " + frame.getBody());
                System.out.println("frame.getCommandId() = " + frame.getCommandId());
                // 在ack是client标记的情况下，确认消息
                if ("client".equals(ack)) {
                    connection.ack(message_id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
