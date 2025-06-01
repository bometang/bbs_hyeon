package com.kh.bbs.domain.postComment.dao;

import com.kh.bbs.domain.entity.PostComment;

import java.util.List;
import java.util.Optional;

public interface PostCommentDAO {
  //댓글등록
  Long save(PostComment postComment);

  //대댓글등록
  Long commentSave(PostComment postComment);

  //댓글 목록
  List<PostComment> findAll(Long id);

  List<PostComment> findAll(Long id, int pageNo, int numOfRows);

  int getTotalCount(Long id);

  //댓글확인
  Optional<PostComment> findById(Long id);

  //댓글삭제(단건)
  int deleteById(Long id);


  //댓글수정
  int updateById(Long postCommentId, PostComment postComment);
}
