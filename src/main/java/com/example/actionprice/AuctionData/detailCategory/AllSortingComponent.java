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

    /**
     * 시장코드 모음집
     * @author 연상훈
     * @created 2024-10-17 오후 1:34
     */
    private final Map<String, String> market_code_map = Map.ofEntries(
        Map.entry("1101", "서울"),
        Map.entry("2100", "부산"),
        Map.entry("2200", "대구"),
        Map.entry("2300", "인천"),
        Map.entry("2401", "광주"),
        Map.entry("2501", "대전"),
        Map.entry("2601", "울산"),
        Map.entry("3111", "수원"),
        Map.entry("3214", "강릉"),
        Map.entry("3211", "춘천"),
        Map.entry("3311", "청주"),
        Map.entry("3511", "전주"),
        Map.entry("3711", "포항"),
        Map.entry("3911", "제주"),
        Map.entry("3113", "의정부"),
        Map.entry("3613", "순천"),
        Map.entry("3714", "안동"),
        Map.entry("3814", "창원"),
        Map.entry("3145", "용인"),
        Map.entry("2701", "세종"),
        Map.entry("3112", "성남"),
        Map.entry("3138", "고양"),
        Map.entry("3411", "천안"),
        Map.entry("3818", "김해")
    );

    /**
     * 단위(g, kg, ton 등)코드 모음집
     * @author 연상훈
     * @created 2024-10-17 오후 1:34
     */
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

    /**
     * 상품 등급(quality) 모음집
     * @author 연상훈
     * @created 2024-10-17 오후 1:34
     */
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

    /**
     * 농산, 수산, 축산 분류 코드 모음집
     * @author 연상훈
     * @created 2024-10-17 오후 1:34
     */
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

    /**
     * 옛날 시장코드 모음집
     * @author 연상훈
     * @created 2024-10-17 오후 1:34
     */
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
