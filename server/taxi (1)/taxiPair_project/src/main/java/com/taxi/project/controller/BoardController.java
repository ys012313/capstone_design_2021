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

import com.taxi.project.service.BoardService;
import com.taxi.project.service.UserService;

/**
 * BoardCOntroller
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

@RequestMapping(value = "/board")
@RestController
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private UserService userService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 1) 택시메이트 목록 가져오기 
	@RequestMapping(value = "/getBoardList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Map<String, Object>> getBoardList(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		List<Map<String, Object>> boardList = new ArrayList<Map<String, Object>>();
		
		logger.debug(" @@ getBoardList call in BoardController ==> ");
		
		try {
			boardList = boardService.getBoardList(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return boardList;
	}
	
	// 2) 택시메이트 검색 목록 가져오기 
	@RequestMapping(value = "/getBoardSearchList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Map<String, Object>> getBoardSearchList(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		List<Map<String, Object>> boardList = new ArrayList<Map<String, Object>>();
		
		logger.debug(" @@ getBoardSearchList call in BoardController ==> ");
		
		try {
			boardList = boardService.getBoardSearchList(paramMap);
			logger.debug(boardList.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return boardList;
	}
	
	// 3) 동승그룹 만들기
	@RequestMapping(value = "/createGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> createGroup(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		int result1 = 0;
		int result2 = 0;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		logger.debug(" @@ createGroup call in BoardController ==> ");
		
		try {
			String userId = paramMap.get("writer").toString();
			paramMap.put("userId", userId);
			int blackUser = userService.getBlackListUser(paramMap);
			if (blackUser > 0) {
				resultMap.put("result", "신고된 계정으로는 작업할 수 없습니다.");
				return resultMap;
			}
			
			result1 = boardService.createGroup(paramMap);
			
			// 동승그룹을 만든 후, 등록자 본인을 동승자 그룹의 그룹장으로 추가
			if (result1 > 0) {
				paramMap.put("leaderYn", "Y");
				result2 = boardService.insertBoardingUser(paramMap);
				
				if (result2 > 0) {
					resultMap.put("result", "Success");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "Fail");
			return resultMap;
		}
		
		return resultMap;
	}
	
	// 4) 동승신청하기 
	@RequestMapping(value = "/insertBoardingUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> insertBoardingUser(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		int result = 0;
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		logger.debug(" @@ insertBoardingUser call in BoardController ==> ");
		
		try {
			int blackUser = userService.getBlackListUser(paramMap);
			if (blackUser > 0) {
				resultMap.put("result", "신고된 계정으로는 작업할 수 없습니다.");
				return resultMap;
			}
			
			paramMap.put("leaderYn", "N");
			result = boardService.insertBoardingUser(paramMap);
			
			if (result > 0) {
					resultMap.put("result", "Success");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "Fail");
			return resultMap;
		}
		
		return resultMap;
	}
	
	// 5) 택시탑승 완료처리하기 
	@RequestMapping(value = "/fixBoardingGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> fixBoardingGroup(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		int result = 0;
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		logger.debug(" @@ fixBoardingGroup call in BoardController ==> ");
		
		try {
			result = boardService.fixBoardingGroup(paramMap);
			
			if (result > 0) {
					resultMap.put("result", "Success");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "Fail");
			return resultMap;
		}
		
		return resultMap;
	}
	
	// 6) 택시탑승 완료내역 가져오기 
	@RequestMapping(value = "/getFixBoardingGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> getFixBoardingGroup(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		logger.debug(" @@ getFixBoardingGroup call in BoardController ==> ");
		
		try {
			resultMap = boardService.getFixBoardingGroup(paramMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "Fail");
			return resultMap;
		}
		
		return resultMap;
	}
	
	// 7) 택시운행종료
	@RequestMapping(value = "/finishDrive", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> finishDrive(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		int result = 0;
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		logger.debug(" @@ finishDrive call in BoardController ==> ");
		
		try {
			result = boardService.finishDrive(paramMap);
			
			if (result > 0) {
				resultMap.put("result", "Success");
				
				// 푸시알림 여부 확인 후 발송
				if(paramMap.get("pushYn").equals("Y")) {
					boardService.pushMsg(paramMap);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "Fail");
			return resultMap;
		}
		
		return resultMap;
	}
	
	// 8) 최근 동승내역 조회  
	@RequestMapping(value = "/getRecentlyBoardingGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Map<String, Object>> getRecentlyMate(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		List<Map<String, Object>> boardList = new ArrayList<Map<String, Object>>();
		
		logger.debug(" @@ getRecentlyBoardingGroup call in BoardController ==> ");
		
		try {
			boardList = boardService.getRecentlyBoardingGroup(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return boardList;
	}
	
	// 9) 동승해야 할 그룹정보 조회  
	@RequestMapping(value = "/getTodayBoardingGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Map<String, Object>> getTodayBoardingGroup(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		List<Map<String, Object>> boardList = new ArrayList<Map<String, Object>>();
		
		logger.debug(" @@ getTodayBoardingGroup call in BoardController ==> ");
		
		try {
			int blackUser = userService.getBlackListUser(paramMap);
			if (blackUser > 0) {
				return boardList;
			}
			
			boardList = boardService.getTodayBoardingGroup(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return boardList;
	}
	
	// 9-1) 동승해야 할 그룹정보 건수 
	@RequestMapping(value = "/getTodayBoardingGroupCnt", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> getTodayBoardingGroupCnt(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		Map<String, Object> boardList = new HashMap<String, Object>();
		
		logger.debug(" @@ getTodayBoardingGroup call in BoardController ==> ");
		
		try {
			int blackUser = userService.getBlackListUser(paramMap);
			if (blackUser > 0) {
				return boardList;
			}
			boardList = boardService.getTodayBoardingGroupCnt(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return boardList;
	}
	
	// 10) 동승취소 
	@RequestMapping(value = "/escapeDrive", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> escapeDrive(@RequestBody Map<String, Object> paramMap) throws Exception {
		
		int result = 0;
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		logger.debug(" @@ escapeDrive call in BoardController ==> ");
		
		try {
			String leader = boardService.leaderChk(paramMap);
			result = boardService.escapeDrive(paramMap);
			
			if (leader.equals("Y")) {
				boardService.deleteGroup(paramMap);
			}
			
			if (result > 0) {
					resultMap.put("result", "Success");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "Fail");
			return resultMap;
		}
		
		return resultMap;
	}
}
