package com.kh.bbs.web;

import com.kh.bbs.domain.entity.PostBoards;
import com.kh.bbs.domain.postBoard.svc.PostBoardsSVC;
import com.kh.bbs.web.form.postboard.DetailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/csr")
@RequiredArgsConstructor
public class CsrController {

  final private PostBoardsSVC postBoardsSVC;

  @GetMapping("/boards")
  public String products() {
    return "csr/postBoards/allForm";
  }

  //게시글조회
  @GetMapping("/boards/{id}")      // GET http://localhost:9080/boards/2
  public String findById(
      @PathVariable("id") Long id,        // 경로변수 값을 읽어올때
      Model model
  ){

    Optional<PostBoards> optionalPostBoards = postBoardsSVC.findById(id);
    PostBoards findedPostBoarders = optionalPostBoards.orElseThrow();

    DetailForm detailForm = new DetailForm();
    detailForm.setPostId(findedPostBoarders.getPostId());
    detailForm.setTitle(findedPostBoarders.getTitle());
    detailForm.setContent(findedPostBoarders.getContent());
    detailForm.setNickname(findedPostBoarders.getNickname());
    detailForm.setUdate(findedPostBoarders.getUdate());
    detailForm.setCdate(findedPostBoarders.getCdate());


    model.addAttribute("detailForm",detailForm);

    return "postBoards/detailForm";   //상품상세화면
  }

}
