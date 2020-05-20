package com.utfpr.ativadi.component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface EmailService {
        void sendEmail(List<String> to, String subject, String body);
}
