import { ajax } from '/js/common.js';


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
      <button id="btnEdit" type="button">수정</button>
      <button id="btnDelete" type="button">삭제</button>
    `;

    // 수정 버튼
    $btns.querySelector('#btnEdit')
      .addEventListener('click', () => changeEditMode(frm));

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
        <input type="text" id="title" name="title"/>
      </div>
      <div>
        <label for="nickname">작성자</label>
        <input type="text" id="nickname" name="nickname" readonly/>
      </div>
      <div>
        <label for="content">내용</label>
        <input type="text" id="content" name="content"/>
      </div>
      <div class="btns"></div>
    </form>
  `;
  document.body.insertAdjacentElement('afterbegin', $wrap);

  const frm = document.getElementById('frm');
  changeReadMode(frm);
  getPostBoard(pid, frm);
}

displayReadForm();
