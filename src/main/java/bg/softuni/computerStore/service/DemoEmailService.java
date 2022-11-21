package bg.softuni.computerStore.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class DemoEmailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    public DemoEmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    public void sendRegistrationEmail(String userEmail, String userName) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("computer@store.com");
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setSubject("Welcome to ComputerStore");
            mimeMessageHelper.setText(userName, true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateMessageContent(String userName){
        Context ctx = new Context();  //org.thymeleaf.context;
        ctx.setVariable("userRegistrationModel.username", userName);

        return templateEngine.process("/user/auth-registerUser", ctx);
    }
}
