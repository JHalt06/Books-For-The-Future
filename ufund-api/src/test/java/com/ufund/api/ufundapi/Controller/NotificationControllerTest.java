package com.ufund.api.ufundapi.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NotificationControllerTest {
    private NotificationController controller;

    @BeforeEach
    void setup(){
        controller = new NotificationController();
    }

    @Test
    void testGetNotificationsInitiallyEmpty(){
        List<String> notifications = controller.getNotifications();
        assertNotNull(notifications);
        assertTrue(notifications.isEmpty(), "Intially this list should be empty");
    }

    @Test
    void testAddNotificationSingleMessage(){
        controller.addNotification("Test message 1");
        List<String> notifications = controller.getNotifications();
        assertEquals(1, notifications.size());
        assertEquals("Test message 1", notifications.get(0));
    }

    @Test
    void testAddMultipleNotifications() {
        controller.addNotification("Test message 1");
        controller.addNotification("Test message 2");
        controller.addNotification("Test message 3");
        List<String> notifications = controller.getNotifications();
        assertEquals(3, notifications.size());
        assertEquals("Test message 1", notifications.get(0));
        assertEquals("Test message 2", notifications.get(1));
        assertEquals("Test message 3", notifications.get(2));
    }

    @Test
    void testClearNotifications(){
        controller.addNotification("Test message 1");
        controller.addNotification("Test message 2");
        assertFalse(controller.getNotifications().isEmpty());
        controller.clearNotifications();
        assertTrue(controller.getNotifications().isEmpty(), "Notifications should now be cleared");
    }
}
