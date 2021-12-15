package com.esen.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esen.shop.dao.MemberDao;
import com.esen.shop.dto.AddressVO;
import com.esen.shop.dto.MemberVO;

@Service
public class MemberService {

	@Autowired
	MemberDao mdao;

	public MemberVO getMember(String id) {
		return  mdao.getMember(id);
	}

	public int confirmID(String id) {
		return mdao.confirmID(id);
	}

	public List<AddressVO> selectAddressByDong(String dong) {
		return mdao.selectAddressByDong(dong);
	}

	public void insertMember(MemberVO mvo) {
		mdao.insertMember(mvo);
		
	}

	public void memberUpdate(MemberVO mvo) {
		mdao.memberUpdate(mvo);
		
	}
}
