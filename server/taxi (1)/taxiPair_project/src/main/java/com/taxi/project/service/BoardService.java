package com.taxi.project.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxi.project.mapper.BoardMapper;
import com.taxi.project.mapper.UserMapper;

@Service
public class BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private UserMapper userMapper;

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 택시메이트 목록 조회
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public List<Map<String, Object>> getBoardList(Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ getBoardList call in BoardService ==> ");
		
		return boardMapper.getBoardList(paramMap);
		
	}
	
	/**
	 * 택시메이트 검색 목록 조회
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public List<Map<String, Object>> getBoardSearchList(Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ getBoardSearchList call in BoardService ==> ");
		
		paramMap.put("field", "end_area");
		
		return boardMapper.getBoardSearchList(paramMap);
		
	}
	
	/**
	 * 최근 택시메이트
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public List<Map<String, Object>> getRecentlyBoardingGroup(Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ getRecentlyMate call in BoardService ==> ");
		
		return boardMapper.getRecentlyBoardingGroup(paramMap);
		
	}
	
	/**
	 * 동승그룹 생성
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int createGroup(Map<String, Object> paramMap) throws Exception {
		
		logger.debug(" @@ crateGroup call in BoardService ==> ");
		
		return boardMapper.createGroup(paramMap);
		
	}
	
	/**
	 * 동승자 정보 등록
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int insertBoardingUser(Map<String, Object> paramMap) throws Exception {
		logger.debug(" @@ insertBoardingUser call in BoardService ==> ");
		
		return boardMapper.insertBoardingUser(paramMap);
	}
	

	/**
	 * 택시탑승 완료처리하기 
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int fixBoardingGroup(Map<String, Object> paramMap) throws Exception {
		logger.debug(" @@ fixBoardingGroup call in BoardService ==> ");

		// API 호출준비
		HttpsURLConnection connection = null;

		try {
			// Connection 준비, 호출 URL 입력
			URL url = new URL("https://apis.openapi.sk.com/tmap/routes?version=1&format=json&callback=result");
			connection = (HttpsURLConnection) url.openConnection();

			connection.setDoOutput(true); 														// 쓰기모드 사용여부 지정 
			connection.setRequestMethod("POST");												// POST 처리 
			connection.setRequestProperty("Content-Type", "application/json");					// JSON으로 호출 
			connection.setRequestProperty("Accept", "application/json");						// JSON으로 지정 
			connection.setRequestProperty("char-set", "UTF-8");									// 인코딩 지정 
			connection.setRequestProperty("appkey", "l7xx1780d174d8a1437ba02bd62438ae4c1a");	// 호출 API키 지정 
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			
			/* 
			 * T Map API Call -> 예상시간, 예상금액 가져오기
			 * 
			 * 출처 : https://tmapapi.sktelecom.com/main.html#webservice/docs/tmapRouteDoc
			 * 
			 * Input Parameter
			 *  - startX : 출발지 X좌표
			 *  - startY : 출발지 Y좌표
			 *  - endX : 목적지 X좌표
			 *  - endY : 목적지 Y좌표
			 *  - reqCoordType, resCoordType : 좌표타입(고정)
			 *  - searchOption : 경로탐색 옵션 (0: 교통최적+추천)
			 *  - trafficInfo : 교통정보 포함 여부 (포함 안할예정이므로 'N' 고정
			 *  
			 * Output Parameter
			 *  - totalTime : 총 소요시간(초)
			 *  - taxiFare : 택시 예상요금
			 * 
			 */
			
			// 입력 parameter 셋팅 
			JSONObject job = new JSONObject();
			job.put("startX", paramMap.get("startX").toString());
			job.put("startY", paramMap.get("startY").toString());
			job.put("endX", paramMap.get("endX").toString());
			job.put("endY", paramMap.get("endY").toString());
			job.put("reqCoordType", "WGS84GEO");
			job.put("resCoordType", "WGS84GEO");
			job.put("searchOption", "0");
			job.put("trafficInfo", "N");
			
			// Send request
			OutputStream wr = connection.getOutputStream();										// API Call
			wr.write(job.toString().getBytes("UTF-8"));
			wr.close();																			// 작업 완료 후 DataOutputStream 닫기 

			// Get Response
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));					// 처리결과 수신
			// Java5 이상은 StringBuffer 를 이용해서 결과 값을 받아야
			StringBuilder response = new StringBuilder();
			String line;
			
			while ((line = rd.readLine()) != null) {
				response.append(line);
			}
			rd.close();
			
			// JSON Object로 전환 
			ObjectMapper mapper = new ObjectMapper();
			
			// 데이터 추출
			JsonNode node = mapper.readTree(response.toString());
			JsonNode features = node.get("features");
			JsonNode properties = features.get(0).get("properties");
			
			String totalTime = properties.get("totalTime").asText();					// 예상시간 : 분 단위로 저장 
			paramMap.put("expectTime", Math.round(Double.parseDouble(totalTime) / 60));
			paramMap.put("expectPay", properties.get("taxiFare").asText());				// 예상금액 
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			// 작업 완료 후 connection 종료 
			if (connection != null) {
				connection.disconnect();
			}
		}

		return boardMapper.fixBoardingGroup(paramMap);
	}
	
	/**
	 * 택시탑승 완료내역 조회
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public Map<String, Object> getFixBoardingGroup(Map<String, Object> paramMap) throws Exception {
		logger.debug(" @@ getFixBoardingGroup call in BoardService ==> ");
		
		return boardMapper.getFixBoardingGroup(paramMap);
	}
	
	/**
	 * 택시운행종료 
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int finishDrive(Map<String, Object> paramMap) throws Exception {
		logger.debug(" @@ finishDrive call in BoardService ==> ");
		
		return boardMapper.finishDrive(paramMap);
	}
	
	/**
	 * 동승해야 할 그룹정보 조회
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public List<Map<String, Object>> getTodayBoardingGroup(Map<String, Object> paramMap) throws Exception {
		logger.debug(" @@ getTodayBoardingGroup call in BoardService ==> ");
		
		return boardMapper.getTodayBoardingGroup(paramMap);
	}
	
	public Map<String, Object> getTodayBoardingGroupCnt(Map<String, Object> paramMap) throws Exception {
		logger.debug(" @@ getTodayBoardingGroup call in BoardService ==> ");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> resultList =  boardMapper.getTodayBoardingGroup(paramMap);
		
		resultMap.put("count", resultList.size());
		
		return resultMap;
	}
	
	/**
	 * 동승취소  
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int escapeDrive(Map<String, Object> paramMap) throws Exception {
		logger.debug(" @@ escapeDrive call in BoardService ==> ");
		
		return boardMapper.escapeDrive(paramMap);
	}
	
	/**
	 * 동승취소  
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public int deleteGroup(Map<String, Object> paramMap) throws Exception {
		logger.debug(" @@ deleteGroup call in BoardService ==> ");
		
		return boardMapper.deleteGroup(paramMap);
	}
	
	/**
	 * 그룹장 체크 
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public String leaderChk(Map<String, Object> paramMap) throws Exception {
		logger.debug(" @@ leaderChk call in BoardService ==> ");
		
		return boardMapper.leaderChk(paramMap);
	}
	
	/**
	 * 푸시 날리기
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public void pushMsg(Map<String, Object> paramMap) throws JSONException, InterruptedException, UnsupportedEncodingException  {
    	// 고객 토큰 값 리스트 가져오기 
    	List<Map<String, Object>> userTokenList = new ArrayList<Map<String, Object>>();
    	try {
			userTokenList = userMapper.getUserTokenList(paramMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    	
    	// 전송처리 
        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson(userTokenList);

        HttpEntity<String> request = new HttpEntity<>(notifications);

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try{
            String firebaseResponse = pushNotification.get();
        }
        catch (InterruptedException e){
            logger.debug("got interrupted!");
            throw new InterruptedException();
        }
        catch (ExecutionException e){
            logger.debug("execution error!");
        }
    }
}
