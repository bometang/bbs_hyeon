import { ajax,  PaginationUI } from '/js/common.js';

let currentPage = 1; // 현재 페이지를 위한 전역 변수
let initialPage = 1; // 게시글 추가 후 이동할 페이지 (1페이지)

const recordsPerPage = 10;        // 페이지당 레코드수
const pagesPerPage = 5;          // 한페이지당 페이지수

// 게시글조회
const getPostBoard = async (pid, frm) => {
  try {
    const url = `/api/postBoards/${pid}`;
    const result = await ajax.get(url);
    console.log('받은 데이터:', result.body);
    if (result.header.rtcd === 'S00') {
      frm.postId.value   = result.body.postId;
      frm.title.value    = result.body.title;
      frm.nickname.value = result.body.nickname;
      frm.content.value  = result.body.content;
      frm.cdate.value  = result.body.cdate;
      frm.udate.value  = result.body.udate;
    } else if (result.header.rtcd.startsWith('E')) {
      for (let key in result.header.details) {
        console.error(`필드명:${key}, 오류:${result.header.details[key]}`);
      }
    } else {
      alert(result.header.rtmsg);
    }
  } catch (err) {
    console.error(err);
  }
};

async function modifyPostBoard(pid, post) {
  try {
    const url = `/api/postBoards/${pid}`;
    const result = await ajax.put(url, post);

    if (result.header.rtcd === 'S00') {
      // 수정 성공
      return;
    } else {
      throw new Error(result.header.rtmsg);
    }
  } catch (err) {
    console.error(err);
    throw err;
  }
}

async function delPostBoard(pid, frm) {
  try {
    const url = `/api/postBoards/${pid}`;
    await ajax.delete(url);
    alert('게시글이 삭제되었습니다.');
    // 삭제 후 목록 페이지로 이동
    location.href = '/csr/boards';
  } catch (err) {
    console.error(err);
    alert('게시글 삭제 중 오류가 발생했습니다.');
  }
}

function changeReadMode(frm) {
  // (1) 편집 모드 클래스 제거, 읽기 모드 클래스 추가
  frm.classList.toggle('mode-edit', false);
  frm.classList.toggle('mode-read', true);

  // (2) 모든 input 필드를 readonly로 설정
  [...frm.querySelectorAll('input')]
    .forEach(input => input.setAttribute('readonly', ''));

  // (3) 버튼 영역(.btns)에 “수정 / 삭제” 버튼을 렌더링
  const $btns = frm.querySelector('.btns');
  $btns.innerHTML = `
    <button id="btnEdit" type="button">수정</button>
    <button id="btnDelete" type="button">삭제</button>
  `;

  // (4) “수정” 버튼 클릭 시 → changeEditMode 호출
  $btns.querySelector('#btnEdit')
    .addEventListener('click', () => changeEditMode(frm));

  // (5) “삭제” 버튼 클릭 시 → delPostBoard 호출
  $btns.querySelector('#btnDelete')
    .addEventListener('click', () => {
      if (!confirm('정말 삭제하시겠습니까?')) return;
      delPostBoard(frm.postId.value, frm);
    });
}

function changeEditMode(frm) {
  // (1) 읽기 모드 클래스 제거, 편집 모드 클래스 추가
  frm.classList.toggle('mode-read', false);
  frm.classList.toggle('mode-edit', true);

  // (2) postId 필드를 제외한 나머지 input 필드들의 readonly 속성 제거
  [...frm.querySelectorAll('input')]
    .filter(input => input.name !== 'postId')
    .forEach(input => input.removeAttribute('readonly'));

  // (3) 버튼 영역(.btns)에 “저장 / 취소” 버튼을 렌더링
  const $btns = frm.querySelector('.btns');
  $btns.innerHTML = `
    <button id="btnSave" type="button">저장</button>
    <button id="btnCancel" type="button">취소</button>
  `;

  // (4) “저장” 버튼 클릭 시 → 수정 API 호출 후 다시 읽기 모드
  $btns.querySelector('#btnSave')
    .addEventListener('click', async () => {
      // FormData 객체를 JS 객체로 변환
      const formData = new FormData(frm);
      const post = {};
      for (let key of formData.keys()) {
        post[key] = formData.get(key);
      }
      try {
        await modifyPostBoard(post.postId, post);
        await getPostBoard(post.postId, frm);
        changeReadMode(frm);
      } catch (err) {
        console.error(err);
        alert('게시글 수정 중 오류가 발생했습니다.');
      }
    });

  // (5) “취소” 버튼 클릭 시 → 폼을 reset()하고 읽기 모드로 복귀
  $btns.querySelector('#btnCancel')
    .addEventListener('click', () => {
      frm.reset();
      changeReadMode(frm);
    });
}

// 게시글조회 화면 (pid를 인자로 받도록)
function displayReadForm(pid) {
  // 읽기/편집 모드 전환 함수
  const changeReadMode = frm => {
    frm.classList.add('mode-read');
    [...frm.querySelectorAll('input')]
      .filter(i => i.name !== 'postId')
      .forEach(i => i.setAttribute('readonly', ''));

    const $btns = frm.querySelector('.btns');
    $btns.innerHTML = `
      <button id="btnList" type="button">목록</button>
      <button id="btnEdit" type="button">수정</button>
      <button id="btnDelete" type="button">삭제</button>
    `;
    // 목록 버튼
    $btns.querySelector('#btnList')
      .addEventListener('click', () => {location.href = '/csr/boards';});


    // 수정 버튼
    $btns.querySelector('#btnEdit')
      .addEventListener('click', () =>  changeEditMode(frm));

    // 삭제 버튼
    $btns.querySelector('#btnDelete')
      .addEventListener('click', () => {
        if (!confirm('삭제하시겠습니까?')) return;
        delPostBoard(frm.postId.value, frm);
      });
  };

  // 폼 그리기
  const $wrap = document.createElement('div');
  $wrap.innerHTML = `
    <form id="frm">
      <div>
        <label for="postId">게시글아이디</label>
        <input type="text" id="postId" name="postId" readonly/>
      </div>
      <div>
        <label for="title">제목</label>
        <input type="text" id="title" name="title" readonly/>
      </div>
      <div>
        <label for="nickname">작성자</label>
        <input type="text" id="nickname" name="nickname" readonly/>
      </div>
      <div>
        <label for="cdate">작성일</label>
        <input type="text" id="cdate" name="cdate" readonly/>
      </div>
      <div>
        <label for="udate">수정일</label>
        <input type="text" id="udate" name="udate" readonly/>
      </div>
      <div>
        <label for="content">내용</label>
        <input type="text" id="content" name="content" readonly/>
      </div>
      <div class="btns"></div>
    </form>
  `;
  document.body.insertAdjacentElement('afterbegin', $wrap);

  const frm = document.getElementById('frm');
  changeReadMode(frm);
  getPostBoard(pid, frm);
}

document.addEventListener("DOMContentLoaded", () => {
  if (typeof window.postId !== 'undefined') {
    displayReadForm(window.postId);
  } else {
    console.error("postId가 정의되지 않았습니다.");
  }
  configPagination();
});



//댓글목록
const getPostComment = async (reqPage, reqRec) => {

  try {
    const url = `/api/postBoards/${window.postId}/postComment/paging?pageNo=${reqPage}&numOfRows=${reqRec}`;
    const result = await ajax.get(url);

    if (result.header.rtcd === 'S00') {
      currentPage = reqPage; // 현재 페이지 업데이트
      displayPostCommentList(result.body);

    } else {
      alert(result.header.rtmsg);
    }
  } catch (err) {
    console.error(err);
  }
};

//댓글목록 화면
function displayPostCommentList(postComment) {

  const makeTr = postComment => {
    const $tr = postComment
      .map(
        postComment =>
          `<tr data-pid=${postComment.commentId}>
            <td>${postComment.commentId}</td>
            <td>${postComment.content}</td>
            <td>${postComment.nickname}</td>
            <td>${postComment.cdate}</td>
            <td>${postComment.udate}</td></tr>`,
      )
      .join('');
    return $tr;
  };

  $list.innerHTML = `
    <table>
      <caption> 게 시 글 목 록 </caption>
      <thead>
        <tr>
          <th>댓글 번호</th>
          <th>내용</th>
          <th>작성자</th>
          <th>작성일</th>
          <th>수정일</th>
        </tr>
      </thead>
      <tbody>
        ${makeTr(postComment)}
      </tbody>
    </table>`;

  const $postComment = $list.querySelectorAll('table tbody tr');
}

const $list = document.createElement('div');
$list.setAttribute('id','list')
document.body.appendChild($list);

const divEle = document.createElement('div');
divEle.setAttribute('id','reply_pagenation');
document.body.appendChild(divEle);

async function configPagination(){
  const url = `/api/postBoards/${window.postId}/postComment/totCnt`;
  try {
    const result = await ajax.get(url);

    const totalRecords = result.body; // 전체 레코드수

    const handlePageChange = (reqPage)=>{
      return getPostComment(reqPage,recordsPerPage);
    };

    // Pagination UI 초기화
    var pagination = new PaginationUI('reply_pagenation', handlePageChange);

    pagination.setTotalRecords(totalRecords);       //총건수
    pagination.setRecordsPerPage(recordsPerPage);   //한페이지당 레코드수
    pagination.setPagesPerPage(pagesPerPage);       //한페이지당 페이지수

    // 첫페이지 가져오기
    pagination.handleFirstClick();

  }catch(err){
    console.error(err);
  }
}







//
///* -----------------------------
//   댓글 모음
//   ----------------------------- */
//
//async function fetchComments(pid) {
//  try {
//    const url = `/api/postBoards/${pid}/postComment`;
//    const result = await ajax.get(url);
//
//    if (result.header.rtcd === 'S00') {
//      renderComments(result.body, pid);
//    } else {
//      alert(result.header.rtmsg);
//    }
//  } catch (err) {
//    console.error(err);
//    document.getElementById("commentList").innerHTML =
//      `<p style="color:red;">댓글을 불러오는 중 오류가 발생했습니다.</p>`;
//  }
//}
//
//function renderComments(comments, pid) {
//  const container = document.getElementById("commentList");
//  if (!Array.isArray(comments) || comments.length === 0) {
//    container.innerHTML = `<p>등록된 댓글이 없습니다.</p>`;
//    return;
//  }
//  // 각 댓글마다 “수정 / 삭제” 버튼을 포함하도록 마크업 생성
//  container.innerHTML = comments.map(c => `
//    <div class="comment-item" data-cid="${c.commentId}">
//      <div>
//        <strong>${c.memberId}</strong>
//        <small style="color:#555; margin-left:0.5rem;">
//          ${new Date(c.cdate).toLocaleString()}
//        </small>
//        <button class="btnEditComment">수정</button>
//        <button class="btnDeleteComment">삭제</button>
//      </div>
//      <div class="comment-content">${c.content}</div>
//    </div>
//  `).join("");
//
//  // “수정” 버튼 이벤트 바인딩
//  container.querySelectorAll(".btnEditComment").forEach(btn => {
//    btn.addEventListener("click", e => {
//      const cid = e.currentTarget.closest(".comment-item").dataset.cid;
//      enterCommentEditMode(cid, pid);
//    });
//  });
//
//  // “삭제” 버튼 이벤트 바인딩
//  container.querySelectorAll(".btnDeleteComment").forEach(btn => {
//    btn.addEventListener("click", async e => {
//      const cid = e.currentTarget.closest(".comment-item").dataset.cid;
//      if (!confirm("댓글을 삭제하시겠습니까?")) return;
//      await deleteComment(cid, pid);
//      fetchComments(pid);
//    });
//  });
//}
//
//
///**
// * 댓글 작성 API 호출 함수
// * @param {number|string} pid    - 댓글을 등록할 게시글 ID
// * @param {string} content       - 댓글 내용
// */
//async function postComment(pid, content) {
//  try {
//    // DTO에 맞게 payload 구성
//    const payload = { postId: pid, content };
//    const result = await ajax.post(`/api/postBoards/${pid}/postComment`, payload);
//
//    if (result.header.rtcd === 'S00') {
//      // 성공 시 다시 댓글 목록 갱신
//      fetchComments(pid);
//    } else {
//      alert(result.header.rtmsg);
//    }
//  } catch (err) {
//    console.error(err);
//    alert("댓글 등록 중 오류가 발생했습니다.");
//  }
//}
//
//
///**
// * 댓글 수정 화면(인라인 편집)을 활성화하는 함수
// * @param {number|string} cid   - 수정할 댓글 ID
// * @param {number|string} pid   - 게시글 ID
// */
//function enterCommentEditMode(cid, pid) {
//  // 1) 해당 .comment-item 엘리먼트 찾기
//  const commentItem = document.querySelector(`.comment-item[data-cid="${cid}"]`);
//  if (!commentItem) return;
//
//  // 2) 기존 콘텐츠를 텍스트로 가져와서
//  const oldContent = commentItem.querySelector(".comment-content").innerText;
//
//  // 3) .comment-content 영역을 인라인 <input>으로 교체
//  commentItem.querySelector(".comment-content").innerHTML = `
//    <input type="text" class="editCommentInput" value="${oldContent}"/>
//    <button class="btnSaveComment">저장</button>
//    <button class="btnCancelComment">취소</button>
//  `;
//
//  // 4) “저장” 버튼 클릭 시 → modifyComment 호출
//  const $btnSaveComment = commentItem.querySelector(".btnSaveComment");
//  $btnSaveComment.addEventListener("click", async () => {
//    const newContent = commentItem.querySelector(".editCommentInput").value.trim();
//    if (!newContent) return alert("댓글 내용을 입력하세요.");
//    await modifyComment(cid, pid, newContent);
//    fetchComments(pid);
//  });
//
//  // 5) “취소” 버튼 클릭 시 → 원래 텍스트로 되돌리고 편집 모드 종료
//  const $btnCancelComment = commentItem.querySelector(".btnCancelComment");
//  $btnCancelComment.addEventListener("click", () => {
//    // 다시 목록을 새로 그리면 편집 모드가 사라짐
//    fetchComments(pid);
//  });
//}
//
//
///**
// * 댓글 수정 API 호출 함수
// * @param {number|string} cid            - 수정할 댓글 ID
// * @param {number|string} pid            - 게시글 ID
// * @param {string} updatedContent        - 수정할 댓글 내용
// */
//async function modifyComment(cid, pid, updatedContent) {
//  try {
//    const payload = { content: updatedContent };
//    const result = await ajax.patch(`/api/postBoards/${pid}/postComment/${cid}`, payload);
//    if (result.header.rtcd !== 'S00') {
//      alert(result.header.rtmsg);
//    }
//  } catch (err) {
//    console.error(err);
//    alert("댓글 수정 중 오류가 발생했습니다.");
//  }
//}
//
//
///**
// * 댓글 삭제 API 호출 함수
// * @param {number|string} cid - 삭제할 댓글 ID
// * @param {number|string} pid - 게시글 ID
// */
//async function deleteComment(cid, pid) {
//  try {
//    await ajax.delete(`/api/postBoards/${pid}/postComment/${cid}`);
//  } catch (err) {
//    console.error(err);
//    alert("댓글 삭제 중 오류가 발생했습니다.");
//  }
//}
//
//
///* ----------------------------------------
//   화면이 모두 로드된 뒤 초기화 작업 수행
//   ---------------------------------------- */
//document.addEventListener("DOMContentLoaded", () => {
//  // 1) 컨트롤러(Thymeleaf)에서 주입한 postId 전역 변수를 가져옴
//  const pid = window.postId;
//  if (!pid) {
//    console.error("postId가 정의되지 않았습니다.");
//    return;
//  }
//
//  // 2) 게시글 읽기 폼 렌더링
//  displayReadForm(pid);
//
//  // 3) 댓글 목록 영역 생성 및 초기 렌더링
//  //    - HTML 템플릿에 <div id="commentList"></div>가 있어야 합니다.
//  fetchComments(pid);
//
//  // 4) 댓글 작성 폼 생성 (HTML 템플릿에 다음 마크업이 포함되어 있어야 합니다.)
//  //    <div id="commentWrite">
//  //      <textarea id="newComment" placeholder="댓글을 입력하세요"></textarea>
//  //      <button id="btnPostComment" type="button">등록</button>
//  //    </div>
//  const $btnPostComment = document.getElementById("btnPostComment");
//  const $newCommentTextarea = document.getElementById("newComment");
//
//  if ($btnPostComment && $newCommentTextarea) {
//    $btnPostComment.addEventListener("click", () => {
//      const content = $newCommentTextarea.value.trim();
//      if (!content) return alert("댓글 내용을 입력하세요.");
//      postComment(pid, content);
//      $newCommentTextarea.value = "";
//    });
//  }
//});
