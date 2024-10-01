package com.example.actionprice.hello;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author 연상훈
* @created 24/10/01 14:20
* @updated 24/10/01 14:20
* @param baseAuctionUrl = api를 사용하기 위한 기본 경로. 해당 값을 application.properties에서 불러옴
* @param auctionEncodedKey = api를 사용하기 위한 기본 키(인코딩된 상태). 해당 값을 application.properties에서 불러옴
* @param WebClient = 중복되는 객체 생성을 피하기 위해 
* @info {api의 간편한 재사용을 위해 AuctionDataFetcher 클래스를
* 만들어서 @Component 로 등록하여 중복되는 객체 생성을 피하고, 메모리 사용을 줄임.
* 그리고 내장된 메서드를 통해 값을 반환함.
* 이게 날짜별로 데이터 호출이라 확실히 데이터를 우리가 관리할 필요가 있겠음} 
*/
@RestController
@RequestMapping("/hello") 
public class helloController {
    
    @GetMapping("/getTest")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("Test successful");
    }
}
