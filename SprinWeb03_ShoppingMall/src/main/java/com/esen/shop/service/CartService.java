package com.esen.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esen.shop.dao.CartDao;

@Service
public class CartService {

	@Autowired
	CartDao cdao;
}
