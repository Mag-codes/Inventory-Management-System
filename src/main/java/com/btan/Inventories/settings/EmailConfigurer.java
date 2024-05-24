package com.btan.Inventories.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailConfigurer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine thymeleafTemplateEngine;

    public void sendRichTextEmail(String recipient, String emailSubject, String htmlBody) throws MessagingException {
        MimeMessage emailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(emailMessage, true);
        messageHelper.setTo(recipient);
        messageHelper.setSubject(emailSubject);
        messageHelper.setText(htmlBody, true);

        mailSender.send(emailMessage);
    }

    public String generateHtmlContent(String header, String bodyText, String url) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("header", header);
        thymeleafContext.setVariable("bodyText", bodyText);
        return thymeleafTemplateEngine.process("email", thymeleafContext);
    }

    public void sendConfirmaitonEmail(String recipient, String emailSubject, String header, String bodyText, String url)
            throws MessagingException {
        String htmlContent = generateHtmlContent(header, bodyText, url);
        sendRichTextEmail(recipient, emailSubject, htmlContent);
    }
}
