package org.example.revhire.controller;


import org.example.revhire.model.Notification;
import org.example.revhire.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class NotificationControllerTest {
    @org.springframework.boot.test.mock.mockito.MockBean
    private org.example.revhire.config.JwtUtils jwtUtils;
    @org.springframework.boot.test.mock.mockito.MockBean
    private org.example.revhire.repository.UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Test message");
        notification.setRead(false);
    }

    @Test
    void getUserNotifications_Success() throws Exception {
        when(notificationService.getUserNotifications(2L)).thenReturn(Collections.singletonList(notification));

        mockMvc.perform(get("/api/notifications/user/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Test message"))
                .andExpect(jsonPath("$[0].read").value(false));

        verify(notificationService).getUserNotifications(2L);
    }

    @Test
    void markAsRead_Success() throws Exception {
        doNothing().when(notificationService).markAsRead(1L);

        mockMvc.perform(patch("/api/notifications/1/read"))
                .andExpect(status().isNoContent());

        verify(notificationService).markAsRead(1L);
    }

    @Test
    void getUnreadCount_Success() throws Exception {
        when(notificationService.getUnreadCount(2L)).thenReturn(5L);

        mockMvc.perform(get("/api/notifications/user/2/unread-count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(notificationService).getUnreadCount(2L);
    }

    @Test
    void markAllAsRead_Success() throws Exception {
        doNothing().when(notificationService).markAllAsRead(2L);

        mockMvc.perform(patch("/api/notifications/user/2/read-all"))
                .andExpect(status().isNoContent());

        verify(notificationService).markAllAsRead(2L);
    }

    @Test
    void deleteNotification_Success() throws Exception {
        doNothing().when(notificationService).deleteNotification(1L);

        mockMvc.perform(delete("/api/notifications/1"))
                .andExpect(status().isNoContent());

        verify(notificationService).deleteNotification(1L);
    }

    @Test
    void clearAllNotifications_Success() throws Exception {
        doNothing().when(notificationService).clearAllNotifications(2L);

        mockMvc.perform(delete("/api/notifications/user/2/clear-all"))
                .andExpect(status().isNoContent());

        verify(notificationService).clearAllNotifications(2L);
    }
}


