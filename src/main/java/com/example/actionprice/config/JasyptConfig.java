package com.example.actionprice.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 인코딩 된 프로퍼티 자동 디코딩 설정
 * @author 연상훈
 * @created 2024-11-27 오후 4:53
 * @info 프로퍼티 값이 디코딩되도록 설정하고 싶으면 "ENC()"를 붙여줘야 함.
 * @info 테스트 겸 암호화가 가능하다는 것을 보여주는 용도로 db만 암호화 진행
 * @info 값을 암호화하는 것은 테스트에서 진행
 */
@Configuration
public class JasyptConfig {

  @Bean(name = "jasyptStringEncryptor")
  public StringEncryptor stringEncryptor() {
    String password = "password"; // 이렇게 하면 인코딩하는 의미가 없으니 이렇게 하면 안 됨. 지금은 그냥 기능 구현만 한 것
    // 인텔리제이의 경우 Run/Debug Configration에서 vm에 값을 넣어줌
    // ex) -jasypt.encryptor.key="..."
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(password);
    config.setAlgorithm("PBEWithMD5AndDES");
    config.setKeyObtentionIterations("1000");
    config.setPoolSize("1");
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setStringOutputType("base64");

    encryptor.setConfig(config);
    return encryptor;
  }

}
