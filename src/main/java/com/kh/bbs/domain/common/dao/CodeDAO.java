package com.kh.bbs.domain.common.dao;

import com.kh.bbs.domain.common.CodeId;
import com.kh.bbs.domain.dto.CodeDTO;

import java.util.List;

public interface CodeDAO {
  List<CodeDTO> loadCodes(CodeId pocdId);;
}
