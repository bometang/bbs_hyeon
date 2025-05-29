package com.kh.bbs.domain.postComment.svc;

import com.kh.bbs.domain.entity.PostComment;

import java.util.List;
import java.util.Optional;

public interface PostCommentSVC {
  // 등록
  Long save(PostComment postcomment);
  // 댓글 목록
  List<PostComment> findAll(Long id);

  List<PostComment> findAll(Long id, int pageNo, int numOfRows);

  int getTotalCount(Long id);


  // 댓글 조회
  Optional<PostComment> findById(Long id);

  //댓글 삭제(단건)
  int deleteById(Long id);


  //댓글 수정
  int updateById(Long postId, PostComment postcomment);
}
