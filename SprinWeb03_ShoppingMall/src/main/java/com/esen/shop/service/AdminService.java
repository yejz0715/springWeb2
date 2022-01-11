package com.esen.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esen.shop.dao.AdminDao;
import com.esen.shop.dto.OrderVO;
import com.esen.shop.dto.Paging;
import com.esen.shop.dto.ProductVO;
import com.esen.shop.dto.QnaVO;

@Service
public class AdminService {

	@Autowired
	AdminDao adao;

	public int workerCheck(String workId, String workPwd) {
		return adao.workerCheck(workId, workPwd);
	}

	public List<ProductVO> listProduct(Paging paging, String key) {
		return adao.listProduct( paging, key );
	}

	public int getAllCount(String tableName, String fieldName, String key) {
		return adao.getAllCount( tableName, fieldName, key );
	}

	public void insertProduct(ProductVO pvo) {
		adao.insertProduct(pvo);		
	}

	public void updateProduct(ProductVO pvo) {
		adao.updateProduct(pvo);		
	}

	public List<OrderVO> listOrderAll(Paging paging, String key) {
		return adao.listOrderAll(paging, key);
	}

	public void updateOrderResult(int odseq) {
		adao.updateOrderResult(odseq);
		
	}

	public List<QnaVO> listQnaAll(Paging paging, String key) {
		return adao.listQnaAll(paging, key);
	}
}







