package com.kh.bbs.domain.common.svc;

import com.kh.bbs.domain.common.CodeId;
import com.kh.bbs.domain.common.dao.CodeDAO;
import com.kh.bbs.domain.dto.CodeDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeSVCImpl implements CodeSVC{

  private final CodeDAO codeDAO;
  private List<CodeDTO> m01;

  @Override
  public List<CodeDTO> getCodes(CodeId pcodeId) {
    return codeDAO.loadCodes(pcodeId);
  }

  @PostConstruct  // 생성자 호출후 실행될 메소드에 선언하면 해당 메소드가 자동 호출
  private List<CodeDTO> getM01Code(){
    log.info("getM01Code() 수행됨!");
    m01 = codeDAO.loadCodes(CodeId.M01);
    return m01;
  }

  public List<CodeDTO> getM01() {
    return m01;
  }
}
