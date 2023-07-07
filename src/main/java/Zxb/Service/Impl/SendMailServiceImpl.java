package Zxb.Service.Impl;

import Zxb.Service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private JavaMailSender javaMailSender;
    //发邮件的四个必要信息
    //1.发送人
    private String from = "1115644521@qq.com";
    //2.接收人
    private String to = "1545193494@qq.com";
    //3.标题
    private String subject = "测试邮件";
    //4.正文
    private String text = "我发送了一封测试邮件";


    //简单邮件
    @Override
    public void sendMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from+"(渣渣辉)");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
