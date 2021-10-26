package com.taxi.project.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taxi.project.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 가입고객 목록 조회
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public List<Map<String, Object>> getUserList() throws Exception {
		
		logger.debug(" @@ getUserList call in UserService ==> ");
		
		return userMapper.getUserList();
		
	}
	
	/**
	 * 가입고객 목록 조회
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public Map<String, Object> getUserInfo(Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ getUserInfo call in UserService ==> ");
		
		return userMapper.getUserInfo(paramMap);
		
	}
	
	/**
	 * 동승내역 통계 조회
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public Map<String, Object> getTotalPay(Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ getTotalPay call in UserService ==> ");
		
		return userMapper.getTotalPay(paramMap);
		
	}
	
	/**
	 * 아이디 검사
	 * 
	 * 1) 회원가입 시 아이디 중복검사 용도로 사용
	 * 2) 로그인 시 아이디 확인용도로 사용
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int getUserIdCheck(Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ getUserIdCheck call in UserService ==> ");
		
		return userMapper.getUserIdCheck(paramMap);
		
	}
	
	
	/**
	 * 비밀번호 검사
	 * 
	 * 1) 로그인 시 비밀번호 확인용도로 사용
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int getUserPasswordCheck(Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ getUserPasswordCheck call in UserService ==> ");
		
		return userMapper.getUserPasswordCheck(paramMap);
		
	}


	/**
	 * 신고된 계정 검사
	 * 
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int getBlackListUser(Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ getBlackListUser call in UserService ==> ");
		
		return userMapper.getBlackListUser(paramMap);
		
	}
	
	
	/**
	 * 토큰 업데이트
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int updateToken (Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ updateToken call in UserService ==> ");
		
		return userMapper.updateToken(paramMap);
		
	}
	
	/**
	 * 회원가입
	 * 
	 * 1) 회원가입 처리 
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int insertUserInfo(Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ insertUserInfo call in UserService ==> ");
		
		return userMapper.insertUserInfo(paramMap);
		
	}
	
	/**
	 * 회원탈퇴 
	 * 
	 * 1) 회원탈퇴 처리 
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int deleteUserInfo(Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ insertUsedeleteUserInforInfo call in UserService ==> ");
		
		return userMapper.deleteUserInfo(paramMap);
		
	}
	
	/**
	 * 신고하기 
	 * 
	 * 1) 신고 처리 
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int insertBlacklistUser(Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ insertBlacklistUser call in UserService ==> ");
		
		return userMapper.insertBlacklistUser(paramMap);
		
	}
	
	/**
	 * 신고취소하기 
	 * 
	 * 1) 신고취소 처리 
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int deleteBlacklistUser(Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ deleteBlacklistUser call in UserService ==> ");
		
		return userMapper.deleteBlacklistUser(paramMap);
		
	}
}
