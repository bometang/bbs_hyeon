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
--외래키
alter table code add constraint bbs_pcode_id_fk
    foreign key(pcode_id) references code(code_id);

--제약조건
alter table code add constraint code_useyn_ck check(useyn in ('Y','N'));


-------
--회원관리
-------
CREATE TABLE member (
  member_id  NUMBER(10)     PRIMARY KEY,     	   	  -- 회원 식별자
  nickname   VARCHAR2(30)   UNIQUE NOT NULL,      	  -- 사용자명
  email      VARCHAR2(50)   UNIQUE NOT NULL,       	  -- 아이디
  passwd     VARCHAR2(15)   NOT NULL,    			  -- 비밀번호
  tel        VARCHAR2(13),                            --연락처 ex)010-1234-5678
  gender     VARCHAR2(6),                             --성별
  hobby      VARCHAR2(300),                           --취미
  region     VARCHAR2(11),                            --지역
  gubun      VARCHAR2(11)   default 'M0101',          --회원구분 (일반,우수,관리자..)
  pic         blob,                                   --사진
  cdate      TIMESTAMP      DEFAULT SYSTIMESTAMP,
  udate      TIMESTAMP
);
--외래키
alter table member add constraint member_region_fk
    foreign key(region) references code(code_id);
alter table member add constraint member_gubun_fk
    foreign key(gubun) references code(code_id);

--제약조건
alter table member add constraint member_gender_ck check (gender in ('남자','여자'));

--시퀀스
create sequence member_member_id_seq;

-------
--게시판
-------
create table postBoard(
    post_id 	number(10)    PRIMARY KEY,		          	--게시글 아이디
    title		varchar2(100) NOT null,						--제목
    content		clob          NOT null,						--내용
    member_id   number(10)    ,						        --작성자
    code_id     varchar2(11)  DEFAULT 'PB0101',        	    --상태값 제약: PB0101=정상, PB0102=삭제, PB0103=비밀
    cdate       timestamp     default systimestamp,     	--작성날짜
    udate       timestamp 									--수정날짜
);
-- 작성자 아이디 외래키 지정
ALTER TABLE postBoard
ADD CONSTRAINT fk_pb_user
FOREIGN KEY (member_id)
REFERENCES member(member_id)
ON DELETE SET NULL;

-- 상태코드 외래키 지정
ALTER TABLE postBoard
ADD CONSTRAINT fk_pb_decode
FOREIGN KEY (code_id)
REFERENCES code(code_id)
ON DELETE SET NULL;
--

--시퀸스 생성
CREATE SEQUENCE postBoard_post_id_seq;

-------
--게시판 댓글
-------
CREATE TABLE post_comment (
  comment_id   NUMBER(10)    PRIMARY KEY,				-- 댓글 아이디
  post_id      NUMBER(10)    NOT NULL,					-- 게시글 아이디
  parent_id    NUMBER(10),                				-- 부모 댓글 아이디
  depth        NUMBER(1)     DEFAULT 0 CHECK(DEPTH<=3),	-- 대댓글 갯수
  content      CLOB          NOT NULL,					-- 내용
  member_id    NUMBER(10)    NOT NULL,					-- 작성자 아이디
  code_id      varchar2(11)  DEFAULT 'PC0101' NOT NULL,	--상태값 제약: PC0101=정상, PC0102=삭제, PC0103=비밀
  cdate        TIMESTAMP     DEFAULT SYSTIMESTAMP,		-- 작성 날짜
  udate        TIMESTAMP
);

--시퀸스 생성
CREATE SEQUENCE post_comment_comment_id_seq;


---------
-- FK설정
---------

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
