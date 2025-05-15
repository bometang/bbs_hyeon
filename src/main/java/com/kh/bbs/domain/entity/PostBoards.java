package com.kh.bbs.domain.entity;

import lombok.Data;

@Data
public class PostBoards {
  private Long postId;        //게시글 아이디
  private String title;       //게시글 제목
  private String content;     //게시글 내용
  private String userName;    //게시글 작성자
  private String cdate;       //게시글 등록시간
  private String udate;       //게시글 수정시간
}
