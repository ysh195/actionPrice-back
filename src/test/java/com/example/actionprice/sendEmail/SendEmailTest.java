package com.example.actionprice.sendEmail;

import com.example.actionprice.redis.sendEmail.SendEmailService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class SendEmailTest {

  @Autowired
  private SendEmailService sendEmailService;

  @Test
  @Disabled
  void emailSendTest() throws Exception {
    sendEmailService.sendVerificationEmail("wqrpn@xmxzcvpr.com");
  }

}
