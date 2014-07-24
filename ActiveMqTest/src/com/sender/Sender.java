package com.sender;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {
    private static final int SEND_NUMBER = 5;

    public static void main(String[] args) {
        // ConnectionFactory �����ӹ�����JMS ������������
        ConnectionFactory connectionFactory;
        // Connection ��JMS �ͻ��˵�JMS Provider ������
        Connection connection = null;
        // Session�� һ�����ͻ������Ϣ���߳�
        Session session;
        // Destination ����Ϣ��Ŀ�ĵ�;��Ϣ���͸�˭.
        Destination destination;
        // MessageProducer����Ϣ������
        MessageProducer producer;
        // TextMessage message;
        // ����ConnectionFactoryʵ�����󣬴˴�����ActiveMq��ʵ��jar
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
        		"failover:(tcp://192.168.7.213:61616,tcp://192.168.7.252:61616,tcp://192.168.5.154:61616)?timeout=3000");
        try {
            // ����ӹ����õ����Ӷ���
            connection = connectionFactory.createConnection();
            // ����
            connection.start();
            // ��ȡ��������
            session = connection.createSession(Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);
            // ��ȡsessionע�����ֵxingbo.xu-queue��һ����������queue��������ActiveMq��console����
            destination = session.createQueue("mytestqueue");
            // �õ���Ϣ�����ߡ������ߡ�
            producer = session.createProducer(destination);
            // ���ò��־û����˴�ѧϰ��ʵ�ʸ�����Ŀ����
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // ������Ϣ���˴�д������Ŀ���ǲ��������߷�����ȡ
            sendMessage(session, producer);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }

    public static void sendMessage(Session session, MessageProducer producer)
            throws Exception {
       // for (int i = 1; i <= SEND_NUMBER; i++) {
            TextMessage message = session
                    .createTextMessage("ActiveMq ��ʼ���͵���Ϣcaoni" );
            producer.send(message);
            StreamMessage streamMessage = session.createStreamMessage();
            
            byte[] b = new byte[1024];
            int len = -1;
            //InputStream ins = Sender.class.getResourceAsStream("E:\test.txt");
            BufferedInputStream bins = new BufferedInputStream(new FileInputStream(new File("E://test.txt")));
            while ((len = bins.read(b)) !=-1) {    
            	                  
            	streamMessage.setStringProperty("FILE_NAME", "E:\test.txt");       
            	streamMessage.setStringProperty("COMMAND", "sending");       
            	streamMessage.clearBody();      
            	streamMessage.writeBytes(b);      
            	producer.send(streamMessage);       
            	}
            
            // ������Ϣ��Ŀ�ĵط�
            System.out.println("������Ϣ��" + "ActiveMq ���͵���Ϣ");
           
            bins.close();
           
       // }
    }
}