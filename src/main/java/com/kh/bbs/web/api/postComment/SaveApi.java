package com.kh.bbs.web.api.postComment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SaveApi {
  private Long commentId;            //게시글 아이디
  private Long postId;           //게시글 제목
  private Long parentId;         //게시글 내용
  private Long depth;          //게시글 작성자 아이디
  private String content;        //게시글 작성자 이름
  private Long memberId;          //게시글 코드
  private String codeId;
  private LocalDateTime cdate;    //게시글 등록시간
  private LocalDateTime udate;
}
