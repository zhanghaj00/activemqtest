package com.recevier;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import com.sender.Sender;

public class TextEnch implements MessageListener {

	@Override
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
		try {
			
				System.out.println("ActiveMq 开始发送的消息:::"+((TextMessage)arg0).getText());
			

			if(((StreamMessage)arg0).getStringProperty("FILE_NAME").equals("E:\test.txt")){
				
				byte[] b = new byte[2048];
				
	            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File("E:\result.zip")));
				while(((StreamMessage)arg0).readBytes(b)!=-1){
					
					out.write(b);
				}
				out.close();
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
