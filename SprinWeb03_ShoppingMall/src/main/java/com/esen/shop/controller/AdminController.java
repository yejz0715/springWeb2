package com.esen.shop.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esen.shop.dto.OrderVO;
import com.esen.shop.dto.Paging;
import com.esen.shop.dto.ProductVO;
import com.esen.shop.dto.QnaVO;
import com.esen.shop.service.AdminService;
import com.esen.shop.service.ProductService;
import com.esen.shop.service.QnaService;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@Controller
public class AdminController {

	@Autowired
	ProductService ps;   //getProduct() 사용
	
	@Autowired
	QnaService qs;  //getQna() 사용
	
	@Autowired
	ServletContext context;   //파일 업로드를 위해 준비
	
	@Autowired
	AdminService as;
	
	@RequestMapping("/")
	public String admin() {
		return "admin/adminLoginForm";
	}
	
	
	
	@RequestMapping("adminLogin")
	public ModelAndView adminLogin( HttpServletRequest request,
			@RequestParam("workId") String workId,
			@RequestParam("workPwd") String workPwd) {
		ModelAndView mav = new ModelAndView();
		
		int result = as.workerCheck(workId, workPwd);
		// result 값이 1이면 정상 로그인, 0 이면 비밀번호 오류, -1 이면 아이디 없음
		
		if(result == 1) {
			HttpSession session = request.getSession();
    		session.setAttribute("workId", workId);
    		mav.setViewName("redirect:/productList");
		}else if( result == 0) {
			mav.addObject("message" , "비밀번호를 확인하세요");
			mav.setViewName("admin/adminLoginForm");
		}else if(result == -1) {
			mav.addObject("message" , "아이디를 확인하세요");
			mav.setViewName("admin/adminLoginForm");
		}else {
			mav.addObject("message" , "이유를 알수없지만 로그인 안돼요");
			mav.setViewName("admin/adminLoginForm");
		}
		return mav;
	}
	
	
	
	
	@RequestMapping("/productList")
	public ModelAndView product_list(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("workId");
		if(id==null)
			mav.setViewName("redirect:/admin");
		else {
			int page = 1;
			if(  request.getParameter("first") != null && request.getParameter("first").equals("y") ) {
				page=1;
				session.removeAttribute("page");
			}else if( request.getParameter("page") != null) {
				page = Integer.parseInt(request.getParameter("page"));
				session.setAttribute("page", page);
			}else if( session.getAttribute("page") != null ) {
				page = (Integer)session.getAttribute("page");
			}else {
				page = 1;
				session.removeAttribute("page");
			}
			
			String key = "";
			if(  request.getParameter("first") != null && request.getParameter("first").equals("y") ) {
				key="";
				session.removeAttribute("key");
			}else if( request.getParameter("key") != null ) {
				key = request.getParameter("key");
				session.setAttribute("key", key);
			} else if( session.getAttribute("key")!= null ) {
				key = (String)session.getAttribute("key");
			} else {
				session.removeAttribute("key");
				key = "";
			}
			
			
			Paging paging = new Paging();
			paging.setPage(page);//paging불러줘야 페이지가 나옴
			int count = as.getAllCount("product" , "name", key);
			paging.setTotalCount(count);
			
			List<ProductVO> productList = as.listProduct( paging, key );
			
			request.setAttribute("paging" , paging );
			mav.addObject("productList", productList);
			request.setAttribute("key", key);
			mav.setViewName("admin/product/productList");
		}
		return mav;
	}
	
	
	
	@RequestMapping("adminProductDetail")
	public ModelAndView product_detail(HttpServletRequest request, 
			@RequestParam("pseq") int pseq) {
		ModelAndView mav = new ModelAndView();
		
		ProductVO pvo = ps.getProduct(pseq);
		
		String kindList[] = { "0", "Heels", "Boots", "Sandals", "Slipers", "Shcakers", "Sale" };
		int index = Integer.parseInt(pvo.getKind());
		
		mav.addObject("productVO", pvo);
		mav.addObject("kind", kindList[index]);
		mav.setViewName("admin/product/productDetail");
		
		return mav;
	}
	
	@RequestMapping("productWriteForm")
	public ModelAndView product_write_form( HttpServletRequest request) {
		String kindList[] = { "Heels", "Boots", "Sandals", "Shcakers", "Slipers",  "Sale" };
		ModelAndView mav = new ModelAndView();
		mav.addObject("kindList", kindList);
		mav.setViewName("admin/product/productWriteForm");
		return mav;
	}
	
	
	
	@RequestMapping(value="/productWrite", method = RequestMethod.POST)
	public String product_write(HttpServletRequest request) {
		
		String savePath = context.getRealPath("resources/product_images");
		ProductVO pvo = new ProductVO();
		// 전달 파라미터를 pvo 에 넣고   서비스의 insertProduct에 전달
		try {
			MultipartRequest multi = new MultipartRequest(
					request, savePath, 5*1024*1024, "UTF-8", new DefaultFileRenamePolicy() );
			pvo.setKind( multi.getParameter("kind"));
		    pvo.setName( multi.getParameter("name"));
		    pvo.setPrice1( Integer.parseInt(multi.getParameter("price1")));
		    pvo.setPrice2( Integer.parseInt(multi.getParameter("price2")));
		    pvo.setPrice3( Integer.parseInt(multi.getParameter("price2"))
		    	        - Integer.parseInt(multi.getParameter("price1")));
		    pvo.setContent( multi.getParameter("content"));
		    pvo.setImage( multi.getFilesystemName("image"));
		}catch (IOException e) {	e.printStackTrace();	}
		 as.insertProduct(pvo);
		return "redirect:/productList";
	}
	
	
	
	@RequestMapping("productUpdateForm")
	public ModelAndView product_update_form( HttpServletRequest request,
			@RequestParam("pseq") int pseq) {
		ModelAndView mav = new ModelAndView();
		ProductVO pvo =  ps.getProduct(pseq);
		mav.addObject("productVO", pvo);
		String kindList[] = { "Heels", "Boots", "Sandals", "Slipers", "Shcakers", "Sale" };    
		mav.addObject("kindList", kindList);
		mav.setViewName("admin/product/productUpdate");
		return mav;
	}
	
	
	@RequestMapping("productUpdate")
	public String product_update( HttpServletRequest request) {
		ProductVO pvo = new ProductVO();
		int pseq = 0;
		String savePath = context.getRealPath("resources/product_images");
		try {
			MultipartRequest  multi = new MultipartRequest(request, savePath, 5*1024*1024,
					"UTF-8", new DefaultFileRenamePolicy()  );
			pvo.setPseq( Integer.parseInt(multi.getParameter("pseq")));
			pseq =Integer.parseInt( multi.getParameter("pseq"));  // 상품 업데이트 후 되돌아갈 상품 번호 저장
		    pvo.setKind( multi.getParameter("kind"));
		    pvo.setName( multi.getParameter("name"));
		    pvo.setPrice1( Integer.parseInt( multi.getParameter("price1")));
		    pvo.setPrice2( Integer.parseInt( multi.getParameter("price2")));
		    pvo.setPrice3( Integer.parseInt( multi.getParameter("price2"))
		        - Integer.parseInt( multi.getParameter("price1")));
		    pvo.setContent( multi.getParameter("content"));
		    pvo.setUseyn( multi.getParameter("useyn"));
		    pvo.setBestyn( multi.getParameter("bestyn"));
		    if( multi.getFilesystemName("image")==null)  // 수정하려는 이미지가  없을경우 이전 이미지로 수저앧체
		    	pvo.setImage( multi.getParameter("oldfilename"));
		    else   pvo.setImage( multi.getFilesystemName("image"));
		} catch (IOException e) { e.printStackTrace();	}
		as.updateProduct(pvo);
		return "redirect:/adminProductDetail?pseq=" + pseq;
	}
	
	@RequestMapping("/adminOrderList")
	public ModelAndView adminOrderList( HttpServletRequest request ) {
		ModelAndView mav = new ModelAndView(  );
		HttpSession session = request.getSession();
		int page = 1;
		
		if( request.getParameter("first") != null && request.getParameter("first").equals("y")) {
			page=1;
			session.removeAttribute("page");
		}else if( request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
			session.setAttribute("page", page);
		}else if( session.getAttribute("page") != null ) {
			page = (Integer)session.getAttribute("page");
		}else {
			page = 1;
			session.removeAttribute("page");
		}		
		String key = "";
		if( request.getParameter("first") != null && request.getParameter("first").equals("y")) {
			key="";
			session.removeAttribute("key");
		}else if( request.getParameter("key") != null ) {
			key = request.getParameter("key");
			session.setAttribute("key", key);
		} else if( session.getAttribute("key")!= null ) {
			key = (String)session.getAttribute("key");
		} else {
			session.removeAttribute("key");
			key = "";
		}
		
		Paging paging = new Paging();
		paging.setPage(page);
		int count = as.getAllCount("order_view" , "mname", key);
		paging.setTotalCount(count);
		
		
		// order_view 에서  전체 주문을 조회해서
		List<OrderVO> list = as.listOrderAll( paging, key );
 		mav.addObject("orderList", list);
		// orderList.jsp로 이동
 		mav.addObject("paging", paging);
 		mav.addObject("key", key);
		mav.setViewName("admin/order/orderList");
		return mav;
	}
	
	
	@RequestMapping("/orderUpdateResult")
	public String orderUpdateResult( @RequestParam("result") int [] resultArr ) {
		
		for( int odseq : resultArr) {
			as.updateOrderResult(odseq);
		}
		
		return "redirect:/adminOrderList";
	}
	
	@RequestMapping("/adminQnaList")
	public ModelAndView adminQnaList(HttpServletRequest request ) {
		ModelAndView mav = new ModelAndView(  );
		HttpSession session = request.getSession();
		int page = 1;
		if( request.getParameter("first") != null && request.getParameter("first").equals("y")) {
			page=1;
			session.removeAttribute("page");
		}else if( request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
			session.setAttribute("page", page);
		}else if( session.getAttribute("page") != null ) {
			page = (Integer)session.getAttribute("page");
		}else {
			page = 1;
			session.removeAttribute("page");
		}		
		String key = "";
		if( request.getParameter("first") != null && request.getParameter("first").equals("y")) {
			key="";
			session.removeAttribute("key");
		}else if( request.getParameter("key") != null ) {
			key = request.getParameter("key");
			session.setAttribute("key", key);
		} else if( session.getAttribute("key")!= null ) {
			key = (String)session.getAttribute("key");
		} else {
			session.removeAttribute("key");
			key = "";
		}
		Paging paging = new Paging();
		paging.setPage(page);
		int count = as.getAllCount("qna" , "subject", key);
		paging.setTotalCount(count);
		List<QnaVO> list = as.listQnaAll( paging, key );
 		mav.addObject("qnaList", list);
 		mav.addObject("paging", paging);
 		mav.addObject("key", key);
		mav.setViewName("admin/qna/qnaList");
		return mav;
		
	}
	
	
	@RequestMapping("/adminQnaView")
	public ModelAndView qnaView(HttpServletRequest request,
			@RequestParam("qseq")int qseq) {
		ModelAndView mav=new ModelAndView();
		mav.addObject("qnaVO", qs.getQna(qseq));
		mav.setViewName("admin/qna/qnaView");
		return mav;
	}
	
	
	@RequestMapping("/adminQnaRepsave")
	public String adminQnaRepsave(@RequestParam("qseq") int qseq,
			@RequestParam("reply")String reply) {
		QnaVO qvo=new QnaVO();
		qvo.setQseq(qseq);
		qvo.setReply(reply);
		
		qvo.updateQna(qvo);
		
		return "redirect:/adminQmaView?qseq=" +qseq;
	}
}








