import { ajax,  PaginationUI} from '/js/common.js';
let currentPage = 1; // 현재 페이지를 위한 전역 변수
let initialPage = 1; // 게시글 추가 후 이동할 페이지 (1페이지)

const recordsPerPage = 10;        // 페이지당 레코드수
const pagesPerPage = 5;          // 한페이지당 페이지수

const board = document.querySelector(".board");     //게시판 클래스명으로 객체 가져오기
const pid = board.id;                               //id 추출로 게시판 아이디 저장



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
  return null;
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
  return null;
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
      .filter(input => !['postId', 'nickname', 'cdate', 'udate'].includes(input.name))
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
        alert('게시글조회 후 삭제바랍니다.');
        return;
      }

      if (!confirm('삭제하시겠습니까?')) return;
      delPostBoard(pid);
    };
  };

  $readFormWrap.innerHTML = `
    <form id="frm2">

      <div>
          <label for="postId">게시글 아이디</label>
          <input type="text" id="postId" name="postId" value="${postBoard.postId}" readonly/>
      </div>
      <div>
          <label for="title">제목</label>
          <input type="text" id="title" name="title" value="${postBoard.title}" readonly/>
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
          <textarea id="content" name="content" readonly>${postBoard.content}</textarea>
          <span class="field-error client" id="errContent"></span>
      </div>
      <div class='btns'></div>

    </form>
  `;
  const $frm2 = $readFormWrap.querySelector('#frm2');
  changeReadMode($frm2);
}
const $readFormWrap = document.createElement('div');
document.body.appendChild($readFormWrap);
displayReadForm();

//////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////


const $list = document.createElement('div');
document.body.appendChild($list);

const divEle = document.createElement('div');
divEle.setAttribute('id','reply_pagenation');
document.body.appendChild(divEle);


//게시글 조회
const getPostComment = async (pid,commentId) => {
  console.log('pid:', pid, 'cid:', commentId);
  try {
    const url = `/api/postBoards/${pid}/postComment/${commentId}`;
    const result = await ajax.get(url);
    console.log(result);
    if (result.header.rtcd === 'S00') {
      console.log('pid:', pid, 'cid:', commentId);
      return result.body;

    } else if(result.header.rtcd.substr(0,1) == 'E'){
        for(let key in result.header.details){
            console.log(`필드명:${key}, 오류:${result.header.details[key]}`);
        }
        console.log('pid:aa');
        return result.header.details;
    } else {
      alert(result.header.rtmsg);
    }
  } catch (err) {
    console.error(err);
  }
  console.log('pid:az');
  return null;
};

//댓글 저장
const addPostComment = async (frm,comment) => {
  try {
    const url = `/api/postBoards/${pid}/postComment`;
    const result = await ajax.post(url,comment);
    console.log(result);
    if (result.header.rtcd === 'S00') {
      console.log(result.body);
      frm.reset();
      initialPage = 1; // 생성 후 1페이지로 이동
      getPostCommentList(initialPage, recordsPerPage); // 첫 페이지의 기본 레코드로 호출
      configPagination();
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

//댓글 삭제
const delPostComment = async (pid, commentId) => {
  try {
    const url = `/api/postBoards/${pid}/postComment/${commentId}`;
    const result = await ajax.delete(url);
    console.log(result);
    if (result.header.rtcd === 'S00') {
      console.log(result.body);
      getPostCommentList(currentPage, recordsPerPage);
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
const modifyPostComment = async (pid,commentId ,content) => {
  try {
    console.log('modifyPostComment 호출, pid=', commentId,'cid=', pid, 'body=', content);
    const url = `/api/postBoards/${pid}/postComment/${commentId}`;
    const result = await ajax.patch(url, { content });
    if (result.header.rtcd === 'S00') {
      console.log(result.body);
       return result;
    } else if(result.header.rtcd.substr(0,1) == 'E'){
        for(let key in result.header.details){
            console.log(`필드명:${key}, 오류:${result.header.details[key]}`);
        }
        console.log(result.header);
        return result;
    } else {
      alert(result.header.rtmsg);
    }
  } catch (err) {
    console.error(err.message);
  }
};

//댓글목록
const getPostCommentList = async (reqPage, reqRec) => {

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

async function configPagination(){
  const url = `/api/postBoards/${pid}/postComment/totCnt`;
  try {
    const result = await ajax.get(url);

    const totalRecords = result.body; // 전체 레코드수

    const handlePageChange = (reqPage)=>{
      return getPostCommentList(reqPage,recordsPerPage);
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

//댓글목록 화면
async function displayPostCommentList(postComments) {

  const changeCommentEditMode = async cid => {

    const data = await getPostComment(pid, cid);
    console.log('[댓글 데이터 조회 완료]', data);
    const $row       = $list.querySelector(`tr[data-cid="${cid}"]`);
    const $btnCell   = $row.querySelector('.commentBtns');
    const $contentTd = $row.previousElementSibling.children[1];
    const $udateTd = $row.previousElementSibling.children[4];

    $contentTd.innerHTML =
      `<textarea id="editContent-${cid}" rows="3" style="width:98%;">${data.content}</textarea>`;

    $btnCell.innerHTML = `
      <button type="button" class="btnSaveComment">저장</button>
      <button type="button" class="btnCancelComment">취소</button>`;

    $btnCell.querySelector('.btnSaveComment').onclick = async () => {
      const newVal = $contentTd.querySelector('textarea').value.trim();
      const res = await modifyPostComment(pid, cid, newVal);

      if (res.header.rtcd.startsWith('E')) {
        const details = res.header.details;
        if (details.content)  document.querySelector(`#errContent-${cid}`).textContent   = details.content;
        return;
      }
      const udate = res.body.udate;
      $udateTd.textContent=udate;
      document.querySelector(`#errContent-${cid}`).textContent = '';
      changeCommentReadMode(cid);
    };


    $btnCell.querySelector('.btnCancelComment').onclick =
      () => {
      document.querySelector(`#errContent-${cid}`).textContent = '';
      changeCommentReadMode(cid);
      };
  };

  const changeCommentReadMode = async cid => {
    const data = await getPostComment(pid, cid);
    const $row       = $list.querySelector(`tr[data-cid="${cid}"]`);
    const $btnCell   = $row.querySelector('.commentBtns');
    const $contentTd = $row.previousElementSibling.children[1];

    $contentTd.textContent = data.content;

    $btnCell.innerHTML = `
      <button type="button" class="btnEditComment">수정</button>
      <button type="button" class="btnDeleteComment">삭제</button>`;

    $btnCell.querySelector('.btnEditComment').onclick  = () => changeCommentEditMode(cid);
    $btnCell.querySelector('.btnDeleteComment').onclick =
      () => { if (confirm('삭제하시겠습니까?')) delPostComment(pid, cid); };
  };





  const makeTr = postComments => {
    const $tr = postComments
      .map(
        postComment =>
          `<tr id="comment-${postComment.commentId}">
            <td>${postComment.commentId}</td>
            <td>${postComment.content}</td>
            <td>${postComment.nickname}</td>
            <td>${postComment.cdate}</td>
            <td>${postComment.udate}</td></tr>
          <tr data-cid="${postComment.commentId}">
            <td colspan="3"><span class="field-error client" id="errContent-${postComment.commentId}"></span></td>
            <td colspan="2" class="commentBtns" style="text-align: right;">
            <button type="button" class="btnEditComment">수정</button>
            <button type="button" class="btnDeleteComment">삭제</button>
            </td>
          </tr>`,
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
        ${makeTr(postComments)}
      </tbody>
    </table>`;

  const $editBtns = $list.querySelectorAll('.btnEditComment');
  const $delBtns  = $list.querySelectorAll('.btnDeleteComment');

  $editBtns.forEach(btn =>
    btn.onclick = e => {
      const cid = e.target.closest('tr[data-cid]').dataset.cid;
      changeCommentEditMode(cid);
    });

  $delBtns.forEach(btn =>
    btn.onclick = e => {
      const cid = e.target.closest('tr[data-cid]').dataset.cid;
      if (confirm('삭제하시겠습니까?')) delPostComment(pid, cid);
    });
};
configPagination();























