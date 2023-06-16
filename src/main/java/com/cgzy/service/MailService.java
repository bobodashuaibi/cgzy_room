package com.cgzy.service;

public interface MailService {

    void sendMail(String to, String subject, int verifyCode);

    void sendMail(String to, int verifyCode);
}
