package org.dp.sf.jms.sender;

import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Session;

import org.dp.sf.jms.config.JmsConfig;
import org.dp.sf.jms.model.Message;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MessageSender {

	private final JmsTemplate jmsTemplate;
	private final ObjectMapper objectMapper;

	@Scheduled(fixedRate = 2000)
	public void sendMessage() {
		// System.out.println("Sending Message...");

		Message msg = Message.builder().id(UUID.randomUUID()).message("JMS-Message...").build();

		jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, msg);

		// System.out.println("... Message Sent.");
	}

	@Scheduled(fixedRate = 2000)
	public void sendAndReceiveMessage() throws JMSException {
		System.out.println("Sending Message...");

		Message msg = Message.builder().id(UUID.randomUUID()).message("JMS-SENDing a Message...").build();

		javax.jms.Message receivedMsg = jmsTemplate.sendAndReceive(JmsConfig.SEND_RECEIVE_QUEUE, new MessageCreator() {

			@Override
			public javax.jms.Message createMessage(Session session) throws JMSException {
				javax.jms.Message sendMsg = null;
				try {
					sendMsg = session.createTextMessage(objectMapper.writeValueAsString(msg));
					sendMsg.setStringProperty("_type", "org.dp.sf.jms.model.Message");
				} catch (JsonProcessingException | JMSException e) {
					throw new JMSException("Failed........");
					// e.printStackTrace();
				}
				return sendMsg;
			}
		});

		System.out.println(receivedMsg.getBody(String.class));
	}
}
