package com.example.actionprice.user.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 연상훈
 * @group CheckForDuplicateUsernameGroup : username 중복 체크 검증그룹
 * @group SendVerificationCodeGroup : 인증코드 검증그룹
 * @created 2024-10-05 오후 10:57
 * @updated 2024-10-12 오후 1:48 : 전체 검증은 그냥 @Valid를 쓰기 때문에 불필요한 검증 그룹인 CheckVerificationCodeGroup 삭제
 * @updated 2024-10-20 오전 10:54 : 패스워드만 검증하기 위한 그룹 CheckValidityOfPasswordGroup 추가. 패스워드의 기존 검증을 정규식 패턴 검증으로 대체
 * @info 사용자가 user로 등록할 때 입력한 데이터를 담아올 객체입니다. 실질적으로 이게 dto 역할입니다.
 * @info 하나의 form이지만, 상황에 따라 검증이 필요한 영역이 다르기 때문에 검증을 위한 별도의 그룹을 지정하여 처리합니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterForm {

  @NotBlank(
      message = "사용자 이름은 필수입니다.",
      groups = {CheckForDuplicateUsernameGroup.class, SendVerificationCodeGroup.class})
  @Size(
      min = 6, max = 20, message = "사용자 이름은 6자 이상 20자 이하여야 합니다.",
      groups = {CheckForDuplicateUsernameGroup.class, SendVerificationCodeGroup.class})
  private String username;

  // 현재 정규식 : 8~20자, 영어+숫자+특수문자
  @Pattern(
      regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$",
      message = "비밀번호는 8~16자로 구성되며, 영어, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.",
      groups = {SendVerificationCodeGroup.class, CheckValidityOfPasswordGroup.class})
  private String password;

  @NotBlank(message = "이메일은 필수입니다.", groups = {SendVerificationCodeGroup.class})
  @Email(message = "유효한 이메일 주소를 입력하세요.", groups = {SendVerificationCodeGroup.class})
  private String email;

  @NotBlank(message = "인증코드는 필수입니다.")
  private String verificationCode;

  // 검증 시 구분을 위한 그룹입니다. 실질적인 기능은 없습니다.
  public interface CheckForDuplicateUsernameGroup {}
  public interface SendVerificationCodeGroup {}
  public interface CheckValidityOfPasswordGroup {}
}