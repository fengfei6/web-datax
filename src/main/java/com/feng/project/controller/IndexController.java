package com.feng.project.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/admin/{xxx}")
	public String method(@PathVariable String xxx) {
		return "admin/"+xxx;
	}

	@RequestMapping("/{xxx}")
	public String init(@PathVariable String xxx) {
		return xxx;
	}

}
