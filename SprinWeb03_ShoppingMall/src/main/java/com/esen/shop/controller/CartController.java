package com.esen.shop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esen.shop.dto.CartVO;
import com.esen.shop.dto.MemberVO;
import com.esen.shop.service.CartService;

@Controller
public class CartController {

	@Autowired
	CartService cs;
	
	@RequestMapping(value="/cartInsert")
	public String carInsert(@RequestParam("pseq") int pseq,
			@RequestParam("quantity") int quantity,HttpServletRequest request) {  //담아갈게없으니 String으로.. 누구의 장바구니인지 알려면 세션에서 얻어와야함
		
		HttpSession session=request.getSession();
		MemberVO mvo =(MemberVO)session.getAttribute("loginUser");
		
		if(mvo == null) {
			return "member/login";
		}else {
			CartVO cvo =new CartVO();
			cvo.setId(mvo.getId());
			cvo.setPseq(pseq);
			cvo.setQuantity(quantity);
			cs.insertCart(cvo);
			
		}
		
		return "redirect:/cartList";
	}
	
	@RequestMapping(value="cartList")
	public ModelAndView cartList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		HttpSession session=request.getSession();
		MemberVO mvo =(MemberVO)session.getAttribute("loginUser");
		
		if(mvo == null) {
			mav.setViewName("member/login");
		}else {
			List<CartVO>list=cs.listCart(mvo.getId());
			int totalPrice=0;
			for(CartVO cvo:list) {
				totalPrice += (cvo.getPrice2() * cvo.getQuantity());
		}
				mav.addObject("totalPrice", totalPrice);
				mav.addObject("cartList", list);
			
			mav.setViewName("mypage/cartList");
			
		}
		return mav;
	}
	
	@RequestMapping("cartDelete")
	public String cartDelete(@RequestParam("cseq")String [] cseqArr) {
		//String[] cseqArr=request.getParameterValues("cseq");
		for(String cseq:cseqArr)
			cs.deleteCart(cseq);
		return "redirect:/cartList";
		
	}
	
	}
