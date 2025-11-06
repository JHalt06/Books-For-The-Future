package com.ufund.api.ufundapi.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    private static final String NOTIFICATION_URL = "http://localhost:8080/notifications";//8080 for spring boot backend
    private final RestTemplate restTemplate = new RestTemplate(); //Create a new instance of the RestTemplate using default settings


    public void sendNotification(String message){
        try{
            System.out.println("Notification sent: " + message);
            restTemplate.postForEntity(NOTIFICATION_URL, message, String.class);
        }
        catch(Exception e){
            System.out.println("Notification failed to send" + e.getMessage());
        }
    }

}
