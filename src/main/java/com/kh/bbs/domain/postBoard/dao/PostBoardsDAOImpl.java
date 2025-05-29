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

  //수동매핑
  private RowMapper<PostBoards> doRowMapper(){

    return (rs, rowNum)->{
      PostBoards postBoards = new PostBoards();
      postBoards.setPostId(rs.getLong("post_id"));
      postBoards.setTitle(rs.getString("title"));
      postBoards.setNickname(rs.getString("nickname"));
      postBoards.setCdate(rs.getTimestamp("cdate").toLocalDateTime());      return postBoards;
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
    sql.append("INSERT INTO POSTBOARD (post_id,title,content,member_id,code_id) ");
    sql.append("VALUES (postBoard_post_id_seq.nextval,:title, :content, :memberId,'pb01N') ");

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
    sql.append("SELECT ");
    sql.append("p.post_id as post_id, ");
    sql.append("p.title as title, ");
    sql.append("m.nickname    AS nickname, ");
    sql.append("p.cdate AS cdate ");
    sql.append("FROM postboard p ");
    sql.append("JOIN member   m ");
    sql.append("ON p.member_id = m.member_id ");
    sql.append("ORDER BY post_id DESC ");

    //db요청
    List<PostBoards> list = template.query(sql.toString(), BeanPropertyRowMapper.newInstance(PostBoards.class));

    return list;
  }

  @Override
  public List<PostBoards> findAll(int pageNo, int numOfRows) {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("  SELECT p.post_id as post_id,p.title as title,m.nickname    AS nickname,p.cdate AS cdate ");
    sql.append("    FROM postboard p ");
    sql.append("JOIN member   m ");
    sql.append("ON p.member_id = m.member_id ");
    sql.append("ORDER BY p.post_id DESC ");
    sql.append("  OFFSET (:pageNo -1) * :numOfRows ROWS ");
    sql.append("FETCH NEXT :numOfRows ROWS only ");

    Map<String, Integer> map = Map.of("pageNo", pageNo, "numOfRows", numOfRows);
    List<PostBoards> list = template.query(sql.toString(), map, doRowMapper());

    return list;
  }

  @Override
  public int getTotalCount() {
    String sql = "select count(post_id) from postBoard ";

    SqlParameterSource param = new MapSqlParameterSource();
    int i = template.queryForObject(sql, param, Integer.class);

    return i;
  }

  /**
   * 게시판조회
   * @param id 게시판번호
   * @return 게시판정보
   */
  @Override
  public Optional<PostBoards> findById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT post_id,title, m.nickname AS nickname, p.cdate AS cdate, p.udate AS udate, content, p.code_id AS code_id ");
    sql.append("FROM postBoard p ");
    sql.append("JOIN MEMBER m ON p.member_id = m.member_id ");
    sql.append("WHERE p.POST_ID= :id ");

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
  public int updateById(Long postId, PostBoards postBoards) {
    StringBuffer sql = new StringBuffer();
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
