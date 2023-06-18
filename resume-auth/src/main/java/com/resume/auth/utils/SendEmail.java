package com.resume.auth.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.Random;

/*
 *@filename: SendEmail
 *@author: lyh
 *@date:2023/4/1 23:56
 *@version 1.0
 *@description TODO
 */
@Slf4j
public class SendEmail {
    public static int send(String email) throws GeneralSecurityException {
        // 收件人电子邮箱
        String to = email;
        // 发件人电子邮箱
        String from = "1470511515@qq.com";
        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.qq.com"; //QQ 邮件服务器
        // 获取系统属性
        Properties properties = System.getProperties();
        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");//加上后解决了465端口报错的问题
        // 关于QQ邮箱，还要设置SSL加密，加上以下代码即可
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("1470511515@qq.com", "aqlxjksdxclehifh"); //发件人邮件用户名、密码
            }
        });

        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);
            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));
            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Set Subject: 头部头字段
            // message.setSubject("This is the Subject Line!");
            message.setSubject("智能简历解析系统验证码");
            // 设置消息体
            Random random = new Random();
            String number="";
            for (int i = 0; i < 6; i++) {
                int rand=random.nextInt(10);
                number+=rand;
            }
            String text="尊敬的用户，你的验证码为:"+number+"\n"+"请注意验证码有效时长为10分钟";
            message.setText(text);
            // 发送消息
            Transport.send(message);
            return Integer.parseInt(number);
        }catch (MessagingException mex) {
            log.error("验证码发送失败",mex);
            return -1;
        }
    }
}
