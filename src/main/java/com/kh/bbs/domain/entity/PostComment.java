package com.kh.bbs.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostComment {
  private Long commentId;            //게시글 아이디
  private Long postId;           //게시글 제목
  private Long parentId;         //게시글 내용
  private Long depth;          //게시글 작성자 아이디
  private String content;        //게시글 작성자 이름
  private Long memberId;          //게시글 코드
  private String nickname;
  private String codeId;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd HH:mm:ss")
  private LocalDateTime cdate;    //게시글 등록시간
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd HH:mm:ss")
  private LocalDateTime udate;    //게시글 수정시간
}
