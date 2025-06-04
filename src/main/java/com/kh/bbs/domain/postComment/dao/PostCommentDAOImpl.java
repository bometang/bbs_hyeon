package com.kh.bbs.domain.postComment.dao;

import com.kh.bbs.domain.entity.PostComment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PostCommentDAOImpl implements PostCommentDAO{
  final private NamedParameterJdbcTemplate template;

  /**
   * 댓글 등록
   * @param postComment
   * @return 댓글 번호
   */
  @Override
  public Long save(PostComment postComment) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO post_comment (comment_id,post_id,parent_id,content,member_id) ");
    sql.append("VALUES (post_comment_comment_id_seq.nextval,:postId,:parentId,:content, :memberId) ");

    //BeanPropertySqlParameterSource : 자바객체 필드명과 SQL파라미터명이 같을때 자동 매칭함.
    SqlParameterSource param = new BeanPropertySqlParameterSource(postComment);

    // template.update()가 수행된 레코드의 특정 컬럼값을 읽어오는 용도(댓글 번호)
    KeyHolder keyHolder = new GeneratedKeyHolder();
    long rows = template.update(sql.toString(),param, keyHolder, new String[]{"comment_id"} );

    Number pidNumber = (Number)keyHolder.getKeys().get("comment_id");
    long pid = pidNumber.longValue();
    return pid;
  }

  /**
   * 대댓글 등록
   * @param postComment
   * @return 댓글 번호
   */
  @Override
  public Long commentSave(PostComment postComment) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO post_comment (comment_id,post_id,parent_id,depth,content,member_id) ");
    sql.append("VALUES (post_comment_comment_id_seq.nextval,:postId,:parentId,:depth,:content, :memberId) ");

    //BeanPropertySqlParameterSource : 자바객체 필드명과 SQL파라미터명이 같을때 자동 매칭함.
    SqlParameterSource param = new BeanPropertySqlParameterSource(postComment);

    // template.update()가 수행된 레코드의 특정 컬럼값을 읽어오는 용도(댓글 번호)
    KeyHolder keyHolder = new GeneratedKeyHolder();
    long rows = template.update(sql.toString(),param, keyHolder, new String[]{"comment_id"} );

    Number pidNumber = (Number)keyHolder.getKeys().get("comment_id");
    long pid = pidNumber.longValue();
    return pid;
  }

  /**
   * 댓글 목록
   * @return 댓글 목록
   */
  @Override
  public List<PostComment> findAll(Long id) {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT comment_id,post_id,parent_id,DEPTH,content,m.nickname as nickname,member_id,code_id,cdate,udate ");
    sql.append("FROM POST_COMMENT p ");
    sql.append("JOIN member m ");
    sql.append("on p.member_id=m.member_id ");
    sql.append("WHERE post_id = :id ");
    sql.append("ORDER BY comment_id DESC ");

    Map<String, Long> param = Map.of("id", id);

    //db요청
    List<PostComment> list = template.query(sql.toString(),param, BeanPropertyRowMapper.newInstance(PostComment.class));

    return list;
  }

  @Override
  public List<PostComment> findAll(Long id, int pageNo, int numOfRows) {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("select comment_id, post_id, parent_id, depth, content,m.nickname as nickname, m.member_id AS member_id, p.code_id AS code_id, p.cdate AS cdate,p.udate AS udate  ");
    sql.append("FROM POST_COMMENT p ");
    sql.append("JOIN member m ");
    sql.append("on p.member_id=m.member_id ");
    sql.append("WHERE post_id = :id ");
    sql.append("ORDER BY comment_id DESC ");  // 정렬 기준 수정
    sql.append("OFFSET (:pageNo - 1) * :numOfRows ROWS ");
    sql.append("FETCH NEXT :numOfRows ROWS ONLY ");

    Map<String, Object> params = Map.of(
        "id", id,
        "pageNo", pageNo,
        "numOfRows", numOfRows
    );

    List<PostComment> list = template.query(sql.toString(), params, BeanPropertyRowMapper.newInstance(PostComment.class));

    return list;
  }

  @Override
  public int getTotalCount(Long id) {
    String sql = "select count(*) from post_comment where post_id= :id";

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("id", id); ;
    int i = template.queryForObject(sql, param, Integer.class);

    return i;
  }

  /**
   * 댓글조회
   * @param id 댓글번호
   * @return 댓글정보
   */
  @Override
  public Optional<PostComment> findById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT comment_id,post_id,parent_id,DEPTH,content,member_id,code_id,cdate ");
    sql.append("FROM POST_COMMENT ");
    sql.append("WHERE comment_id= :id ");

    SqlParameterSource param = new MapSqlParameterSource().addValue("id",id);

    PostComment postComment = null;
    try {
      postComment = template.queryForObject(sql.toString(), param, BeanPropertyRowMapper.newInstance(PostComment.class));
    } catch (EmptyResultDataAccessException e) { //template.queryForObject() : 레코드를 못찾으면 예외 발생
      return Optional.empty();
    }

    return Optional.of(postComment);
  }

  /**
   * 댓글 삭제(단건)
   * @param id 댓글 번호
   * @return 삭제 건수
   */

//  @Override
//  public int deleteById(Long id) {
//    StringBuffer sql = new StringBuffer();
//    sql.append("DELETE FROM post_comment ");
//    sql.append("WHERE comment_id = :id ");
//
//    Map<String, Long> param = Map.of("id",id);
//    int rows = template.update(sql.toString(), param); //삭제된 행의 수 반환
//    return rows;
//  }

  @Override
  public int deleteById(Long id) {
    StringBuffer sql = new StringBuffer();
//    sql.append("UPDATE post_comment ");
//    sql.append("   SET content = '삭제된 댓글입니다.', udate = SYSTIMESTAMP ");
//    sql.append(" WHERE comment_id = :commentId ");

    sql.append("DELETE  ");
    sql.append("FROM post_comment   ");
    sql.append(" WHERE comment_id = :commentId ");

    //수동매핑
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("commentId", id);

    int rows = template.update(sql.toString(), param); // 수정된 행의 수 반환

    return rows;
  }


  /**
   * 댓글 수정
   * @param commentId mmentId 댓글 번호
   * @param postComment  댓글 정보
   * @return 댓글 수정 건수
   */
  @Override
  public int updateById(Long commentId, PostComment postComment) {
    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE post_comment ");
    sql.append("   SET content = :content, udate = SYSTIMESTAMP ");
    sql.append(" WHERE comment_id = :commentId ");

    //수동매핑
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("content", postComment.getContent())
        .addValue("commentId", commentId);

    int rows = template.update(sql.toString(), param); // 수정된 행의 수 반환

    return rows;
  }
}
