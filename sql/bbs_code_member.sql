----------
--코드 생성
----------
-- 회원 구분 코드
insert into code (code_id,code_name,pcode_id,useyn) values ('M01','회원구분',null,'Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('M0101','일반','M01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('M0102','우수','M01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('M01A1','관리자1','M01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('M01A2','관리자2','M01','Y');

insert into code (code_id,code_name,pcode_id,useyn) values ('A02','지역',null,'Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('A0201','서울','A02','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('A0202','부산','A02','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('A0203','대구','A02','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('A0204','울산','A02','Y');

insert into code (code_id,code_name,pcode_id,useyn) values ('H01','취미',null,'Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('H0101','등산','H01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('H0102','수영','H01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('H0103','골프','H01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('H0104','독서','H01','Y');

insert into code (code_id,code_name,pcode_id,useyn) values ('PB01','게시글',null,'Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('PB0101','일반','PB01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('PB0102','삭제','PB01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('PB0103','비밀','PB01','Y');

insert into code (code_id,code_name,pcode_id,useyn) values ('PC01','댓글',null,'Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('PC0101','일반','PC01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('PC0102','삭제','PC01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('PC0103','비밀','PC01','Y');

insert into code (code_id,code_name,pcode_id,useyn) values ('F01','첨부파일',null,'Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('F0101','회원','F01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('F010101','사진','F0101','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('F0102','게시판','F01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('F010201','첨부파일','F0102','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('F0103','상품관리','F01','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('F010301','설명서','F0103','Y');
insert into code (code_id,code_name,pcode_id,useyn) values ('F010302','이미지','F0103','Y');
--

----------
--회원 생성
----------
--관리자
INSERT INTO MEMBER (member_id,nickname,email,passwd,gubun)
VALUES (member_member_id_seq.nextval,'관리자1','a@kh.com','1234','M01A1');
INSERT INTO MEMBER (member_id,nickname,email,passwd,gubun)
VALUES (member_member_id_seq.nextval,'관리자2','b@kh.com','1234','M01A2');

--일반회원
INSERT INTO MEMBER (member_id,nickname,email,passwd)
VALUES (member_member_id_seq.nextval,'회원1','n1@kh.com','1234');
INSERT INTO MEMBER (member_id,nickname,email,passwd)
VALUES (member_member_id_seq.nextval,'회원2','n2@kh.com','1234');
INSERT INTO MEMBER (member_id,nickname,email,passwd)
VALUES (member_member_id_seq.nextval,'회원3','n3@kh.com','1234');
INSERT INTO MEMBER (member_id,nickname,email,passwd)
VALUES (member_member_id_seq.nextval,'회원4','n4@kh.com','1234');
INSERT INTO MEMBER (member_id,nickname,email,passwd)
VALUES (member_member_id_seq.nextval,'회원5','n5@kh.com','1234');
INSERT INTO MEMBER (member_id,nickname,email,passwd)
VALUES (member_member_id_seq.nextval,'회원6','n6@kh.com','1234');
INSERT INTO MEMBER (member_id,nickname,email,passwd)
VALUES (member_member_id_seq.nextval,'회원7','n7@kh.com','1234');

--우수회원
INSERT INTO MEMBER (member_id,nickname,email,passwd,gubun)
VALUES (member_member_id_seq.nextval,'회원8','n8@kh.com','1234','M0102');
INSERT INTO MEMBER (member_id,nickname,email,passwd,gubun)
VALUES (member_member_id_seq.nextval,'회원9','n9@kh.com','1234','M0102');
--

