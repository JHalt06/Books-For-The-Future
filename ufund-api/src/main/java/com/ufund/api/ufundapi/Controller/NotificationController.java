package com.ufund.api.ufundapi.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//Receive and store notifications
@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:4200")//Allows angular
public class NotificationController {
    private final List<String> notifications = new ArrayList<>();
    
    @GetMapping
    public List<String> getNotifications(){
        return notifications;
    }

    @PostMapping
    public void addNotification(@RequestBody String message){ //An object that maps keys to values. A map cannot contain duplicate keys;
        notifications.add(message);
        System.out.println("New notification: " + message);
    }

    @DeleteMapping("/clear")
    public void clearNotifications(){
        notifications.clear();
        System.out.println("Notifications cleared");
    }
}
