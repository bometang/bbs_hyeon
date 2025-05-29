import { ajax,  PaginationUI} from '/js/common.js';

let currentPage = 1; // 현재 페이지를 위한 전역 변수
let initialPage = 1; // 게시글 추가 후 이동할 페이지 (1페이지)

const recordsPerPage = 10;        // 페이지당 레코드수
const pagesPerPage = 10;          // 한페이지당 페이지수

//게시글등록
const addPostBoard = async postBoard => {
  try {
    const url = '/api/postBoards';
    const result = await ajax.post(url, postBoard);
    if (result.header.rtcd === 'S00') {
      console.log(result.body);
      frm.reset();
      initialPage = 1; // 생성 후 1페이지로 이동
      getPostBoards(initialPage, recordsPerPage); // 첫 페이지의 기본 레코드로 호출
      configPagination();
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

//게시글조회
const getPostBoard = async pid => {
  try {
    const url = `/api/postBoards/${pid}`;
    const result = await ajax.get(url);
    console.log(result);
    if (result.header.rtcd === 'S00') {
      console.log(result.body);
      // postId2.value = result.body.postId;
      postId2.setAttribute('value', result.body.postId);
      title2.setAttribute('value', result.body.title);
      nickname2.setAttribute('value', result.body.nickname);
      cdate2.setAttribute('value', result.body.cdate);
      content2.setAttribute('value', result.body.content);

      postId2.value = result.body.postId;
      title2.value = result.body.title;
      nickname2.value =  result.body.nickname;
      cdate2.value = result.body.cdate;
      content2.value = result.body.content;

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

//게시글삭제
const delPostBoard = async (pid, frm) => {
  try {
    const url = `/api/postBoards/${pid}`;
    const result = await ajax.delete(url);
    console.log(result);
    if (result.header.rtcd === 'S00') {
      console.log(result.body);
      const $inputs = frm.querySelectorAll('input');
      [...$inputs].forEach(ele => (ele.value = '')); //폼필드 초기화
      getPostBoards(currentPage, recordsPerPage); // 현재 페이지의 기본 레코드로 호출
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

//게시글수정
const modifyPostBoard = async (pid, postBoard) => {
  try {
    const url = `/api/postBoards/${pid}`;
    const result = await ajax.patch(url, postBoard);
    if (result.header.rtcd === 'S00') {
      console.log(result.body);
      getPostBoards(currentPage, recordsPerPage); // 현재 페이지의 기본 레코드로 호출
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

//게시글목록
const getPostBoards = async (reqPage, reqRec) => {

  try {
    const url = `/api/postBoards/paging?pageNo=${reqPage}&numOfRows=${reqRec}`;
    const result = await ajax.get(url);

    if (result.header.rtcd === 'S00') {
      currentPage = reqPage; // 현재 페이지 업데이트
      displayPostBoardList(result.body);

    } else {
      alert(result.header.rtmsg);
    }
  } catch (err) {
    console.error(err);
  }
};

//게시글등록 화면
function displayForm() {
  //게시글등록
  const $addFormWrap = document.createElement('div');
  $addFormWrap.innerHTML = `
    <form id="frm">
      <div>
          <label for="title">제목</label>
          <input type="text" id="title" name="title"/>
          <span class="field-error client" id="errTitle"></span>
      </div>
      <div>
          <label for="nickname">작성자</label>
          <input type="text" id="nickname" name="nickname" readonly>
      </div>
      <div>
          <label for="cdate">작성일</label>
          <input type="text" id="cdate" name="cdate" readonly>
      </div>
      <div>
          <label for="content">내용</label>
          <input type="text" id="content" name="content"/>
          <span class="field-error client" id="errContent"></span>
      </div>
      <div>
          <button id="btnAdd" type="submit">등록</button>
      </div>
    </form>
  `;
  document.body.insertAdjacentElement('afterbegin', $addFormWrap);
  const $frm = $addFormWrap.querySelector('#frm');
  $frm.addEventListener('submit', e => {
    e.preventDefault(); // 기본동작 중지

    //유효성 체크
    if($frm.title.value.trim().length === 0) {
      errTitle.textContent = '제목은 필수 입니다';
      $frm.title.focus();
      return;
    }
    if($frm.content.value.trim().length === 0) {
      errContent.textContent = '내용 필수 입니다';
      $frm.content.focus();
      return;
    }

    const formData = new FormData(e.target); //폼데이터가져오기
    const postBoard = {};
    [...formData.keys()].forEach(
      ele => (postBoard[ele] = formData.get(ele)),
    );

    addPostBoard(postBoard);

  });
}

//게시글조회 화면
function displayReadForm() {
  //상태 : 조회 mode-read, 편집 mode-edit
  const changeEditMode = frm => {
    frm.classList.toggle('mode-edit', true);
    [...frm.querySelectorAll('input')]
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
      getPostBoard(postBoard.postId); //조회
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
    [...frm.querySelectorAll('input')]
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
    $btnEdit.addEventListener('click', e => {
      changeEditMode(frm);
    });

    //삭제
    $btnDelete.addEventListener('click', e => {
      const pid = frm.postId.value;
      if (!pid) {
        alert('게시글조회 후 삭제바랍니다.');
        return;
      }

      if (!confirm('삭제하시겠습니까?')) return;
      delPostBoard(pid, frm);
    });
  };

  const $readFormWrap = document.createElement('div');
  $readFormWrap.innerHTML = `
    <form id="frm2">

      <div>
          <label for="postId2">게시글아이디</label>
          <input type="text" id="postId2" name="postId" readonly/>
      </div>
      <div>
          <label for="title">제목</label>
          <input type="text" id="title2" name="title"/>
      </div>
      <div>
          <label for="nickname">작성자</label>
          <input type="text" id="nickname2" name="nickname"/>
      </div>
      <div>
          <label for="content">내용</label>
          <input type="text" id="content2" name="content"/>
      </div>
      </div>
      <div class='btns'></div>

    </form>
  `;
  document.body.insertAdjacentElement('afterbegin', $readFormWrap);
  const $frm2 = $readFormWrap.querySelector('#frm2');
  changeReadMode($frm2);
}

//게시글목록 화면
function displayPostBoardList(postBoards) {

  const makeTr = postBoards => {
    const $tr = postBoards
      .map(
        postBoard =>
          `<tr data-pid=${postBoard.postId}>
            <td>${postBoard.postId}</td>
            <td>${postBoard.title}</td></tr>`,
      )
      .join('');
    return $tr;
  };

  $list.innerHTML = `
    <table>
      <caption> 게 시 글 목 록 </caption>
      <thead>
        <tr>
          <th>게시글번호</th>
          <th>제목</th>
        </tr>
      </thead>
      <tbody>
        ${makeTr(postBoards)}
      </tbody>
    </table>`;

  const $postBoards = $list.querySelectorAll('table tbody tr');

  // Array.from($postBoards)
  [...$postBoards].forEach(postBoard =>
    postBoard.addEventListener('click', e => {
      const pid = e.currentTarget.dataset.pid;
      getPostBoard(pid);
    }),
  );
}

displayReadForm(); //조회
displayForm();//등록
//getPostBoards();//목록

const $list = document.createElement('div');
$list.setAttribute('id','list')
document.body.appendChild($list);

const divEle = document.createElement('div');
divEle.setAttribute('id','reply_pagenation');
document.body.appendChild(divEle);

async function configPagination(){
  const url = '/api/postBoards/totCnt';
  try {
    const result = await ajax.get(url);

    const totalRecords = result.body; // 전체 레코드수

    const handlePageChange = (reqPage)=>{
      return getPostBoards(reqPage,recordsPerPage);
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
configPagination();