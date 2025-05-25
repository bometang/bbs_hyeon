package com.kh.bbs.domain.member.dao;

import com.kh.bbs.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberDAOImpl implements MemberDAO{
  final private NamedParameterJdbcTemplate template;

  @Override
  public Optional<Member> saveId(Long id){
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT member_id, nickname, email, passwd,code_id,cdate,udate ");
    sql.append("FROM MEMBER ");
    sql.append("WHERE member_id = :id ");

    SqlParameterSource param = new MapSqlParameterSource().addValue("id",id);

    Member m1 = null;
    try {
      m1 = template.queryForObject(sql.toString(), param, BeanPropertyRowMapper.newInstance(Member.class));
    } catch (EmptyResultDataAccessException e) { //template.queryForObject() : 레코드를 못찾으면 예외 발생
      return Optional.empty();
    }

    return  Optional.of(m1);
  }
}
