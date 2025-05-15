package com.kh.bbs.web.form.postboard;

import lombok.Data;

@Data
public class DetailForm {
  //테이블의 모든 칼럼
  private Long postId;
  private String title;
  private String content;
  private String userName;
  private String cdate;
  private String udate;
}
