package org.dp.sf.jms.listener;

import java.util.UUID;

import javax.jms.JMSException;

import org.dp.sf.jms.config.JmsConfig;
import org.dp.sf.jms.model.Message;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MessageListener {

	private final JmsTemplate jmsTemplete;

	@JmsListener(destination = JmsConfig.MY_QUEUE)
	public void listen(@Payload Message msg, @Headers MessageHeaders headers, javax.jms.Message message) {
		// System.out.println("Got the Message");
		// System.out.println(msg);
		// System.out.println("*******************************");

	}

	@JmsListener(destination = JmsConfig.SEND_RECEIVE_QUEUE)
	public void listenAndReply(@Payload Message msg, @Headers MessageHeaders headers, javax.jms.Message message)
			throws JmsException, JMSException {
		Message replyMsg = Message.builder().id(UUID.randomUUID()).message("JMS-Received and Replying a Message...")
				.build();
		System.out.print("********");
		System.out.println(msg);
		jmsTemplete.convertAndSend(message.getJMSReplyTo(), replyMsg);

	}
}
