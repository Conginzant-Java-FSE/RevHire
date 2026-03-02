package org.example.revhire.controller;


import org.example.revhire.model.Notification;
import org.example.revhire.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private org.example.revhire.config.JwtUtils jwtUtils;

    @MockBean
    private org.example.revhire.repository.UserRepository userRepository;

    @Test
    void getUserNotifications_Success() throws Exception {
        when(notificationService.getUserNotifications(1L)).thenReturn(List.of(new Notification()));
        mockMvc.perform(get("/api/notifications/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    void markAsRead_Success() throws Exception {
        mockMvc.perform(patch("/api/notifications/1/read"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUnreadCount_Success() throws Exception {
        when(notificationService.getUnreadCount(1L)).thenReturn(5L);
        mockMvc.perform(get("/api/notifications/user/1/unread-count"))
                .andExpect(status().isOk());
    }

    @Test
    void markAllAsRead_Success() throws Exception {
        mockMvc.perform(patch("/api/notifications/user/1/read-all"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNotification_Success() throws Exception {
        mockMvc.perform(delete("/api/notifications/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void clearAllNotifications_Success() throws Exception {
        mockMvc.perform(delete("/api/notifications/user/1/clear-all"))
                .andExpect(status().isNoContent());
    }
}

