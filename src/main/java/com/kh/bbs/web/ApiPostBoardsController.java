package com.kh.bbs.web;

import com.kh.bbs.domain.entity.PostBoards;
import com.kh.bbs.domain.postBoard.svc.PostBoardsSVC;
import com.kh.bbs.web.api.ApiResponse;
import com.kh.bbs.web.api.ApiResponseCode;
import com.kh.bbs.web.api.postBoard.SaveApi;
import com.kh.bbs.web.api.postBoard.UpdateApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequestMapping("/api/postBoards")
@RestController // @Controller + @ResponseBody
@RequiredArgsConstructor
public class ApiPostBoardsController {


  private final PostBoardsSVC postBoardsSVC;

  //상품 생성      //   POST    /postBoards  =>      POST http://localhost:9080/api/postBoards
  @PostMapping
  //@RequestBody : 요청메세지 body에 포함된 json포멧 문자열을 java 객체로 변환
  public ResponseEntity<ApiResponse<PostBoards>> add(
      @RequestBody @Valid SaveApi saveApi
  ) {
    log.info("saveApi={}", saveApi);

    PostBoards postBoards = new PostBoards();
    BeanUtils.copyProperties(saveApi, postBoards);

    Long id = postBoardsSVC.save(postBoards);
    Optional<PostBoards> optionalPostBoards = postBoardsSVC.findById(id);
    PostBoards findedPostBoards = optionalPostBoards.orElseThrow();

    ApiResponse<PostBoards> postBoardsApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, findedPostBoards);

    return ResponseEntity.status(HttpStatus.CREATED).body(postBoardsApiResponse);
  }


  //게시글 조회      //   GET     /PostBoards/{id} =>  GET http://localhost:9080/api/PostBoards/{id}
  @GetMapping("/{id}")
//  @ResponseBody   // 응답메세지 body에 자바 객체를 json포맷 문자열로 변환
  public ResponseEntity<ApiResponse<PostBoards>> findById(@PathVariable("id") Long id) {

    Optional<PostBoards> optionalPostBoards = postBoardsSVC.findById(id);
    PostBoards findedPostBoard = optionalPostBoards.orElseThrow();  // 찾고자하는 게시글이 없으면 NoSuchElementException 예외발생

    ApiResponse<PostBoards> postBoardsApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, findedPostBoard);

    return ResponseEntity.ok(postBoardsApiResponse);  //상태코드 200, 응답메세지Body:postBoardsApiResponse객채가 json포맷 문자열로 변환됨
  }

  //게시글 수정      //   PATCH   /postBoards/{id} =>  PATCH http://localhost:9080/api/postBoards/{id}
  @PatchMapping("/{id}")
  public ResponseEntity<ApiResponse<PostBoards>> updateById(
      @PathVariable("id") Long id,
      @RequestBody @Valid UpdateApi updateApi // 요청메세지의 json포맷의 문자열을 자바 객체로 변환
  ) {

    //1) 게시글조회
    Optional<PostBoards> optionalPostBoards = postBoardsSVC.findById(id);
    PostBoards findedpostBoards = optionalPostBoards.orElseThrow(
        ()->new NoSuchElementException("게시글번호 : " + id + " 를 찾을 수 없습니다.")
    );  // 찾고자하는 게시글이 없으면 NoSuchElementException 예외발생

    //2) 게시글수정
    PostBoards postBoard = new PostBoards();
    BeanUtils.copyProperties(updateApi, postBoard);
    int updatedRow = postBoardsSVC.updateById(id, postBoard);

    //3) 수정된게시글 조회
    optionalPostBoards = postBoardsSVC.findById(id);
    PostBoards updatedPostBoard = optionalPostBoards.orElseThrow();

    //4) REST API 응답 표준 메시지 생성
    ApiResponse<PostBoards> postBoardApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, updatedPostBoard);

    //5) HTTP 응답 메세지 생성
    return ResponseEntity.ok(postBoardApiResponse);
  }

  //게시글 삭제      //   DELETE  /postBoards/{id} =>  DELETE http://localhost:9080/api/postBoards/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<PostBoards>> deleteById(@PathVariable("id") Long id) {
    //1) 게시글조회
    Optional<PostBoards> optionalPostBoards = postBoardsSVC.findById(id);
    PostBoards findedPostBoards = optionalPostBoards.orElseThrow(
        ()->new NoSuchElementException("게시글번호 : " + id + " 를 찾을 수 없습니다.")
    );  // 찾고자하는 게시글이 없으면 NoSuchElementException 예외발생

    //2) 게시글 삭제
    int deletedRow = postBoardsSVC.deleteById(id);

    //3) REST API 표준 응답 생성
    ApiResponse<PostBoards> postBoardApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, findedPostBoards);

    //4) HTTP응답 메세지 생성
    return ResponseEntity.ok(postBoardApiResponse);
  }

  //게시글 목록      //   GET     /postBoards      =>  GET http://localhost:9080/api/postBoards
  @GetMapping
//  @ResponseBody
  public ResponseEntity<ApiResponse<List<PostBoards>>> findAll() {

    List<PostBoards> list = postBoardsSVC.findAll();
    ApiResponse<List<PostBoards>> listApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, list);

    return ResponseEntity.ok(listApiResponse);
  }

  //게시글 목록-페이징      //   GET     /postBoards      =>  GET http://localhost:9080/api/postBoards/paging?pageNo=1&numOfRows=10
  @GetMapping("/paging")
//  @ResponseBody
  public ResponseEntity<ApiResponse<List<PostBoards>>> findAll(
      @RequestParam(value="pageNo", defaultValue = "1") Integer pageNo,
      @RequestParam(value="numOfRows", defaultValue = "10") Integer numOfRows
  ) {
    log.info("pageNo={},numOfRows={}", pageNo, numOfRows);
    //게시글목록 가져오기
    List<PostBoards> list = postBoardsSVC.findAll(pageNo, numOfRows);
    //게시글 총건수 가져오기
    int totalCount = postBoardsSVC.getTotalCount();
    //REST API 표준 응답 만들기
    ApiResponse<List<PostBoards>> listApiResponse = ApiResponse.of(
        ApiResponseCode.SUCCESS,
        list,
        new ApiResponse.Paging(pageNo, numOfRows, totalCount)
    );
    return ResponseEntity.ok(listApiResponse);
  }

  //전체 건수 가져오기      //   GET   /postBoards/totCnt =>  GET http://localhost:9080/api/postBoards/totCnt
  @GetMapping("/totCnt")
  public ResponseEntity<ApiResponse<Integer>> totalCount() {

    int totalCount = postBoardsSVC.getTotalCount();
    ApiResponse<Integer> postBoardApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, totalCount);

    return ResponseEntity.ok(postBoardApiResponse);  //상태코드 200, 응답메세지Body:postBoardApiResponse객채가 json포맷 문자열로 변환됨
  }
}
