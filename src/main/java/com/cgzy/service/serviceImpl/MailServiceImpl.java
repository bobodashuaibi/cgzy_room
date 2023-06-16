package com.cgzy.service.serviceImpl;
import com.cgzy.service.MailService;
import com.cgzy.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    JavaMailSenderImpl mailSender;

    /**
     * 用来发送模版邮件
     */

    @Override
    public void sendMail(String to, String subject, int verifyCode) {
        //创建邮件正文

        //创建邮件
        SimpleMailMessage message = new SimpleMailMessage ();

        message.setFrom ("1940763757@qq.com"); // 设置发件人Email

        message.setTo (to);//设定收件人Email

        message.setSubject (subject); //设置邮件主题

        String verify = "你好，你正在找回<成工职院寝室管理系统>账号的密码!\n您的验证码:"
                + verifyCode +
                ",有效时间2分钟,\n如果不是你本人操作,请不要把验证码给别人！";
        message.setText (verify);
        try {
            mailSender.send (message);
            log.info ("简单邮件已经发送。");
        } catch (Exception e) {
            log.error ("发送简单邮件时发生异常！", e);

        }
    }

    /**
     * 发送邮件
     * Const.SUBJECT_NAME 邮件主题
     *
     * @param to         邮件收件人
     * @param verifyCode 邮件验证码 内容
     */
    @Override
    public void sendMail(String to, int verifyCode) {

        this.sendMail (to, "cgzy寝室系统密码重置", verifyCode);
    }
}

