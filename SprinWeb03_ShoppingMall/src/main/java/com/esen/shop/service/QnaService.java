package com.esen.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esen.shop.dao.QnaDao;

@Service
public class QnaService {

	@Autowired
	QnaDao qdao;
}
