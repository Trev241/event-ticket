package com.example.eventticket.consumers;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
                @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/NotificationQueue")
        }
)
public class EmailServiceConsumer implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println("Message received");

        if (message instanceof MapMessage) {
            MapMessage mapMessage = (MapMessage) message;

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("toursite.service@gmail.com", "audvrgfdtorjzpxb");
                }
            });

            try {
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress("toursite.service@gmail.com"));
                mimeMessage.addRecipient(
                        jakarta.mail.Message.RecipientType.TO,
                        new InternetAddress(mapMessage.getStringProperty("to"))
                );
                mimeMessage.setSubject(mapMessage.getStringProperty("subject"));
                mimeMessage.setText(mapMessage.getStringProperty("body"));

                Transport.send(mimeMessage);
                System.out.println("Email sent successfully: " + mapMessage.getStringProperty("to"));
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
