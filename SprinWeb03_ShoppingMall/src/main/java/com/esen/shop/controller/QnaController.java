package com.esen.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.esen.shop.service.QnaService;

@Controller
public class QnaController {

	@Autowired
	QnaService qs;
}
