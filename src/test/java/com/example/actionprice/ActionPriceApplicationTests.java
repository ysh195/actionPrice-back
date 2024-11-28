package com.example.actionprice;

import lombok.extern.log4j.Log4j2;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 
 * @author : 연상훈
 * @created : 2024-10-11 오후 8:29
 * @updated 2024-10-17 오후 7:51
 * @info : 불필요한 테스트는 바로 삭제
 */

@SpringBootTest(classes = {ActionPriceApplication.class})
@Log4j2
class ActionPriceApplicationTests {

  @Test
  @Disabled
  void encryptTest(){
    String id = "root";
    String password = "9423";

    System.out.println(jasyptEncoding(id));
    System.out.println(jasyptEncoding(password));
  }

  public String jasyptEncoding(String value){
    String key = "password";
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    encryptor.setAlgorithm("PBEWithMD5AndDES");
    encryptor.setPassword(key);

    return encryptor.encrypt(value);
  }

}
