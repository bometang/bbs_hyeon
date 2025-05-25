----------
--코드 생성
----------
-- 회원 구분 코드
insert into code (code_id,code_name,pcode_id,useyn) values ('M01', '회원구분', null, 'Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('M01N','일반 회원','M01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('M01D','탈퇴 회원','M01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('M01A','관리자',   'M01','Y');

-- 게시판 글 상태 코드
insert into code (code_id,code_name,pcode_id,useyn) values ('pb01', '게시판',null,  'Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('pb01N','일반글','pb01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('pb01D','삭제글','pb01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('pb01S','비밀글','pb01','Y');

-- 게시판 댓글 상태 코드
insert into code (code_id,code_name,pcode_id,useyn) values ('pc01', '게시판 댓글',null, 'Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('pc01N','일반댓글',  'pc01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('pc01D','삭제댓글',  'pc01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('pc01S','비밀댓글',  'pc01','Y');
--

----------
--회원 생성
----------
--관리자
INSERT INTO MEMBER (member_id,nickname,email,passwd,code_id) 
VALUES (member_member_id_seq.nextval,'관리자1','a@kh.com','1234','M01A');

--일반회원
INSERT INTO MEMBER (member_id,nickname,email,passwd,code_id) 
VALUES (member_member_id_seq.nextval,'회원1','n1@kh.com','1234','M01N');
INSERT INTO MEMBER (member_id,nickname,email,passwd,code_id) 
VALUES (member_member_id_seq.nextval,'회원2','n2@kh.com','1234','M01N');
INSERT INTO MEMBER (member_id,nickname,email,passwd,code_id) 
VALUES (member_member_id_seq.nextval,'회원3','n3@kh.com','1234','M01N');
INSERT INTO MEMBER (member_id,nickname,email,passwd,code_id) 
VALUES (member_member_id_seq.nextval,'회원4','n4@kh.com','1234','M01N');
INSERT INTO MEMBER (member_id,nickname,email,passwd,code_id) 
VALUES (member_member_id_seq.nextval,'회원5','n5@kh.com','1234','M01N');
INSERT INTO MEMBER (member_id,nickname,email,passwd,code_id) 
VALUES (member_member_id_seq.nextval,'회원6','n6@kh.com','1234','M01N');
INSERT INTO MEMBER (member_id,nickname,email,passwd,code_id) 
VALUES (member_member_id_seq.nextval,'회원7','n7@kh.com','1234','M01N');
INSERT INTO MEMBER (member_id,nickname,email,passwd,code_id) 
VALUES (member_member_id_seq.nextval,'회원8','n8@kh.com','1234','M01N');
INSERT INTO MEMBER (member_id,nickname,email,passwd,code_id) 
VALUES (member_member_id_seq.nextval,'회원9','n9@kh.com','1234','M01N');
--

