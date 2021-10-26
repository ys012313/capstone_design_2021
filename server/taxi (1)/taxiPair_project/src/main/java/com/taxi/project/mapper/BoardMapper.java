package com.taxi.project.mapper;

import java.util.List;
import java.util.Map;

public interface BoardMapper {
	public List<Map<String,Object>> getBoardList(Map<String, Object> map) throws Exception;						// 택시메이트 목록 조회
	public List<Map<String,Object>> getBoardSearchList(Map<String, Object> map) throws Exception;				// 택시메이트 목록 검색 조회
	public List<Map<String,Object>> getTodayBoardingGroup(Map<String, Object> map) throws Exception;			// 동승해야 할 그룹 조회
	public List<Map<String,Object>> getRecentlyBoardingGroup(Map<String, Object> map) throws Exception;			// 최근 택시메이트 조회 
	public Map<String,Object> getFixBoardingGroup(Map<String, Object> map) throws Exception;					// 택시탑승 완료내역 조회 
	public String leaderChk(Map<String, Object> map) throws Exception;											// 그룹장 체크
	
	public int createGroup(Map<String, Object> map) throws Exception;											// 동승 그룹 생성
	public int insertBoardingUser(Map<String, Object> map) throws Exception;									// 동승자 정보 등록 
	public int fixBoardingGroup(Map<String, Object> map) throws Exception;										// 택시탑승 완료처리 
	public int finishDrive(Map<String, Object> map) throws Exception;											// 택시운행종료하기 
	public int escapeDrive(Map<String, Object> map) throws Exception;											// 동승 취소 
	public int deleteGroup(Map<String, Object> map) throws Exception;											// 동승 취소한 사람이 그룹장일 경우, 해당 게시글도 삭제
}
