import { ajax,  PaginationUI} from '/js/common.js';

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
      displayReadForm(result.body);
//      postId.setAttribute('value', result.body.postId);
//      title.setAttribute('value', result.body.title);
//      nickname.setAttribute('value', result.body.nickname);
//      content.setAttribute('value', result.body.content);
//      cdate.setAttribute('value', result.body.cdate);
//      udate.setAttribute('value', result.body.udate);
//
//      postId.value = result.body.postId;
//      title.value = result.body.title;
//      nickname.value = result.body.nickname;
//      content.value = result.body.content;
//      cdate.value = result.body.cdate;
//      udate.value = result.body.udate;

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

//게시글 삭제
const delPostBoard = async (pid, frm) => {
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
    } else {
      alert(result.header.rtmsg);
    }
  } catch (err) {
    console.error(err);
  }
};


//게시글 수정
const modifyPostBoard = async (pid, PostBoard) => {
  try {
      console.log('modifyPostBoard 호출, pid=', pid, 'body=', postBoard);

    const url = `/api/postBoards/${pid}`;
    const result = await ajax.patch(url, PostBoard);
    if (result.header.rtcd === 'S00') {
      console.log(result.body);
    } else if(result.header.rtcd.substr(0,1) == 'E'){
        for(let key in result.header.details){
            console.log(`필드명:${key}, 오류:${result.header.details[key]}`);
        }
    } else {
      alert(result.header.rtmsg);
    }
  } catch (err) {
    console.error(err.message);
  }
};

//게시글조회 화면
function displayReadForm(postBoard) {
  //상태 : 조회 mode-read, 편집 mode-edit
  const changeEditMode = frm => {
    frm.classList.toggle('mode-edit', true);
    [...frm.querySelectorAll('input,textarea')]
      .filter(input => input.name !== 'postId')
      .forEach(input => input.removeAttribute('readonly'));

    const $btns = frm.querySelector('.btns');
    $btns.innerHTML = `
      <button id="btnSave" type="button">저장</button>
      <button id="btnCancel" type="button">취소</button>
    `;

    const $btnSave = $btns.querySelector('#btnSave');
    const $btnCancel = $btns.querySelector('#btnCancel');

    //저장
    $btnSave.addEventListener('click', e => {
      const formData = new FormData(frm); //폼데이터가져오기
      const postBoard = {};

      [...formData.keys()].forEach(
        ele => (postBoard[ele] = formData.get(ele)),
      );

      modifyPostBoard(postBoard.postId, postBoard); //수정
      changeReadMode(frm); //읽기모드
    });

    //취소
    $btnCancel.addEventListener('click', e => {
      frm.reset(); //초기화
      changeReadMode(frm);
    });
  };

  const changeReadMode = frm => {
    frm.classList.toggle('mode-read', true);
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
      delPostBoard(pid, frm);
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
      </div>
      <div class='btns'></div>

    </form>
  `;
  document.body.insertAdjacentElement('afterbegin', $readFormWrap);
  const $frm2 = $readFormWrap.querySelector('#frm2');
  changeReadMode($frm2);
}

document.addEventListener('DOMContentLoaded', () => {
  const raw = document.getElementById('rawPostId').value;
  const pid = Number(raw);
  if (pid) {
    getPostBoard(pid);
  }
});
