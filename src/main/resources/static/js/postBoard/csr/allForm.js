import { ajax,  PaginationUI} from '/js/common.js';

let currentPage = 1; // 현재 페이지를 위한 전역 변수
let initialPage = 1; // 게시글 추가 후 이동할 페이지 (1페이지)

const recordsPerPage = 10;        // 페이지당 레코드수
const pagesPerPage = 10;          // 한페이지당 페이지수

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

//게시글목록 화면
function displayPostBoardList(postBoards) {

  const makeTr = postBoards => {
    const $tr = postBoards
      .map(
        postBoard =>
          `<tr data-pid=${postBoard.postId}>
            <td>${postBoard.postId}</td>
            <td>${postBoard.title}</td>
            <td>${postBoard.nickname}</td>
            <td>${postBoard.cdate}</td>
            <td>${postBoard.udate}</td></tr>`,
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
          <th>작성자</th>
          <th>작성일</th>
          <th>수정일</th>
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
      location.href = `/csr/boards/${pid}`;
    }),
  );
}

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