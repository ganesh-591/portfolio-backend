
package com.ganesh.portfolio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ganesh.portfolio.entity.Contact;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String recipientEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


public void sendContactNotification(Contact contact) {

    SimpleMailMessage mail = new SimpleMailMessage();

    mail.setFrom(recipientEmail);       // Sender's Gmail
    mail.setTo(recipientEmail);         // Your inbox
    mail.setReplyTo(contact.getEmail()); // Visitor's email

    mail.setSubject("New Portfolio Contact: " + contact.getSubject());

    mail.setText(
        "You received a new message from your portfolio!\n\n" +
        "Name: " + contact.getName() + "\n" +
        "Email: " + contact.getEmail() + "\n" +
        "Subject: " + contact.getSubject() + "\n\n" +
        "Message:\n" + contact.getMessage()
    );

    mailSender.send(mail);
}

}
