package com.kh.bbs.web;

import com.kh.bbs.domain.postBoard.svc.PostBoardsSVC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
      @PathVariable("id") Long id,
      Model model
  ){
    model.addAttribute("postId", id);

    return "csr/postBoards/detailForm";   //상품상세화면
  }

}
