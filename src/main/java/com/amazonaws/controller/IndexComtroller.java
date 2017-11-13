package com.amazonaws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexComtroller {

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

}