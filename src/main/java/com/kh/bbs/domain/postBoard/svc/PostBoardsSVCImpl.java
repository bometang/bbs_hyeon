package com.kh.bbs.domain.postBoard.svc;

import com.kh.bbs.domain.entity.PostBoards;
import com.kh.bbs.domain.postBoard.dao.PostBoardsDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostBoardsSVCImpl implements PostBoardsSVC {
  final private PostBoardsDAO postBoardsDAO;

  //게시글 등록
  @Override
  public Long save(PostBoards postBoards) {
    return postBoardsDAO.save(postBoards);
  }

  //게시글 목록
  @Override
  public List<PostBoards> findAll() {
    return postBoardsDAO.findAll();
  }

  @Override
  public List<PostBoards> findAll(int pageNo, int numOfRows) {
    return postBoardsDAO.findAll(pageNo, numOfRows);
  }

  @Override
  public int getTotalCount() {
    return postBoardsDAO.getTotalCount();
  }

  //게시글 조회
  @Override
  public Optional<PostBoards> findById(Long id) {
    return postBoardsDAO.findById(id);
  }

  //게시글 삭제(단건)
  @Override
  public int deleteById(Long id) {
    return  postBoardsDAO.deleteById(id);
  }

  //게시글(여러건)
  @Override
  public int deleteByIds(List<Long> ids) {
    return postBoardsDAO.deleteByIds(ids);
  }

  //게시글 수정
  @Override
  public int updateById(Long postId, PostBoards postBoards) {
    return postBoardsDAO.updateById(postId, postBoards);
  }

}
