-------------
--댓글 작성
-------------
--일반댓글
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, NULL, 0, '댓글1', 1);

INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, NULL, 0, '댓글2', 2);

INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, NULL, 0, '댓글3', 3);

-- 2) depth = 1 (parent = 댓글1)
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 1,    1, '댓글4', 4);

INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 1,    1, '댓글5', 5);

-- 3) depth = 2 (parent = 댓글4)
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 4,    2, '댓글6', 6);

-- 4) depth = 3 (parent = 댓글6)
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 6,    3, '댓글7', 7);

-- 5) 추가답글 (depth = 1 → 2 → 3)
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 2,    1, '댓글8', 8);

INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 8,    2, '댓글9', 9);

INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 9,    3, '댓글10', 10);

-- 1) depth = 0 (댓글11~댓글20)
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, NULL, 0, '댓글11', 1);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, NULL, 0, '댓글12', 1);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, NULL, 0, '댓글13', 1);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, NULL, 0, '댓글14', 1);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, NULL, 0, '댓글15', 1);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, NULL, 0, '댓글16', 2);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, NULL, 0, '댓글17', 2);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, NULL, 0, '댓글18', 2);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, NULL, 0, '댓글19', 2);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, NULL, 0, '댓글20', 2);

-- 2) depth = 1 (댓글21~댓글30), parent = 댓글11~댓글20
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 11,   1, '댓글21', 2);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 12,   1, '댓글22', 3);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 13,   1, '댓글23', 3);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 14,   1, '댓글24', 3);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 15,   1, '댓글25', 3);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 16,   1, '댓글26', 4);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 17,   1, '댓글27', 4);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 18,   1, '댓글28', 4);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 19,   1, '댓글29', 4);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 20,   1, '댓글30', 4);

-- 3) depth = 2 (댓글31~댓글40), parent = 댓글21~댓글30
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 21,   2, '댓글31', 5);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 22,   2, '댓글32', 5);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 23,   2, '댓글33', 5);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 24,   2, '댓글34', 5);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 25,   2, '댓글35', 5);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 26,   2, '댓글36', 5);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 27,   2, '댓글37', 6);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 28,   2, '댓글38', 6);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 29,   2, '댓글39', 6);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 30,   2, '댓글40', 6);

-- 4) depth = 3 (댓글41~댓글50), parent = 댓글31~댓글40
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 31,   3, '댓글41', 7);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 32,   3, '댓글42', 7);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 33,   3, '댓글43', 7);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 34,   3, '댓글44', 7);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 35,   3, '댓글45', 7);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 36,   3, '댓글46', 8);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 37,   3, '댓글47', 8);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 38,   3, '댓글48', 8);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 39,   3, '댓글49', 8);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 40,   3, '댓글50', 8);

-- 5) depth = 1 (댓글51~댓글60), parent = 댓글11~댓글20
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 11,   1, '댓글51', 8);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 12,   1, '댓글52', 9);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 13,   1, '댓글53', 9);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 14,   1, '댓글54', 9);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 15,   1, '댓글55', 9);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 16,   1, '댓글56', 10);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 17,   1, '댓글57', 10);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 18,   1, '댓글58', 10);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 19,   1, '댓글59', 10);
INSERT INTO post_comment (comment_id, post_id, parent_id, depth, content, member_id)
VALUES (post_comment_comment_id_seq.nextval, 110, 20,   1, '댓글60', 10);
