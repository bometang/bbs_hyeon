package com.kh.bbs.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
//  private Long memberId = 1L;
//  private String nickname = "관리자1";
//  private String email = "a@kh.com";
//  private String passwd = "1234";
//  private String codeID = "M01A";
//  private String cdate = "2025-05-25 02:48:50.503";
//  private String udate;

  private Long memberId;
  private String nickname;
  private String email;
  private String passwd;
  private String codeID;
  private LocalDateTime cdate;
  private LocalDateTime udate;
}
