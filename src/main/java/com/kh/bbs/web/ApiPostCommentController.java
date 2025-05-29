package com.kh.bbs.web;

import com.kh.bbs.domain.entity.PostComment;
import com.kh.bbs.domain.postComment.svc.PostCommentSVC;
import com.kh.bbs.web.api.ApiResponse;
import com.kh.bbs.web.api.ApiResponseCode;
import com.kh.bbs.web.api.postComment.UpdateApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequestMapping("/api/postBoards/{postId}")
@RestController // @Controller + @ResponseBody
@RequiredArgsConstructor
public class ApiPostCommentController {


  private final PostCommentSVC postCommentSVC;


  //댓글 조회
  @GetMapping("/{id}")
//  @ResponseBody   // 응답메세지 body에 자바 객체를 json포맷 문자열로 변환
  public ResponseEntity<ApiResponse<PostComment>> findById(@PathVariable("postId") Long postId,@PathVariable("id") Long id) {

    Optional<PostComment> optionalPostComment = postCommentSVC.findById(id);
    PostComment findedPostComment = optionalPostComment.orElseThrow();  // 찾고자하는 댓글이 없으면 NoSuchElementException 예외발생

    ApiResponse<PostComment> postCommentApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, findedPostComment);

    return ResponseEntity.ok(postCommentApiResponse);  //상태코드 200, 응답메세지Body:postCommentApiResponse객채가 json포맷 문자열로 변환됨
  }

  //댓글 수정      //   PATCH   /postComment/{id} =>  PATCH http://localhost:9080/api/postComment/{id}
  @PatchMapping("/{id}")
  public ResponseEntity<ApiResponse<PostComment>> updateById(
      @PathVariable("postId") Long postId,
      @PathVariable("id") Long id,
      @RequestBody @Valid UpdateApi updateApi // 요청메세지의 json포맷의 문자열을 자바 객체로 변환
  ) {

    //1) 댓글조회
    Optional<PostComment> optionalPostComment = postCommentSVC.findById(id);
    PostComment findedpostComment = optionalPostComment.orElseThrow(
        ()->new NoSuchElementException("댓글번호 : " + id + " 를 찾을 수 없습니다.")
    );  // 찾고자하는 댓글이 없으면 NoSuchElementException 예외발생

    //2) 댓글수정
    PostComment postComment = new PostComment();
    BeanUtils.copyProperties(updateApi, postComment);
    int updatedRow = postCommentSVC.updateById(id, postComment);

    //3) 수정된댓글 조회
    optionalPostComment = postCommentSVC.findById(id);
    PostComment updatedPostComment = optionalPostComment.orElseThrow();

    //4) REST API 응답 표준 메시지 생성
    ApiResponse<PostComment> postCommentApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, updatedPostComment);

    //5) HTTP 응답 메세지 생성
    return ResponseEntity.ok(postCommentApiResponse);
  }

  //댓글 삭제      //   DELETE  /postComment/{id} =>  DELETE http://localhost:9080/api/postComment/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<PostComment>> deleteById(@PathVariable("postId") Long postId,@PathVariable("id") Long id) {
    //1) 댓글조회
    Optional<PostComment> optionalPostComment = postCommentSVC.findById(id);
    PostComment findedPostComment = optionalPostComment.orElseThrow(
        ()->new NoSuchElementException("댓글번호 : " + id + " 를 찾을 수 없습니다.")
    );  // 찾고자하는 댓글이 없으면 NoSuchElementException 예외발생

    //2) 댓글 삭제
    int deletedRow = postCommentSVC.deleteById(id);

    //3) REST API 표준 응답 생성
    ApiResponse<PostComment> postCommentApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, findedPostComment);

    //4) HTTP응답 메세지 생성
    return ResponseEntity.ok(postCommentApiResponse);
  }

  //댓글 목록      //   GET     /postComment      =>  GET http://localhost:9080/api/postComment
  @GetMapping
//  @ResponseBody
  public ResponseEntity<ApiResponse<List<PostComment>>> findAll(@PathVariable("postId") Long postId) {

    List<PostComment> list = postCommentSVC.findAll(postId);
    ApiResponse<List<PostComment>> listApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, list);

    return ResponseEntity.ok(listApiResponse);
  }

  //댓글 목록-페이징      //   GET     /postComment      =>  GET http://localhost:9080/api/postComment/paging?pageNo=1&numOfRows=10
  @GetMapping("/paging")
//  @ResponseBody
  public ResponseEntity<ApiResponse<List<PostComment>>> findAll(
      @PathVariable("postId") Long postId,
      @RequestParam(value="pageNo", defaultValue = "1") Integer pageNo,
      @RequestParam(value="numOfRows", defaultValue = "5") Integer numOfRows
  ) {
    log.info("pageNo={},numOfRows={}", pageNo, numOfRows);
    //댓글목록 가져오기
    List<PostComment> list = postCommentSVC.findAll(postId,pageNo, numOfRows);
    //댓글 총건수 가져오기
    int totalCount = postCommentSVC.getTotalCount(postId);
    //REST API 표준 응답 만들기
    ApiResponse<List<PostComment>> listApiResponse = ApiResponse.of(
        ApiResponseCode.SUCCESS,
        list,
        new ApiResponse.Paging(pageNo, numOfRows, totalCount)
    );
    return ResponseEntity.ok(listApiResponse);
  }

  //전체 건수 가져오기      //   GET   /postComment/totCnt =>  GET http://localhost:9080/api/postComment/totCnt
  @GetMapping("/totCnt")
  public ResponseEntity<ApiResponse<Integer>> totalCount(@PathVariable("postId") Long postId) {

    int totalCount = postCommentSVC.getTotalCount(postId);
    ApiResponse<Integer> postCommentApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, totalCount);

    return ResponseEntity.ok(postCommentApiResponse);  //상태코드 200, 응답메세지Body:postCommentApiResponse객채가 json포맷 문자열로 변환됨
  }
}
