package com.esen.shop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esen.shop.dto.MemberVO;
import com.esen.shop.dto.QnaVO;
import com.esen.shop.service.QnaService;

@Controller
public class QnaController {

	@Autowired
	QnaService qs;
	
	@RequestMapping(value="/qnaList")
	public ModelAndView qna_list(Model model, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		HttpSession session =request.getSession();
		MemberVO mvo=(MemberVO)session.getAttribute("loginUser");
		if(mvo==null)mav.setViewName("member/login");
		else {
			mav.addObject("qnaList", qs.listQna(mvo.getId()));
			mav.setViewName("qna/qnaList");
		}
		return mav;
	}
	
	@RequestMapping(value="/qnaView")
	public ModelAndView qna_view(Model model, HttpServletRequest request,
			@RequestParam("qseq")int qseq) {
		ModelAndView mav = new ModelAndView();
		
		HttpSession session =request.getSession();
		MemberVO mvo=(MemberVO)session.getAttribute("loginUser");
		if(mvo==null)mav.setViewName("member/login");
		else {
			mav.addObject("qnaVO", qs.getQna(qseq));
			mav.setViewName("qna/qnaView");
		}
		return mav;
	
	}
	
	
	@RequestMapping(value="/qnaWriteForm")
	public String qna_write_form(HttpServletRequest request) {		
		HttpSession session =request.getSession();
		MemberVO mvo=(MemberVO)session.getAttribute("loginUser");
		if(mvo==null) return "member/login";
		return "qna/qnaWrite";
  }
	
	@RequestMapping(value="/qnaWrite")
	public ModelAndView qna_write(HttpServletRequest request,
			@RequestParam("subject")String subject,@RequestParam("content")String content) {
		ModelAndView mav = new ModelAndView();
		
		HttpSession session =request.getSession();
		MemberVO mvo=(MemberVO)session.getAttribute("loginUser");
		
		if(mvo==null)mav.setViewName("member/login");
		else {
			QnaVO qvo=new QnaVO();
			qvo.setSubject(request.getParameter("subject"));
			qvo.setContent(request.getParameter("content"));
			qs.insertQna(qvo,mvo.getId());
		}
		mav.setViewName("redirect:qnaList");
		return mav;
	}	
}
