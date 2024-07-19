package com.youtube.crud.service.interfaces;

import com.youtube.crud.model.Mail;


public interface MailService {
    void sendEmail(Mail mail);

    void sendEmail(String to, String subject, String text);
}

