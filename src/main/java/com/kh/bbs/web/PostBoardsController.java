package com.kh.bbs.web;


import com.kh.bbs.domain.entity.PostBoards;
import com.kh.bbs.domain.postBoard.svc.PostBoardsSVC;
import com.kh.bbs.web.form.postboard.DetailForm;
import com.kh.bbs.web.form.postboard.SaveForm;
import com.kh.bbs.web.form.postboard.UpdateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/boards")      // GET http://localhost:9080/boards
@RequiredArgsConstructor
public class PostBoardsController {
  final private PostBoardsSVC postBoardsSVC;

  //목록
  @GetMapping       // GET  http://localhost:9080/boards
  public String findAll(Model model) {
    List<PostBoards> list = postBoardsSVC.findAll();
    model.addAttribute("list", list);
    return "postBoards/allForm";   //view
  }

  //게시글등록화면
  @GetMapping("/add")       // GET  http://localhost:9080/boards/add
  public String addForm(Model model){
    model.addAttribute("saveForm",new SaveForm());
    return "postBoards/addForm";  //view
  }

  //게시글등록처리
  @PostMapping("/add")      // POST http://localhost:9080/boards/add
  public String add(
      @Valid @ModelAttribute SaveForm saveForm,
      BindingResult bindingResult,  //BindingResult :1.요청데이터  2.검증 결과를 담는 객체
      RedirectAttributes redirectAttributes,
      Model model
  ){
    //1)유효성 체크
    //1-1) 어노테이션 기반의 필드 검증
    if(bindingResult.hasErrors()){
      return "postBoards/addForm";
    }

    //2)정상로직
    PostBoards postBoards = new PostBoards();
    postBoards.setTitle(saveForm.getTitle());
    postBoards.setUserName(saveForm.getUserName());
    postBoards.setContent(saveForm.getContent());

    Long pid = postBoardsSVC.save(postBoards);
    redirectAttributes.addAttribute("id",pid);
    return "redirect:/boards/{id}"; //http 응답메세지  상태라인 : 302
                                    //               응답헤더 -> location : http://localhost:9080/boards/2
                                    //http 요청메세지  요청라인 : GET http://localhost:9080/boards/2
  }

  //게시글조회
  @GetMapping("/{id}")      // GET http://localhost:9080/boards/2
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
    detailForm.setUserName(findedPostBoarders.getUserName());
    detailForm.setUdate(findedPostBoarders.getUdate());
    detailForm.setCdate(findedPostBoarders.getCdate());


    model.addAttribute("detailForm",detailForm);

    return "postBoards/detailForm";   //상품상세화면
  }

  //게시글삭제(단건)
  @GetMapping("/{id}/del")   // GET http://localhost:9080/products/게시글아이디/del
  public String deleteById(
      @PathVariable("id") Long postId) {

    int rows = postBoardsSVC.deleteById(postId);

    return "redirect:/boards";      // 302 get redirectUrl: http://localhost:9080/boards
  }

  //게시글삭제(여러건)
  @PostMapping("/del")      // POST http://localhost:9080/boards/del
  public String deleteByIds(@RequestParam("postIds") List<Long> postIds) {

    int rows = postBoardsSVC.deleteByIds(postIds);
    return "redirect:/boards";
  }

  //게시글수정화면
  @GetMapping("/{id}/edit")         // GET http://localhost:9080/게시글아이디/edit
  public String updateForm(
      @PathVariable("id") Long postId,
      Model model
  ) {

    //게시글조회
    Optional<PostBoards> optionalPostBoards = postBoardsSVC.findById(postId);
    PostBoards findedPostBoards = optionalPostBoards.orElseThrow();

    UpdateForm updateForm = new UpdateForm();
    updateForm.setPostId(findedPostBoards.getPostId());
    updateForm.setTitle(findedPostBoards.getTitle());
    updateForm.setContent(findedPostBoards.getContent());
    updateForm.setUserName(findedPostBoards.getUserName());
    updateForm.setUdate(findedPostBoards.getUdate());

    model.addAttribute("updateForm",updateForm);
    return "postBoards/updateForm";
  }

  //상품수정처리
  @PostMapping("/{id}/edit")         // POST http://localhost:9080/게시글아이디/edit
  public String updateById(
      @PathVariable("id") Long postId,
      @Valid @ModelAttribute UpdateForm updateForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
  ){
    //1)유효성 체크
    //1-1) 어노테이션 기반의 필드 검증
    if(bindingResult.hasErrors()){

      return "postBoards/updateForm";
    }

    PostBoards postBoards = new PostBoards();
    postBoards.setPostId(updateForm.getPostId());
    postBoards.setTitle(updateForm.getTitle());
    postBoards.setContent(updateForm.getContent());
    postBoards.setUserName(updateForm.getUserName());

    int rows = postBoardsSVC.updateById(postId, postBoards);

    redirectAttributes.addAttribute("id",postId);
    return "redirect:/boards/{id}";  // 302 get redirectUrl-> http://localhost/boards/id
  }
}
