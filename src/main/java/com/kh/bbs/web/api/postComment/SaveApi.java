package com.kh.bbs.web.api.postComment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SaveApi {
  private Long commentId;
  private Long postId;
  private Long parentId;
  @Max(value=3L, message = "대댓글은 더이상 달수 없습니다.")
  private Long depth;
  @NotNull(message="내용은 필수 입니다.")
  private String content;
  private Long memberId;
  private String codeId;
  private LocalDateTime cdate;
  private LocalDateTime udate;
}
