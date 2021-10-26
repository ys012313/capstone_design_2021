package com.taxi.project.mapper;

import java.util.List;
import java.util.Map;

public interface UserMapper {
	public List<Map<String,Object>> getUserList() throws Exception;								// 가입고객 목록 조회
	public List<Map<String,Object>> getUserTokenList(Map<String, Object> map) throws Exception;	// 가입고객 목록 조회
	public Map<String,Object> getUserInfo(Map<String, Object> map) throws Exception;			// 가입고객 정보 조회
	public Map<String,Object> getTotalPay(Map<String, Object> map) throws Exception;			// 동승내역 통계 조회
	public int getBlackListUser(Map<String, Object> map) throws Exception;						// 신고된 계정 조회
	
	public int getUserIdCheck(Map<String, Object> map) throws Exception;						// 아이디 검사
	public int getUserPasswordCheck(Map<String, Object> map) throws Exception;					// 비밀번호 검사
	public int updateToken(Map<String, Object> map) throws Exception;							// 토큰 업데이트
	
	public int insertUserInfo(Map<String, Object> map) throws Exception;						// 회원가입 
	public int deleteUserInfo(Map<String, Object> map) throws Exception;						// 회원탈퇴 
	public int insertBlacklistUser(Map<String, Object> map) throws Exception;					// 신고하기
	public int deleteBlacklistUser(Map<String, Object> map) throws Exception;					// 신고취소하기
}
