package com.taxi.project.service;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AndroidPushPeriodicNotifications {

    public static String PeriodicNotificationJson(List<Map<String, Object>> listMap) throws JSONException, UnsupportedEncodingException {
        LocalDate localDate = LocalDate.now();
        
        JSONObject body = new JSONObject();

        List<String> tokenlist = new ArrayList<String>();

        for(int i=0; i< listMap.size(); i++){
            tokenlist.add(String.valueOf(listMap.get(i).get("token")));
        }
//        tokenlist.add("eDegwmz3Qpqc9l-v6tJVbl:APA91bHGlLusPHHMHrdhWO_-2vuVjQrzXiNNcIYKiIkDIY7MpI9t7sPGoYcQNdvYxehXhdd9ywHrcfOGKQepD1xRKIjIBdJFDEvScyyGwgZzxwE5B4gG8vES7QAl1xiFIfv7tpV0Q87Q");
 
        JSONArray array = new JSONArray();

        for(int i=0; i<tokenlist.size(); i++) {
            array.put(tokenlist.get(i));
        }

        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();
        notification.put("title", listMap.get(0).get("title"));
        notification.put("body", "2시간 이내로 입금하지 않을 시 신고당할 수 있습니다.");
        notification.put("click_action", "SPLASH_ACTION");

        body.put("data", notification);
        
        return body.toString();
    }
}