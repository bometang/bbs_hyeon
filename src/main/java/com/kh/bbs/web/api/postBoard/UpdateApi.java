package com.kh.bbs.web.api.postBoard;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateApi {
  @NotBlank(message = "제목은 필수 입니다.")
  private String title;           //게시글 제목
  @NotBlank(message = "내용은 필수 입니다.")
  private String content;         //게시글 내용
}
