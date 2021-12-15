package com.esen.shop.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Repository
public class CartDao {
	private JdbcTemplate template;
	@Autowired  
	public CartDao( ComboPooledDataSource dataSource ) {
		this.template = new JdbcTemplate(dataSource);
	}
}
