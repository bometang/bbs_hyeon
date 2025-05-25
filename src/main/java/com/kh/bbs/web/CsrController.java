package com.kh.bbs.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/csr")
public class CsrController {

  @GetMapping("/boards")
  public String products() {
    return "csr/postBoards/postBoards";
  }
}
