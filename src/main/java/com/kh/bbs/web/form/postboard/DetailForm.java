package com.kh.bbs.web.form.postboard;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DetailForm {
  //테이블의 모든 칼럼
  private Long postId;
  private String title;
  private String content;
  private String nickname;
  private String codeId;
  private LocalDateTime cdate;
  private LocalDateTime udate;
}
