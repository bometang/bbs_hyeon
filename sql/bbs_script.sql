SELECT * FROM code;
SELECT * FROM MEMBER;
SELECT * FROM postBoard;
SELECT * FROM post_comment;

INSERT INTO POSTBOARD (post_id,title,content,member_id,code_id) 
VALUES (postBoard_post_id_seq.nextval, :title, :content, 1,'pb01N');



SELECT post_id,title, (SELECT nickname FROM MEMBER WHERE p.member_id = member_id) AS member_id,TO_CHAR(cdate, 'YYYY-MM-DD HH24:MI') AS cdate 
FROM POSTBOARD p;

SELECT 
  p.post_id,
  p.title,
  m.nickname    AS member_id,
  TO_CHAR(p.cdate, 'YYYY-MM-DD HH24:MI') AS cdate
FROM postboard p
JOIN member   m
  ON p.member_id = m.member_id
ORDER BY post_id DESC;


INSERT INTO POSTBOARD (post_id,title,content,member_id,code_id) 
VALUES (postBoard_post_id_seq.nextval,'제목1', '내용1', 1,'pb01N');


SELECT post_id,title,p.member_id AS member_id, TO_CHAR(p.cdate, 'YYYY-MM-DD HH24:MI') AS cdate, TO_CHAR(p.udate, 'YYYY-MM-DD HH24:MI') AS udate, content, p.code_id AS code_id 
FROM postBoard p
JOIN MEMBER m ON p.member_id = m.member_id
WHERE p.POST_ID=2; 

SELECT member_id, nickname, email, passwd,code_id,cdate,udate
FROM MEMBER
WHERE member_id = 1;

DELETE FROM postBoard WHERE post_id=1;

