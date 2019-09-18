package com.coordination.service;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.coordination.dao.MemberDAO;
import com.coordination.dto.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {

	@Inject
	private MemberDAO dao;
	
	private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
	
	//회원목록 전체 조회
//	@Override
//	public List<MemberVO> memberList() throws Exception {
//		
//		return dao.memberList();
//	}
//
//	//특정 회원 정보 조회
//	@Override
//	public List<MemberVO> selectMember(MemberVO vo) throws Exception {
//
//		return dao.selectMember(vo);
//	}

	//회원가입
	@Override
	public void insertMember(MemberVO vo) {

		dao.insertMember(vo);
	}

	//회원정보 수정
	@Override
	public void updateMember(MemberVO vo) {

		dao.updateMember(vo);
	}

	//회원정보 삭제
	@Override
	public void deleteMember(MemberVO vo) {

		dao.deleteMember(vo);
	}
	
	//로그인 처리
	@Override
	public MemberVO loginCheck(MemberVO vo, HttpSession session, HttpServletRequest request) throws Exception {
		
		vo = dao.loginCheck(vo);
		
		//로그인 성공
		if(vo != null)
		{
			logger.info(vo.getName() + "회원 Login Success");
			
			//Test 
			//잘 수행될 경우 해당 구문 주석 or 삭제요망
			System.out.println("ID : " + vo.getId());
			System.out.println("PASSWORD : " + vo.getPassword());
			System.out.println("NAME : " + vo.getName());
			System.out.println("PHONE : " + vo.getPhone());
			
			//세션 설정
			//id의 경우 회원정보 수정, 삭제 시 필요
			//이름의 경우 사이트에 OOO님 환영합니다 문구 시 필요 [추후 필요없을 경우 삭제]
			session.setAttribute("userID", vo.getId());
			session.setAttribute("userName", vo.getName());
		}
		
		return vo;
	}

}
