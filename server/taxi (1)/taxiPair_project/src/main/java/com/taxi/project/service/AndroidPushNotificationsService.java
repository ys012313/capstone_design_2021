package com.taxi.project.service;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.taxi.project.interceptor.HeaderRequestInterceptor;

// firebase_server_key = firebase project > cloud messaging > server key

@Service
public class AndroidPushNotificationsService {
    private static final String firebase_server_key="AAAADdfkpe4:APA91bFWUGZtSXW3ePqD36krE_Gbo-4OBQMNFBu37dYi7ZFO63H1Za5bHWeKjDXj2xE-rcCUQhuoldkjkrCBB7MrUKTuZkBpOtwP-HXYEpP6yC4DY0GjF4Ih5HrqcBqXZJPrIObEiuzF";
    private static final String firebase_api_url="https://fcm.googleapis.com/fcm/send";

    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));	// 한글 ???현상 해결 메소드 

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new HeaderRequestInterceptor("Authorization",  "key=" + firebase_server_key));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json; UTF-8 "));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(firebase_api_url, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}