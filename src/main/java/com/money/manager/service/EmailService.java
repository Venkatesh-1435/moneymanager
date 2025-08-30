package com.money.manager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;
	
	@Value("${spring.mail.properties.mail.smtp.from}")
	private String fromEmail;
	
	public void sendMail(String to,String subject,String body) {
		try {
			SimpleMailMessage message=new SimpleMailMessage();
			message.setFrom(fromEmail);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(body);
			mailSender.send(message);
			
		}catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void sendMailWithAttachment(String to, String subject, String body, byte[] attachment, String filename) {
        try {
            // Create a new MimeMessage
            MimeMessage message = mailSender.createMimeMessage();

            // true = multipart (for attachment)
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);

            // Add attachment
            helper.addAttachment(filename, new org.springframework.core.io.ByteArrayResource(attachment));

            // Send mail
            mailSender.send(message);

            //System.out.println("Email sent successfully with attachment: " + filename);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
