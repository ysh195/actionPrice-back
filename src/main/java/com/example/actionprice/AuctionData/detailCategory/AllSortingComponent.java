package com.example.actionprice.AuctionData.detailCategory;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * api로부터 메인데이터를 추출하는 과정에서 변환하는 데에 쓰이는 객체들을 모아둔 컴포넌트
 * @author 연상훈
 * @created 2024-10-16 오후 3:01
 * @updated 2024-10-16 오후 3:01
 * @info : 추후 라이브서비스 때 scheduler에 넣기 위해 컴포넌트로 만듬
 * @see :
 * 표로 정리된 구글 시트 링크
 * https://docs.google.com/spreadsheets/d/1v83UPPUlm5D36oOcQTuxpi7xn1DBw-OsUCiFhKff2-M/edit?gid=0#gid=0
 */
@Component
@Getter
@Log4j2
public class AllSortingComponent {

    private final Map<String, String> market_code_map = Map.ofEntries(
            Map.entry("110001","서울가락"),
            Map.entry("110008","서울강서"),
            Map.entry("210001","부산엄궁"),
            Map.entry("210005","부산국제수산"),
            Map.entry("210009","부산반여"),
            Map.entry("220001","대구북부"),
            Map.entry("230001","인천남촌"),
            Map.entry("230003","인천삼산"),
            Map.entry("240001","광주각화"),
            Map.entry("240004","광주서부"),
            Map.entry("250001","대전오정"),
            Map.entry("250003","대전노은"),
            Map.entry("310101","수원"),
            Map.entry("310401","안양"),
            Map.entry("310901","안산"),
            Map.entry("311201","구리"),
            Map.entry("320101","춘천"),
            Map.entry("320201","원주"),
            Map.entry("320301","강릉"),
            Map.entry("330101","청주"),
            Map.entry("330201","충주"),
            Map.entry("340101","천안"),
            Map.entry("350101","전주"),
            Map.entry("350301","익산"),
            Map.entry("350402","정읍"),
            Map.entry("360301","순천"),
            Map.entry("370101","포항"),
            Map.entry("370401","안동"),
            Map.entry("371501","구미"),
            Map.entry("380101","창원팔용"),
            Map.entry("380201","울산"),
            Map.entry("380303","창원내서"),
            Map.entry("380401","진주")
    );

    private final Map<String, String> unit_code_map = Map.ofEntries(
            Map.entry("10","."),
            Map.entry("11","g"),
            Map.entry("12","kg"),
            Map.entry("13","ton(M/T)"),
            Map.entry("14","ml"),
            Map.entry("15","L"),
            Map.entry("70","."),
            Map.entry("71","g"),
            Map.entry("72","kg"),
            Map.entry("73","ton(M/T)")
    );

    private final Map<String, String> level_code_map = Map.ofEntries(
            Map.entry("10","."),
            Map.entry("11","특"),
            Map.entry("12","상"),
            Map.entry("13","보통"),
            Map.entry("14","4등"),
            Map.entry("15","5등"),
            Map.entry("16","6등"),
            Map.entry("17","7등"),
            Map.entry("18","8등"),
            Map.entry("19","등외"),
            Map.entry("1A","유기농산물"),
            Map.entry("1B","유기농산물(전환기)"),
            Map.entry("1C","무농약농산물"),
            Map.entry("1D","저농약농산물"),
            Map.entry("1E","유기축산물"),
            Map.entry("1F","유기축산물(전환기)"),
            Map.entry("1G","무항생제축산물"),
            Map.entry("1Y","혼합"),
            Map.entry("1Z","무등급"),
            Map.entry("70","."),
            Map.entry("71","자연산 특"),
            Map.entry("72","자연산 상"),
            Map.entry("73","자연산 보통"),
            Map.entry("74","자연산 하"),
            Map.entry("75","양식산 특"),
            Map.entry("76","양식산 상"),
            Map.entry("77","양식산 보통"),
            Map.entry("78","양식산 하"),
            Map.entry("79","등외"),
            Map.entry("7A","특"),
            Map.entry("7B","상"),
            Map.entry("7C","보통"),
            Map.entry("7D","하"),
            Map.entry("7Z","무등급")
    );

    private final Map<String, String> grand_category_map = Map.ofEntries(
            Map.entry("미곡류","농산물"),
            Map.entry("맥류","농산물"),
            Map.entry("두류","농산물"),
            Map.entry("잡곡류","농산물"),
            Map.entry("서류","농산물"),
            Map.entry("과실류","농산물"),
            Map.entry("수실류","농산물"),
            Map.entry("과일과채류","농산물"),
            Map.entry("과채류","농산물"),
            Map.entry("엽경채류","농산물"),
            Map.entry("근채류","농산물"),
            Map.entry("조미채소류","농산물"),
            Map.entry("양채류","농산물"),
            Map.entry("산채류","농산물"),
            Map.entry("농산물 종자류","농산물"),
            Map.entry("특용작물류","농산물"),
            Map.entry("버섯류","농산물"),
            Map.entry("인삼류","농산물"),
            Map.entry("약용작물류","농산물"),
            Map.entry("화목류","농산물"),
            Map.entry("초화류","농산물"),
            Map.entry("난류","농산물"),
            Map.entry("선인장/다육식물류","농산물"),
            Map.entry("숙근류","농산물"),
            Map.entry("구근류","농산물"),
            Map.entry("관엽식물류","농산물"),
            Map.entry("화훼종자류","농산물"),
            Map.entry("기타화훼","농산물"),
            Map.entry("산림종묘","농산물"),
            Map.entry("수목류","농산물"),
            Map.entry("목재류","농산물"),
            Map.entry("합판.보드류","농산물"),
            Map.entry("목재부산물","농산물"),
            Map.entry("임산부산물","농산물"),
            Map.entry("토.석류","농산물"),
            Map.entry("절화류","농산물"),
            Map.entry("관상수류","농산물"),
            Map.entry("생축(가축)류","축산물"),
            Map.entry("기타생축류","축산물"),
            Map.entry("국내산육류","축산물"),
            Map.entry("수입육류","축산물"),
            Map.entry("기타육류","축산물"),
            Map.entry("우유 및 유제품","축산물"),
            Map.entry("기타동물생산물","축산물"),
            Map.entry("비식용생산물","축산물"),
            Map.entry("배합사료","축산물"),
            Map.entry("양식용 어류사료","수산물"),
            Map.entry("식물성단미사료","농산물"),
            Map.entry("동물성단미사료","축산물"),
            Map.entry("조사료","축산물"),
            Map.entry("정액,수정란,종란","축산물"),
            Map.entry("활 해면어류","수산물"),
            Map.entry("활 해면패류","수산물"),
            Map.entry("활 해면갑각류","수산물"),
            Map.entry("활 해면연체류","수산물"),
            Map.entry("활 해면기타","수산물"),
            Map.entry("활 내수면어류","수산물"),
            Map.entry("활 내수면기타","수산물"),
            Map.entry("신선 해면어류","수산물"),
            Map.entry("신선 해면패류","수산물"),
            Map.entry("신선 해면갑각류","수산물"),
            Map.entry("신선 해면연체류","수산물"),
            Map.entry("신선 해면기타","수산물"),
            Map.entry("신선 해조류","수산물"),
            Map.entry("신선 내수면어류","수산물"),
            Map.entry("신선 내수면기타","수산물"),
            Map.entry("냉동 해면어류","수산물"),
            Map.entry("냉동 해면패류","수산물"),
            Map.entry("냉동 해면갑각류","수산물"),
            Map.entry("냉동 해면연체류","수산물"),
            Map.entry("냉동 해면기타","수산물"),
            Map.entry("냉동 내수면어류","수산물"),
            Map.entry("냉동 내수면기타","수산물"),
            Map.entry("건제품","수산물"),
            Map.entry("농림가공","농산물"),
            Map.entry("축산가공","축산물"),
            Map.entry("수산가공","수산물"),
            Map.entry("GMO농산물","농산물")
    );

    private final String[] old_market_name_Arr = {"강릉도매시장",  "구리도매시장",  "구미도매시장",  "목포농산시장",  "수원도매시장",  "순천도매시장",  "안동도매시장",  "안산도매시장",  "안양도매시장",  "여수농산시장",  "울산도매시장",  "원주도매시장",  "익산도매시장",  "전주도매시장",  "정읍도매시장",  "진주도매시장",  "천안도매시장",  "청주도매시장",  "춘천도매시장",  "충주도매시장",  "포항도매시장",  "광주각화도매시장",  "광주서부도매시장",  "대구북부도매시장",  "대전노은도매시장",  "대전오정도매시장",  "부산반여도매시장",  "부산엄궁도매시장",  "서울가락도매시장",  "서울강서도매시장",  "인천남촌도매시장",  "인천삼산도매시장",  "창원내서도매시장",  "창원팔용도매시장",  "부산국제수산물도매시장"};

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public LocalDate convertStrToLocalDate(String str) {
        if (str == null || str.length() == 0) {
            log.error("[class] AllSortingComponent > [method] convertStrToLocalDate > 입력된 값이 null입니다.");
            return LocalDate.parse("19991231", formatter);
        }
        return LocalDate.parse(str, formatter);
    }
}
