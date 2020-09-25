package com.utfpr.ativadi.component;

import java.util.List;

public interface EmailService {
        void sendEmail(List<String> to, String subject, String body);
}
