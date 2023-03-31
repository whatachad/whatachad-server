package com.whatachad.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_PROTOCOL = "mail.transport.protocol";
    private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String MAIL_SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    private static final String MAIL_DEBUG = "mail.debug";
    private static final String GOOGLE_SMTP_PASSWORD = "oorvxmugxbvpgbii";

    @Bean(name = "mailSender")
    public JavaMailSender getJavaMailSender() {
        Properties properties = new Properties();
        properties.put(MAIL_SMTP_AUTH, true);
        properties.put(MAIL_PROTOCOL, "smtp");
        properties.put(MAIL_SMTP_STARTTLS_ENABLE, true);
        properties.put(MAIL_SMTP_STARTTLS_REQUIRED, true);
        properties.put(MAIL_DEBUG, true);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("whatachad.master@gmail.com");
        mailSender.setPassword(GOOGLE_SMTP_PASSWORD);
        mailSender.setDefaultEncoding("utf-8");
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }
}
