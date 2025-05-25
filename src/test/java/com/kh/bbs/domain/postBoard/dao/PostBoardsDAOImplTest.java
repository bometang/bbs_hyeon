package com.kh.bbs.domain.postBoard.dao;

import com.kh.bbs.domain.entity.PostBoards;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
class PostBoardsDAOImplTest {
  @Autowired  // 스프링 컨테이너에 동일 타입의 객체를 찾아와 주입해줌
  PostBoardsDAO postBoardsDAO;

  @Test
  @DisplayName("게시글목록")
  void findAll(){
    List<PostBoards> list = postBoardsDAO.findAll();
    for (PostBoards postBoards : list) {
      log.info("postBoards={}",postBoards);
    }
  }

  @Test
  @DisplayName("게시글등록")
  void save(){
    PostBoards postBoards = new PostBoards();
    postBoards.setTitle("제목1");
    postBoards.setNickname("유저1");
    postBoards.setContent("내용1");

    Long pid = postBoardsDAO.save(postBoards);
    log.info("게시글번호={}", pid);
  }

  @Test
  @DisplayName("게시글조회")
  void findById() {
    Long postId = 32L;
    Optional<PostBoards> optionalPostBoards = postBoardsDAO.findById(postId);
    PostBoards findedPost = optionalPostBoards.orElseThrow();// 값이 없으면 예외 발생
    log.info("findedPost={}", findedPost);
  }

  @Test
  @DisplayName("게시글삭제(단건)")
  void deleteById(){
    Long id = 32L;
    int rows = postBoardsDAO.deleteById(id);
    log.info("rows={}",rows);
    Assertions.assertThat(rows).isEqualTo(1);
  }

  @Test
  @DisplayName("게시글삭제(여러건)")
  void deleteByIds() {
    List<Long> ids = List.of(28L,29L);
    int rows = postBoardsDAO.deleteByIds(ids);
    Assertions.assertThat(rows).isEqualTo(2);
  }

  @Test
  @DisplayName("게시글수정")
  void updateById() {
    Long id = 7L;
    PostBoards postBoards = new PostBoards();
    postBoards.setTitle("시험기간 동안");
    postBoards.setContent("시험기간입니다 시험");

    int rows = postBoardsDAO.updateById(id, postBoards);

    Optional<PostBoards> optPost = postBoardsDAO.findById(id);
    // optProduct.orElseThrow() : optional 컨테이너 객체에 postBoard객체가 존재하면 반환 존재하지 않으면 예외발생
    PostBoards modifiedPost = optPost.orElseThrow();

    Assertions.assertThat(modifiedPost.getTitle()).isEqualTo("시험기간 동안");
    Assertions.assertThat(modifiedPost.getContent()).isEqualTo("시험기간입니다 시험");
  }





}