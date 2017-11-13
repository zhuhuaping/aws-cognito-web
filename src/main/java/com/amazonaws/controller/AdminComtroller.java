package com.amazonaws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminComtroller {

	@RequestMapping("/admin")
	public String admin() {
		return "admin";
	}

}