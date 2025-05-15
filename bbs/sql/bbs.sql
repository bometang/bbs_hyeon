--테이블 삭제
drop table postBoard;


--시퀀스삭제
drop sequence postBoard_post_id_seq;

-------
--게시판
-------
create table postBoard(
    post_id 	  number(10) PRIMARY KEY,		          --게시글 아이디
    title				varchar2(100) NOT null,									--제목
    content			clob NOT null,													--내용
    user_name   varchar2(20) NOT null,									--작성자
    cdate       timestamp default systimestamp,         --작성날짜
    udate       timestamp 										          --수정날짜
);


CREATE SEQUENCE postBoard_post_id_seq;


INSERT INTO postBoard (post_id, title, content, user_name)
VALUES (postBoard_post_id_seq.nextval, '제목1', '내용1', '유저1');
INSERT INTO postBoard (post_id, title, content, user_name)
VALUES (postBoard_post_id_seq.nextval, '제목2', '내용2', '유저2');
INSERT INTO postBoard (post_id, title, content, user_name)
VALUES (postBoard_post_id_seq.nextval, '제목3', '내용3', '유저3');
INSERT INTO postBoard (post_id, title, content, user_name)
VALUES (postBoard_post_id_seq.nextval, '제목4', '내용4', '유저4');
INSERT INTO postBoard (post_id, title, content, user_name)
VALUES (postBoard_post_id_seq.nextval, '제목5', '내용5', '유저5');
INSERT INTO postBoard (post_id, title, content, user_name)
VALUES (postBoard_post_id_seq.nextval, '제목6', '내용6', '유저6');
INSERT INTO postBoard (post_id, title, content, user_name)
VALUES (postBoard_post_id_seq.nextval, '제목7', '내용7', '유저7');

SELECT post_id, title, user_name,TO_CHAR(cdate, 'YYYY-MM-DD HH24:MI') AS cdate
FROM postBoard
ORDER BY post_id desc;

