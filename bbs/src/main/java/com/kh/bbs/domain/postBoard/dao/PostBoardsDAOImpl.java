package com.kh.bbs.domain.postBoard.dao;

import com.kh.bbs.domain.entity.PostBoards;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
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
public class PostBoardsDAOImpl implements PostBoardsDAO {
  final private NamedParameterJdbcTemplate template;

  /*목록*/
  //수동 매핑
  RowMapper<PostBoards> postsRowMapper(){

    return (rs, rowNum)->{
      PostBoards postBoards = new PostBoards();
      postBoards.setPostId(rs.getLong("post_id"));
      postBoards.setTitle(rs.getString("title"));
      postBoards.setUserName(rs.getString("user_name"));
      postBoards.setCdate(rs.getString("cdate"));
      return postBoards;
    };
  }

  /**
   * 게시글 등록
   * @param postBoards
   * @return 게시글 번호
   */
  @Override
  public Long save(PostBoards postBoards) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO postBoard (post_id, title, content, user_name) ");
    sql.append("VALUES (postBoard_post_id_seq.nextval, :title, :content, :userName) ");

    //BeanPropertySqlParameterSource : 자바객체 필드명과 SQL파라미터명이 같을때 자동 매칭함.
    SqlParameterSource param = new BeanPropertySqlParameterSource(postBoards);

    // template.update()가 수행된 레코드의 특정 컬럼값을 읽어오는 용도(게시글 번호)
    KeyHolder keyHolder = new GeneratedKeyHolder();
    long rows = template.update(sql.toString(),param, keyHolder, new String[]{"post_id"} );

    Number pidNumber = (Number)keyHolder.getKeys().get("post_id");
    long pid = pidNumber.longValue();
    return pid;
  }

  /**
   * 게시판 목록
   * @return 게시판 목록
   */
  @Override
  public List<PostBoards> findAll() {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("  SELECT post_id, title, user_name,TO_CHAR(cdate, 'YYYY-MM-DD HH24:MI') AS cdate ");
    sql.append("    FROM postBoard ");
    sql.append("ORDER BY post_id desc ");

    //db요청
    List<PostBoards> list = template.query(sql.toString(), postsRowMapper());

    return list;
  }

  /**
   * 게시판조회
   * @param id 게시판번호
   * @return 게시판정보
   */
  @Override
  public Optional<PostBoards> findById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT post_id, title,user_name, TO_CHAR(cdate, 'YYYY-MM-DD HH24:MI') AS cdate, TO_CHAR(udate, 'YYYY-MM-DD HH24:MI') AS udate, content ");
    sql.append("  FROM postBoard ");
    sql.append(" WHERE post_id = :id ");

    SqlParameterSource param = new MapSqlParameterSource().addValue("id",id);

    PostBoards postBoards = null;
    try {
      postBoards = template.queryForObject(sql.toString(), param, BeanPropertyRowMapper.newInstance(PostBoards.class));
    } catch (EmptyResultDataAccessException e) { //template.queryForObject() : 레코드를 못찾으면 예외 발생
      return Optional.empty();
    }

    return Optional.of(postBoards);
  }

  /**
   * 게시글 삭제(단건)
   * @param id 게시글 번호
   * @return 삭제 건수
   */
  @Override
  public int deleteById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM postBoard ");
    sql.append("WHERE post_id = :id ");

    Map<String, Long> param = Map.of("id",id);
    int rows = template.update(sql.toString(), param); //삭제된 행의 수 반환
    return rows;
  }

  /**
   * 게시글 삭제(여러건)
   * @param ids 게시글 번호s
   * @return 삭제 건수
   */
  @Override
  public int deleteByIds(List<Long> ids) {
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM postBoard ");
    sql.append("WHERE post_id IN ( :ids ) ");

    Map<String, List<Long>> param = Map.of("ids",ids);
    int rows = template.update(sql.toString(), param); //삭제한 행의 수 반환
    return rows;
  }

  /**
   * 게시글 수정
   * @param postId 게시글 번호
   * @param postBoards  게시글 정보
   * @return 게시글 수정 건수
   */
  @Override
  public int updateById(Long postId, PostBoards postBoards) {    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE postBoard ");
    sql.append("   SET title = :title, content = :content, udate = SYSTIMESTAMP ");
    sql.append(" WHERE post_id = :postId ");

    //수동매핑
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("title", postBoards.getTitle())
        .addValue("content", postBoards.getContent())
        .addValue("postId", postId);

    int rows = template.update(sql.toString(), param); // 수정된 행의 수 반환

    return rows;
  }
}
