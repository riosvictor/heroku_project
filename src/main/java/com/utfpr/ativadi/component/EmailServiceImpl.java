package com.utfpr.ativadi.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(List<String> to, String subject, String body) {

        try {
            SimpleMailMessage msg = new SimpleMailMessage();

            msg.setTo(String.join(",", to));

            msg.setSubject(subject);
            msg.setText(body);

            javaMailSender.send(msg);
        }catch (MailException e){
            e.printStackTrace();
        }
    }
}