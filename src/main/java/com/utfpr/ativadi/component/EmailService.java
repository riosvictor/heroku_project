package com.utfpr.ativadi.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(List<String> to, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(String.join(",", to));

        msg.setSubject(subject);
        msg.setText(body);

        javaMailSender.send(msg);
    }
}