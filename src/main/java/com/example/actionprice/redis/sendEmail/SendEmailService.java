package com.example.actionprice.redis.sendEmail;

/**
 * @author 연상훈
 * @created 2024-10-06 오후 9:17
 */
public interface SendEmailService {
  boolean sendVerificationEmail(String email) throws Exception;
  String checkVerificationCode(String email, String verificationCode);
}
