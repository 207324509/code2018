package cn.kenenjoy.netty;

import org.apache.activemq.transport.stomp.StompConnection;

import java.net.Socket;
import java.util.Date;

/**
 * 消息生产者
 * Created by hefa on 2017/12/5.
 */
public class TestProducer {
    public static void main(String[] args){

        try {
            // 建立Stomp协议的连接
            StompConnection connection = new StompConnection();
            Socket socket = new Socket("127.0.0.1", 61613);
            connection.open(socket);
            // 注意：协议版本可以是1.1，也可以是1.2
            connection.setVersion("1.2");
            // 用户名和密码
            connection.connect("admin", "admin");
            // 发送一条消息
            connection.send("test!", "127.0.0.1 " + new Date().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
