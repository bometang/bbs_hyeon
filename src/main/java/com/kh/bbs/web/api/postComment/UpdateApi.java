package com.kh.bbs.web.api.postComment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateApi {
  private Long commentId;            //댓글 아이디
  private Long postId;           //게시글 아이디
  private Long parentId;         //부모 댓글 아이디
  private Long depth;          //대댓글 위치
  @NotNull(message="내용은 필수 입니다.")
  private String content;        //댓글 내용
  private Long memberId;          //댓글 작성자 아이디
  private String codeId;        //댓글 코드
  private LocalDateTime udate;
}
