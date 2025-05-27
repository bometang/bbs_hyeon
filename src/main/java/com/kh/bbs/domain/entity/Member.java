package com.kh.bbs.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
  private Long memberId;
  private String nickname;
  private String email;
  private String passwd;
  private String tel;             //  TEL	VARCHAR2(13 BYTE)
  private String gender;          //  GENDER	VARCHAR2(6 BYTE)
  private String hobby;           //  HOBBY	VARCHAR2(300 BYTE)
  private String region;          //  REGION	VARCHAR2(11 BYTE)
  private String gubun;           //  GUBUN	VARCHAR2(11 BYTE)
  private byte[] pic;             //  PIC	BLOB             //  PIC	BLOB
  private LocalDateTime cdate;
  private LocalDateTime udate;
}
