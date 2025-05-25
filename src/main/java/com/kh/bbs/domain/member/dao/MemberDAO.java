package com.kh.bbs.domain.member.dao;

import com.kh.bbs.domain.entity.Member;

import java.util.Optional;

public interface MemberDAO {
  Optional<Member> saveId(Long memberId);
}
