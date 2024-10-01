package com.example.actionprice.sendEmail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SendEmailComponent {
		
	@Value("${spring.mail.username}")
	private String senderEmail;

	private final JavaMailSender javaMailSender;
	
	public boolean sendSimpleMail(String receiverEmail, String subject, String content) throws Exception{
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(senderEmail);
		simpleMailMessage.setTo(receiverEmail);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(content);
		
		try {
			javaMailSender.send(simpleMailMessage);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;		
	}
	
	public boolean sendMimeMail(String receiverEmail, String subject, String content) throws Exception{
		MimeMessage message = javaMailSender.createMimeMessage();
		
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, false, "UTF-8");
		mimeMessageHelper.setFrom(senderEmail);
		mimeMessageHelper.setTo(receiverEmail); // 메일 수신자
		mimeMessageHelper.setSubject(subject); // 메일 제목
		mimeMessageHelper.setText(content); // 메일 본문 내용, HTML 여부
		
		try {
            javaMailSender.send(message);
        } catch (Exception e) {
        	e.printStackTrace();
			return false;
		}

		return true;
	}

}
