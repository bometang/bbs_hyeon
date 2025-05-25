--테이블 삭제
drop table post_comment;
drop table postBoard;
drop table member;
DROP TABLE code;

--시퀀스삭제
drop sequence post_comment_comment_id_seq;
drop sequence postBoard_post_id_seq;
drop sequence member_member_id_seq;

-------
--코드
-------
create table code(
    code_id     varchar2(11) PRIMARY KEY,       --코드
    code_name   varchar2(30) NOT null,          --코드명
    discript    clob,                           --코드설명
    pcode_id    varchar2(11),                   --상위코드
    useyn       char(1) default 'Y' NOT null,   --사용여부 (사용:'Y',미사용:'N')
    cdate       timestamp default systimestamp,
    udate       timestamp
);

-------
--회원관리
-------
CREATE TABLE member (
  member_id  NUMBER(10)     PRIMARY KEY,     	   	  -- 회원 식별자
  nickname   VARCHAR2(30)   UNIQUE NOT NULL,      	  -- 사용자명
  email      VARCHAR2(20)   UNIQUE NOT NULL,       	  -- 아이디
  passwd     VARCHAR2(15)   NOT NULL,    			  -- 비밀번호
  code_id    varchar2(11)   DEFAULT 'M01N' NOT NULL,  -- 상태값 제약: M01N=일반회원, M01D=탈퇴회원, M01A=관리자
  cdate      TIMESTAMP      DEFAULT SYSTIMESTAMP,
  udate      TIMESTAMP
);

-------
--게시판
-------
create table postBoard(
    post_id 	number(10)    PRIMARY KEY,		          	--게시글 아이디
    title		varchar2(100) NOT null,						--제목
    content		clob          NOT null,						--내용
    member_id   number(10)    NOT null,						--작성자
    code_id     varchar2(11)  DEFAULT 'pb01N' NOT NULL,		--상태값 제약: pb01N=정상, pb01D=삭제, pb01S=비밀
    cdate       timestamp     default systimestamp,     	--작성날짜
    udate       timestamp 									--수정날짜
);

-------
--게시판 댓글
-------
CREATE TABLE post_comment (
  comment_id   NUMBER(10)   PRIMARY KEY,					-- 댓글 아이디
  post_id      NUMBER(10)   NOT NULL,						-- 게시글 아이디
  parent_id    NUMBER(10),                					-- 부모 댓글 아이디
  depth        NUMBER(1)    DEFAULT 0 CHECK(DEPTH<=3),	    -- 대댓글 갯수
  content      CLOB         NOT NULL,						-- 내용
  member_id    NUMBER(10)   NOT NULL,						-- 작성자 아이디
  code_id      varchar2(11)  DEFAULT 'pc01N' NOT NULL,	    --상태값 제약: pc01N=정상, pc01D=삭제, pc01S=비밀
  cdate        TIMESTAMP    DEFAULT SYSTIMESTAMP,			-- 작성 날짜
  udate        TIMESTAMP									-- 수정 날짜
);

--시퀸스 생성
CREATE SEQUENCE member_member_id_seq;
CREATE SEQUENCE postBoard_post_id_seq;
CREATE SEQUENCE post_comment_comment_id_seq;


---------
-- FK설정
---------

--부모 코드 외래키 지정
alter table code 
add constraint bbs_pcode_id_fk
foreign key(pcode_id) 
references code(code_id);
--

-- 상태코드 외래키 지정
ALTER TABLE member
ADD CONSTRAINT fk_mm_decode
FOREIGN KEY (code_id)
REFERENCES code(code_id);
--

-- 작성자 아이디 외래키 지정
ALTER TABLE postBoard
ADD CONSTRAINT fk_pb_user
FOREIGN KEY (member_id)
REFERENCES member(member_id);

-- 상태코드 외래키 지정
ALTER TABLE postBoard
ADD CONSTRAINT fk_pb_decode
FOREIGN KEY (code_id)
REFERENCES code(code_id);
--

-- 게시글 아이디 외래키 지정
ALTER TABLE post_comment 
ADD CONSTRAINT fk_pc_post 
FOREIGN KEY (post_id) 
REFERENCES postBoard(post_id)
ON DELETE CASCADE;

--부모 댓글 아이디 외래키 지정
ALTER TABLE post_comment
ADD CONSTRAINT fk_pc_parent
FOREIGN KEY (parent_id)
REFERENCES post_comment(comment_id);

-- 작성자 아이디 외래키 지정
ALTER TABLE post_comment
ADD CONSTRAINT fk_pc_user
FOREIGN KEY (member_id)
REFERENCES member(member_id);

-- 상태코드 외래키 지정
ALTER TABLE post_comment
ADD CONSTRAINT fk_pc_decode
FOREIGN KEY (code_id)
REFERENCES code(code_id);
--

--------------
--코드 제약조건
--------------
alter table code add constraint code_useyn_ck check(useyn in ('Y','N'));
--