import { ajax,  PaginationUI} from '/js/common.js';
//<script src="/js/main.js" defer></script>
const board = document.querySelector(".board");     //게시판 클래스명으로 객체 가져오기
const pid = board.id;                               //id 추출로 게시판 아이디 저장

let currentPage = 1; // 현재 페이지를 위한 전역 변수
let initialPage = 1; // 게시글 추가 후 이동할 페이지 (1페이지)

const recordsPerPage = 10;        // 페이지당 레코드수
const pagesPerPage = 5;          // 한페이지당 페이지수

//게시글 조회
const getPostBoard = async pid => {
  try {
    const url = `/api/postBoards/${pid}`;
    const result = await ajax.get(url);
    console.log(result);
    if (result.header.rtcd === 'S00') {
        return result.body;

    } else if(result.header.rtcd.substr(0,1) == 'E'){
        for(let key in result.header.details){
            console.log(`필드명:${key}, 오류:${result.header.details[key]}`);
        }
        return result.header.details;
    } else {
      alert(result.header.rtmsg);
    }
  } catch (err) {
    console.error(err);
  }
};

//게시글 삭제
const delPostBoard = async pid => {
  try {
    const url = `/api/postBoards/${pid}`;
    const result = await ajax.delete(url);
    console.log(result);
    if (result.header.rtcd === 'S00') {
      console.log(result.body);
      window.location.href = '/csr/boards';
    } else if(result.header.rtcd.substr(0,1) == 'E'){
        for(let key in result.header.details){
            console.log(`필드명:${key}, 오류:${result.header.details[key]}`);
        }
        return result.header.details;
    } else {
      alert(result.header.rtmsg);
    }
  } catch (err) {
    console.error(err);
  }
};

//게시글 수정
const modifyPostBoard = async (pid, postBoard) => {
  try {
      console.log('modifyPostBoard 호출, pid=', pid, 'body=', postBoard);

    const url = `/api/postBoards/${pid}`;
    const result = await ajax.patch(url, postBoard);
    if (result.header.rtcd === 'S00') {
      console.log(result.body);
       return result;
    } else if(result.header.rtcd.substr(0,1) == 'E'){
        for(let key in result.header.details){
            console.log(`필드명:${key}, 오류:${result.header.details[key]}`);
        }
        return result;
    } else {
      alert(result.header.rtmsg);
    }
  } catch (err) {
    console.error(err.message);
  }
};

//게시글조회 화면
async function displayReadForm() {
  const postBoard = await getPostBoard(pid);

  //상태 : 조회 mode-read, 편집 mode-edit
  const changeEditMode = frm => {
    frm.classList.toggle('mode-edit', true);
    frm.classList.toggle('mode-read', false);
    [...frm.querySelectorAll('input,textarea')]
      .filter(input => !['postId', 'nickname', 'cdate', 'udate'].includes(input.name))
      .forEach(input => input.removeAttribute('readonly'));

    const $btns = frm.querySelector('.btns');
    $btns.innerHTML = `
      <button id="btnSave" type="button">저장</button>
      <button id="btnCancel" type="button">취소</button>
    `;

    const $btnSave = $btns.querySelector('#btnSave');
    const $btnCancel = $btns.querySelector('#btnCancel');

    //저장
    $btnSave.onclick = async e => {
      frm.querySelector('#errTitle').textContent = '';
      frm.querySelector('#errContent').textContent = '';

      const formData = new FormData(frm); //폼데이터가져오기
      const postBoard = {};

      [...formData.keys()].forEach(
        ele => (postBoard[ele] = formData.get(ele))
      );

      const result = await modifyPostBoard(pid, postBoard);

      if (result.header.rtcd.startsWith('E')) {
        const details = result.header.details;
        if (details.title)  frm.querySelector('#errTitle').textContent   = details.title;
        if (details.content) frm.querySelector('#errContent').textContent = details.content;
        return;
      }
      const udate = result.body.udate;
      frm.querySelector('#udate"]').value = udate; //수정
      changeReadMode(frm); //읽기모드
    };

    //취소
    $btnCancel.addEventListener('click', e => {
      frm.reset(); //초기화
      changeReadMode(frm);
    });
  };


  const changeReadMode = frm => {
    frm.classList.toggle('mode-read', true);
    frm.classList.toggle('mode-edit', false);
    [...frm.querySelectorAll('input,textarea')]
      .filter(input => input.name !== 'postId')
      .forEach(input => input.setAttribute('readonly', ''));

    const $btns = frm.querySelector('.btns');
    $btns.innerHTML = `
      <button id="btnEdit" type="button">수정</button>
      <button id="btnDelete" type="button">삭제</button>
    `;

    const $btnDelete = $btns.querySelector('#btnDelete');
    const $btnEdit = $btns.querySelector('#btnEdit');

    //수정
    $btnEdit.onclick = () => changeEditMode(frm);

    //삭제
    $btnDelete.onclick = ()  => {
      const pid = frm.postId.value;
      if (!pid) {
        alert('상품조회 후 삭제바랍니다.');
        return;
      }

      if (!confirm('삭제하시겠습니까?')) return;
      delPostBoard(pid);
    };
  };

  const $readFormWrap = document.createElement('div');
  $readFormWrap.innerHTML = `
    <form id="frm2">

      <div>
          <label for="postId">게시글 아이디</label>
          <input type="text" id="postId" name="postId" value="${postBoard.postId}" readonly/>
      </div>
      <div>
          <label for="title">제목</label>
          <input type="text" id="title" name="title" value="${postBoard.title}"/>
          <span class="field-error client" id="errTitle"></span>
      </div>
      <div>
          <label for="nickname">작성자</label>
          <input type="text" id="nickname" name="nickname" value="${postBoard.nickname}" readonly/>
      </div>
      <div>
          <label for="cdate">작성일</label>
          <input type="text" id="cdate" name="cdate" value="${postBoard.cdate}" readonly/>
      </div>
      <div>
          <label for="udate">수정일</label>
          <input type="text" id="udate" name="udate" value="${postBoard.udate}" readonly/>
      </div>
      <div>
          <label for="content">내용</label>
          <textarea id="content" name="content">${postBoard.content}</textarea>
          <span class="field-error client" id="errContent"></span>
      </div>
      <div class='btns'></div>

    </form>
  `;
  document.body.insertAdjacentElement('afterbegin', $readFormWrap);
  const $frm2 = $readFormWrap.querySelector('#frm2');
  changeReadMode($frm2);
}

///////////////////////////////////////////////////////////////////////////////////////////////



//댓글 삭제
const delPostComment = async (pid, commentId) => {
  try {
    const url = `/api/postBoards/${pid}/postComment/${commentId}`;
    const result = await ajax.delete(url);
    console.log(result);
    if (result.header.rtcd === 'S00') {
      console.log(result.body);
      getPostComment(currentPage, recordsPerPage);
    } else if(result.header.rtcd.substr(0,1) == 'E'){
        for(let key in result.header.details){
            console.log(`필드명:${key}, 오류:${result.header.details[key]}`);
        }
    } else {
      alert(result.header.rtmsg);
    }
  } catch (err) {
    console.error(err);
  }
};

//댓글 수정
const modifyPostComment = async (pid,commentId ,postComment) => {
  try {
      console.log('modifyPostComment 호출, pid=','cid=', commentId, pid, 'body=', postComment);

    const url = `/api/postBoards/${pid}/postComment/${commentId}`;
    const result = await ajax.patch(url, postComment);
    if (result.header.rtcd === 'S00') {
      console.log(result.body);
       return result;
    } else if(result.header.rtcd.substr(0,1) == 'E'){
        for(let key in result.header.details){
            console.log(`필드명:${key}, 오류:${result.header.details[key]}`);
        }
        return result;
    } else {
      alert(result.header.rtmsg);
    }
  } catch (err) {
    console.error(err.message);
  }
};

//댓글목록
const getPostComment = async (reqPage, reqRec) => {

  try {
    const url = `/api/postBoards/${pid}/postComment/paging?pageNo=${reqPage}&numOfRows=${reqRec}`;
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

function displayReadForm() {
  //상태 : 조회 mode-read, 편집 mode-edit
  const changeEditModeComment = (cid,frm) => {
    frm.classList.toggle('mode-edit-comment', true);
    frm.classList.toggle('mode-read-comment', false);

      // 기존 다른 댓글의 버튼 비우기
      frm.querySelectorAll('.btns').forEach($td => {
        if ($td.dataset.cid !== cid) {
          $td.innerHTML = '';
        }
      });

    const $label = frm.querySelector(`label.comment-content[data-cid="${cid}"]`);
    const text = $label.textContent;

      // input 요소 생성
    const $input = document.createElement('input');
    $input.type = 'text';
    $input.name = 'content';
    $input.value = text;
    $input.classList.add('input-comment');

    $label.replaceWith($input);

    const $btnsCell = frm.querySelector(`.btns[data-cid="${cid}"]`);
    $btnsCell.innerHTML = `
      <button id="btnCommentSave" type="button">저장</button>
      <button id="btnCommentCancel" type="button">취소</button>
    `;

    const $btnSave = $btns.querySelector('#btnCommentSave');
    const $btnCancel = $btns.querySelector('#btnCommentCancel');

    //저장
    $btnSave.addEventListener('click', e => {
      const content = frm.querySelector('input[name="content"]').value;
      const commentId = frm.querySelector('input[name="commentId"]').value;

      modifyPostComment(pid, commentId, postComment); //수정
      changeReadModeComment(frm); //읽기모드
    });

    //취소
    $btnCancel.addEventListener('click', e => {
      frm.reset(); //초기화
      changeReadModeComment(frm);
    });
  };

  const changeReadMode = frm => {
    frm.classList.toggle('mode-read-comment', true);
    frm.classList.toggle('mode-edit-comment', false);

  // 기존 다른 댓글의 버튼 비우기
    frm.querySelectorAll('.btns').forEach($td => {
      $td.innerHTML = `
         <button id="btnCommentSave" type="button">수정</button>
         <button id="btnCommentCancel" type="button">삭제</button>
      `;
    });

    const $btns = frm.querySelector('.btns');
    $btns.innerHTML = `
      <button id="btnEdit" type="button">수정</button>
      <button id="btnDelete" type="button">삭제</button>
    `;

    const $btnDelete = $btns.querySelector('#btnDelete');
    const $btnEdit = $btns.querySelector('#btnEdit');

    //수정
    $btnEdit.addEventListener('click', e => {
      changeEditMode(frm);
    });

    //삭제
    $btnDelete.addEventListener('click', e => {
      const pid = frm.productId.value;
      if (!pid) {
        alert('상품조회 후 삭제바랍니다.');
        return;
      }

      if (!confirm('삭제하시겠습니까?')) return;
      delProduct(pid, frm);
    });
  };
}






















































//댓글목록 화면
function displayPostCommentList(postComment) {

  const makeTr = postComment => {
    const $tr = postComment
      .map(
        postComment =>
          `<tr data-cid=${postComment.commentId}>
            <td>${postComment.commentId}</td>
            <td>${postComment.content}</td>
            <td>${postComment.nickname}</td>
            <td>${postComment.cdate}</td>
            <td>${postComment.udate}</td></tr>
            <tr>
            <td><span class="field-error client" id="errCommentSave"></span></td>
            <td><span class="field-error client" id="errCommentDel"></span></td>
            <td>
              <button class="btnEditComment">수정</button>
              <button class="btnDeleteComment">삭제</button>
            </td></tr>`,
      )
      .join('');
    return $tr;
  };
  const $commentFormWrap = document.createElement('div');
  $commentFormWrap.innerHTML = `
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

  const $postComment = $commentFormWrap.querySelectorAll('table tbody tr');
  attachCommentHandlers();
}

$commentFormWrap.setAttribute('id','list')
document.body.appendChild($commentFormWrap);

const divEle = document.createElement('div');
divEle.setAttribute('id','reply_pagenation');
document.body.appendChild(divEle);

async function configPagination(){
  const url = `/api/postBoards/${pid}/postComment/totCnt`;
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

// 버튼 클릭 핸들러 연결 함수
function attachCommentHandlers() {
  document.querySelectorAll('#list tbody tr').forEach($tr => {
    const commentId = $tr.dataset.cid;

    // 삭제 버튼
    $tr.querySelector('.btnDeleteComment').onclick = () => {
      if (!confirm('댓글을 정말 삭제하시겠습니까?')) return;
      delPostComment(pid, commentId);
    };

    // 수정 버튼 (예시: prompt로 새 내용 입력)
    $tr.querySelector('.btnEditComment').onclick = async () => {
      frm.querySelector('#errCommentSave').textContent = '';
      frm.querySelector('#errCommentDel').textContent = '';

      const formData = new FormData(frm); //폼데이터가져오기
      const postBoard = {};

      [...formData.keys()].forEach(
        ele => (postBoard[ele] = formData.get(ele))
      );

      const result = await modifyPostBoard(postBoard.postId, postBoard);

      if (result.header.rtcd.startsWith('E')) {
        const details = result.header.details;
        if (details.title)  frm.querySelector('#errTitle').textContent   = details.title;
        if (details.content) frm.querySelector('#errContent').textContent = details.content;
        return;
      }
      const udate = result.body.udate;
      frm.querySelector('input[name="udate"]').value = udate; //수정
      changeReadMode(frm); //읽기모드
    };
  });
}


































