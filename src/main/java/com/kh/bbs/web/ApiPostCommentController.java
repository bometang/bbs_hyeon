package com.kh.bbs.web;

import com.kh.bbs.domain.entity.PostComment;
import com.kh.bbs.domain.postComment.svc.PostCommentSVC;
import com.kh.bbs.web.api.ApiResponse;
import com.kh.bbs.web.api.ApiResponseCode;
import com.kh.bbs.web.api.postComment.SaveApi;
import com.kh.bbs.web.api.postComment.UpdateApi;
import com.kh.bbs.web.form.login.LoginMember;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequestMapping("/api/postBoards/{postId}/postComment")
@RestController // @Controller + @ResponseBody
@RequiredArgsConstructor
public class ApiPostCommentController {


  private final PostCommentSVC postCommentSVC;

  //댓글 생성      //   POST      =>      POST http://localhost:9080/api/postBoards/{postId}/postComment
  @PostMapping
  //@RequestBody : 요청메세지 body에 포함된 json포멧 문자열을 java 객체로 변환
  public ResponseEntity<ApiResponse<PostComment>> add(
      @PathVariable("postId") Long postId,
      @RequestBody @Valid SaveApi saveApi,
      HttpSession session
  ) {
    log.info("saveApi={}", saveApi);

    // — 로그인 정보 가져오기 (예: 세션에 들어 있는 LoginMember에서 memberId 획득)
    LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");
    if (loginMember == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
    }

    PostComment postComment = new PostComment();
    BeanUtils.copyProperties(saveApi, postComment);

    postComment.setPostId(postId);
    postComment.setMemberId(loginMember.getMemberId());

    Long id = postCommentSVC.save(postComment);
    Optional<PostComment> optionalProduct = postCommentSVC.findById(id);
    PostComment findedProduct = optionalProduct.orElseThrow();

    ApiResponse<PostComment> postCommentApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, findedProduct);

    return ResponseEntity.status(HttpStatus.CREATED).body(postCommentApiResponse);
  }

  @PostMapping("/{parentId}")
  public ResponseEntity<ApiResponse<PostComment>> reply(
      @PathVariable("postId")   Long postId,
      @PathVariable("parentId") Long parentId,
      @RequestBody @Valid SaveApi saveApi,
      HttpSession session
  ) {
    // (1) 로그인 체크
    LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");
    if (loginMember == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
    }

    // (2) 부모 댓글 조회
    PostComment parentComment = postCommentSVC.findById(parentId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "부모 댓글을 찾을 수 없습니다. id=" + parentId));

    // (3) 부모 댓글이 해당 게시글에 속하는지 확인 (선택 사항)
    if (!parentComment.getPostId().equals(postId)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "부모 댓글이 현재 게시글에 속해 있지 않습니다. postId=" + postId);
    }

    // (4) depth 제한: 부모가 이미 depth 3 이상이면 더 달 수 없음
    if (parentComment.getDepth() >= 3) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "대댓글은 최대 깊이 3까지만 허용됩니다. 현재 부모 깊이=" + parentComment.getDepth());
    }

    // (5) 대댓글 엔티티 생성 및 필드 세팅
    PostComment childComment = new PostComment();
    BeanUtils.copyProperties(saveApi, childComment);
    childComment.setPostId(postId);
    childComment.setMemberId(loginMember.getMemberId());
    childComment.setParentId(parentId);
    childComment.setDepth(parentComment.getDepth() + 1);

    // (6) 저장
    Long newCommentId = postCommentSVC.save(childComment);
    PostComment created = postCommentSVC.findById(newCommentId)
        .orElseThrow(() -> new RuntimeException("대댓글 조회 실패 id=" + newCommentId));

    ApiResponse<PostComment> response = ApiResponse.of(ApiResponseCode.SUCCESS, created);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }


  //댓글 수정      //   PATCH   /postComment/{id} =>  PATCH http://localhost:9080/api/postComment/{id}
  @PatchMapping("/{id}")
  public ResponseEntity<ApiResponse<PostComment>> updateById(
      @PathVariable("postId") Long postId,
      @PathVariable("id") Long id,
      @RequestBody @Valid UpdateApi updateApi, // 요청메세지의 json포맷의 문자열을 자바 객체로 변환
      HttpSession session
  ) {

    //1) 댓글조회
    Optional<PostComment> optionalPostComment = postCommentSVC.findById(id);
    PostComment findedpostComment = optionalPostComment.orElseThrow(
        ()->new NoSuchElementException("댓글번호 : " + id + " 를 찾을 수 없습니다.")
    );  // 찾고자하는 댓글이 없으면 NoSuchElementException 예외발생

    if (!findedpostComment.getPostId().equals(postId)) {
      throw new NoSuchElementException(
          "댓글 " + id + " 는 게시글 " + postId + " 에 속해 있지 않습니다."
      );
    }

    // 3) 로그인 정보 꺼내기
    LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");
    if (loginMember == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
    }

    Long loginMemberId = loginMember.getMemberId();
    String loginGubun   = loginMember.getGubun();

    // 4) 작성자이거나 관리자 권한 확인
    boolean isAuthor    = findedpostComment.getMemberId().equals(loginMemberId);
    boolean isAdminCode = "M01A1".equals(loginGubun) || "M01A2".equals(loginGubun);

    if (!isAuthor) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 댓글을 수정할 수 있습니다.");
    }

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
  public ResponseEntity<ApiResponse<PostComment>> deleteById(
      @PathVariable("postId") Long postId,
      @PathVariable("id") Long id,
      HttpSession session) {
    //1) 댓글조회
    Optional<PostComment> optionalPostComment = postCommentSVC.findById(id);
    PostComment findedPostComment = optionalPostComment.orElseThrow(
        ()->new NoSuchElementException("댓글번호 : " + id + " 를 찾을 수 없습니다.")
    );  // 찾고자하는 댓글이 없으면 NoSuchElementException 예외발생

    // 3) 로그인 정보 꺼내기
    LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");
    if (loginMember == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
    }

    Long loginMemberId = loginMember.getMemberId();
    String loginGubun   = loginMember.getGubun();

    // 4) 작성자이거나 관리자 권한 확인
    boolean isAuthor    = findedPostComment.getMemberId().equals(loginMemberId);
    boolean isAdminCode = "M01A1".equals(loginGubun) || "M01A2".equals(loginGubun);

    if (!isAuthor) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 댓글을 삭제할 수 있습니다.");
    }


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

  @GetMapping("/{id}")
//  @ResponseBody
  public  ResponseEntity<ApiResponse<PostComment>> findById(
      @PathVariable("postId") Long postId,
      @PathVariable("id") Long id
  ) {
    Optional<PostComment> optionalPostComment = postCommentSVC.findById(id);
    PostComment findedPostComment = optionalPostComment.orElseThrow();  // 찾고자하는 게시글이 없으면 NoSuchElementException 예외발생

    ApiResponse<PostComment> postCommentApiResponse = ApiResponse.of(ApiResponseCode.SUCCESS, findedPostComment);

    return ResponseEntity.ok(postCommentApiResponse);
  }

}
