package com.kh.bbs.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostBoards {
  private Long postId;            //게시글 아이디
  private String title;           //게시글 제목
  private String content;         //게시글 내용
  private Long memberId;          //게시글 작성자 아이디
  private String nickname;        //게시글 작성자 이름
  private String codeId;          //게시글 코드
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd HH:mm:ss")
  private LocalDateTime cdate;    //게시글 등록시간
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd HH:mm:ss")
  private LocalDateTime udate;    //게시글 수정시간
}
