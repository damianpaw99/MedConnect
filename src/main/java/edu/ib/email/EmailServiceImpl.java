package edu.ib.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl{


    private JavaMailSender emailSender;

    @Autowired
    EmailServiceImpl(@Qualifier("getJavaMailSender") JavaMailSender emailSender){
        this.emailSender=emailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreplymedconnect@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
    public void sendPasswordReset(String to,String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreplymedconnect@gmail.com");
        message.setTo(to);
        message.setSubject("Resetowanie hasła MedConnect");
        String text="Twój token do resetowania hasła na stronie MedConnect to:\n"+token+"\nJest on ważny przez 1h. Jeśli to nie Ty zgłosiłeś potrzeby zmiany hasła zignoruj tą wiadomość";
        message.setText(text);
        emailSender.send(message);
    }
}
