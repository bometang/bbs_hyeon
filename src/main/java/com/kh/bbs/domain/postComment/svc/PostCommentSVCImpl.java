package com.kh.bbs.domain.postComment.svc;

import com.kh.bbs.domain.entity.PostComment;
import com.kh.bbs.domain.postComment.dao.PostCommentDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostCommentSVCImpl implements PostCommentSVC{
  final private PostCommentDAO postCommentDAO;

  //댓글 등록
  @Override
  public Long save(PostComment postComment) {
    return postCommentDAO.save(postComment);
  }

  //댓글 목록
  @Override
  public List<PostComment> findAll(Long id) {
    return postCommentDAO.findAll(id);
  }

  @Override
  public List<PostComment> findAll(Long id, int pageNo, int numOfRows) {
    return postCommentDAO.findAll(id, pageNo, numOfRows);
  }

  @Override
  public int getTotalCount(Long id) {
    return postCommentDAO.getTotalCount(id);
  }

  //댓글 조회
  @Override
  public Optional<PostComment> findById(Long id) {
    return postCommentDAO.findById(id);
  }

  //댓글 삭제(단건)
  @Override
  public int deleteById(Long id) {
    return  postCommentDAO.deleteById(id);
  }


  //댓글 수정
  @Override
  public int updateById(Long postId, PostComment postcomment) {
    return postCommentDAO.updateById(postId, postcomment);
  }

}
