package com.example.eventticket.services;

import com.example.eventticket.dto.EmailDTO;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.*;

@Stateless
public class EmailService {

    @Resource(lookup = "java:/jms/queue/NotificationQueue")
    private Queue notificationQueue;

    @Resource
    private ConnectionFactory connectionFactory;

    public void sendEmail(EmailDTO emailDTO) {
        try (JMSContext context = connectionFactory.createContext()) {
            MapMessage message = context.createMapMessage();
            message.setStringProperty("to", emailDTO.getTo());
            message.setStringProperty("subject", emailDTO.getSubject());
            message.setStringProperty("body", emailDTO.getBody());

            context.createProducer().send(notificationQueue, message);
            System.out.println("Email message sent to NotificationQueue: " + emailDTO.getTo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

