package com.esen.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esen.shop.dao.ProductDao;
import com.esen.shop.dto.ProductVO;

@Service
public class ProductService {

	@Autowired
	ProductDao pdao;

	public List<ProductVO> getNewList() {
		return pdao.getNewList();
	}

	public List<ProductVO> getBestList() {
		return pdao.getBestList();
	}
}
