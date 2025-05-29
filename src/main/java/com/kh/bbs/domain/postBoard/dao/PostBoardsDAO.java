package com.kh.bbs.domain.postBoard.dao;

import com.kh.bbs.domain.entity.PostBoards;

import java.util.List;
import java.util.Optional;

public interface PostBoardsDAO {
  //게시글등록
  Long save(PostBoards postBoards);

  //게시글 목록
  List<PostBoards> findAll();

  List<PostBoards> findAll(int pageNo, int numOfRows);

  int getTotalCount();

  //게시글확인
  Optional<PostBoards> findById(Long id);

  //게시글삭제(단건)
  int deleteById(Long id);

  //게시글삭제(여러건)
  int deleteByIds(List<Long> ids);

  //게시글수정
  int updateById(Long postId, PostBoards postBoards);
}
