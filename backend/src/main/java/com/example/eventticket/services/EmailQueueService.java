package com.example.eventticket.services;

import com.example.eventticket.dto.EmailDTO;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;

@Stateless
public class EmailQueueService {

    @Resource(lookup = "jms/NotificationQueue")
    private Queue notificationQueue;

    @Resource
    private ConnectionFactory connectionFactory;

    public void sendToNotificationQueue(EmailDTO emailDTO) {
        try (JMSContext context = connectionFactory.createContext()) {
            ObjectMessage message = context.createObjectMessage(emailDTO);
            context.createProducer().send(notificationQueue, message);
            System.out.println("Email message sent to NotificationQueue: " + emailDTO.getTo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

