package com.taxi.project.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.taxi.project.service.UserService;

/**
 * UserController
 * 
 * @author 
 * 
 * @description
 * 가입고객 데이터에 대한 등록, 조회, 수정 기능 관리 
 * 1) 가입고객 목록 가져오기
 * 2) 가입고객 이메일 체크 
 * 3) 가입고객 등록 
 * 4) 가입고객 정보 변경 
 * 5) 계정 사용여부 변경 
 * 
 */

@RequestMapping(value = "/user")
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 1) 가입고객 목록 가져오기 
	@RequestMapping(value = "/getUserList", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getUserList() throws Exception {
		
		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
		
		logger.debug(" @@ getUserList call in UserController ==> ");
		
		try {
			userList = userService.getUserList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userList;
	}
	
	// 2) 아이디 체크
	@RequestMapping(value = "/getUserIdCheck", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public HashMap<String, String> getUserIdCheck(@RequestBody Map<String, Object> paramMap) {

		logger.debug(" @@ getUserIdCheck call in UserController ==> ");
		
		int resultCnt = 0;													// 처리결과를 담는 변수 
		HashMap<String, String> resultMap = new HashMap<String, String>();	//처리결과를 반환하기 위한 변
		
		try {
			// 아이디 체크  
			resultCnt = userService.getUserIdCheck(paramMap);
			
			// 처리결과 HashMap에 등록 
			if (resultCnt == 0) {
				resultMap.put("result", "Success");
			} else {
				resultMap.put("result", "Fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "Fail");
			return resultMap;
		}
		return resultMap;
	}
	
	// 3) 회원가입 
	@RequestMapping(value = "/insertUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public HashMap<String, String> insertUserInfo(@RequestBody Map<String, Object> paramMap) {

		logger.debug(" @@ insertUserInfo call in UserController ==> ");
		
		int resultCnt = 0;													// 처리결과를 담는 변수 
		HashMap<String, String> resultMap = new HashMap<String, String>();	//처리결과를 반환하기 위한 변
		
		try {
			// 가입고객 등록 
			resultCnt = userService.insertUserInfo(paramMap);
			
			// 처리결과 HashMap에 등록 
			if (resultCnt > 0) {
				resultMap.put("result", "Success");
			} else {
				resultMap.put("result", "Fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "Fail");
			return resultMap;
		}
		return resultMap;
	}

	// 4) 회원 로그인
	@RequestMapping(value = "/userLogin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> userLogin(@RequestBody Map<String, Object> paramMap) {
		
		logger.debug(" @@ userLogin call in UserController ==> ");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();	//처리결과를 반환하기 위한 변
		
		try {
			// 아이디 확인
			if (userService.getUserIdCheck(paramMap) == 0) { 
				resultMap.put("result", "아이디를 확인해주세요.");
				return resultMap;
			}
			
			// 비밀번호 확인
			if (userService.getUserPasswordCheck(paramMap) == 0) { 
				resultMap.put("result", "비밀번호를 확인해주세요."); 
				return resultMap;
			}
			
			// 로그인 시 토큰 업데이트 처리하기
			userService.updateToken(paramMap);
			
			// 회원정보 가저오기
			resultMap = userService.getUserInfo(paramMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "Fail");
			return resultMap;
		}
		return resultMap;
	}
	
	// 5) 회원탈퇴 
	@RequestMapping(value = "/deleteUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public HashMap<String, String> deleteUserInfo(@RequestBody Map<String, Object> paramMap) {
		
		logger.debug(" @@ deleteUserInfo call in UserController ==> ");
		
		int resultCnt = 0;													// 처리결과를 담는 변수 
		HashMap<String, String> resultMap = new HashMap<String, String>();	//처리결과를 반환하기 위한 변
		
		try {
			// 탈퇴처리 
			resultCnt = userService.deleteUserInfo(paramMap);
			
			// 처리결과 HashMap에 등록 
			if (resultCnt > 0) {
				resultMap.put("result", "Success");
			} else {
				resultMap.put("result", "Fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "Fail");
			return resultMap;
		}
		return resultMap;
	}
	
	// 6) 동승내역 통계 
	
	// 7) 신고하기
	@RequestMapping(value = "/insertBlacklistUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public HashMap<String, String> insertBlacklistUser(@RequestBody Map<String, Object> paramMap) {
		
		logger.debug(" @@ insertBlacklistUser call in UserController ==> ");
		
		int resultCnt = 0; // 처리결과를 담는 변수 
		
		HashMap<String, String> resultMap = new HashMap<String, String>();	//처리결과를 반환하기 위한 변
		
		try {
			// 탈퇴처리 
			resultCnt = userService.insertBlacklistUser(paramMap);
			
			// 처리결과 HashMap에 등록 
			if (resultCnt > 0) {
				resultMap.put("result", "Success");
			} else {
				resultMap.put("result", "Fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "Fail");
			return resultMap;
		}
		return resultMap;
	}
	
	// 8) 신고취소하기
	@RequestMapping(value = "/deleteBlacklistUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public HashMap<String, String> delteBlacklistUser(@RequestBody Map<String, Object> paramMap) {
		
		logger.debug(" @@ delteBlacklistUser call in UserController ==> ");
		
		int resultCnt = 0; // 처리결과를 담는 변수 
		
		HashMap<String, String> resultMap = new HashMap<String, String>();	//처리결과를 반환하기 위한 변
		
		try {
			// 탈퇴처리 
			resultCnt = userService.deleteBlacklistUser(paramMap);
			
			// 처리결과 HashMap에 등록 
			if (resultCnt > 0) {
				resultMap.put("result", "Success");
			} else {
				resultMap.put("result", "Fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "Fail");
			return resultMap;
		}
		return resultMap;
	}
	
	// 9) 회원 로그인
	@RequestMapping(value = "/getTotalPay", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> getTotalPay(@RequestBody Map<String, Object> paramMap) {
		
		logger.debug(" @@ getTotalPay call in UserController ==> ");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();	//처리결과를 반환하기 위한 변수
		
		try {
			resultMap = userService.getTotalPay(paramMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "Fail");
			return resultMap;
		}
		return resultMap;
	}
}
