package com.kh.bbs.web.form.postboard;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SaveForm {
  @NotBlank(message = "제목은 필수 입니다.")
  @Size(min=1,max=33,message = "제목은 33자를 초과할 수 없습니다.")
  private String title;

  @NotBlank(message = "내용은 필수 입니다.")
  private String content;

  private String nickname;
}
