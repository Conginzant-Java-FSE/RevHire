package org.example.revhire.service;


import org.example.revhire.model.Notification;
import org.example.revhire.model.User;
import org.example.revhire.repository.NotificationRepository;
import org.example.revhire.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User user;
    private Notification notification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        notification = new Notification();
        notification.setId(10L);
        notification.setUser(user);
        notification.setMessage("Test Alert");
        notification.setType("INFO");
        notification.setIsRead(false);
    }

    @Test
    void getUserNotifications_Success() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(Collections.singletonList(notification));

        List<Notification> result = notificationService.getUserNotifications(1L);
        assertEquals(1, result.size());
        assertEquals("Test Alert", result.get(0).getMessage());
    }

    @Test
    void getUnreadNotifications_Success() {
        when(notificationRepository.findByUserIdAndIsReadFalse(1L)).thenReturn(Collections.singletonList(notification));

        List<Notification> result = notificationService.getUnreadNotifications(1L);
        assertEquals(1, result.size());
        assertFalse(result.get(0).isRead());
    }

    @Test
    void markAsRead_Success() {
        when(notificationRepository.findById(10L)).thenReturn(Optional.of(notification));

        notificationService.markAsRead(10L);
        assertTrue(notification.isRead());
        verify(notificationRepository).save(notification);
    }

    @Test
    void createNotification_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        notificationService.createNotification(1L, "New Alert", "SUCCESS");
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void getUnreadCount_Success() {
        when(notificationRepository.findAll()).thenReturn(Collections.singletonList(notification));

        long count = notificationService.getUnreadCount(1L);
        assertEquals(1L, count);
    }

    @Test
    void markAllAsRead_Success() {
        when(notificationRepository.findAll()).thenReturn(Collections.singletonList(notification));

        notificationService.markAllAsRead(1L);
        assertTrue(notification.isRead());
        verify(notificationRepository).save(notification);
    }

    @Test
    void deleteNotification_Success() {
        notificationService.deleteNotification(10L);
        verify(notificationRepository).deleteById(10L);
    }

    @Test
    void clearAllNotifications_Success() {
        when(notificationRepository.findAll()).thenReturn(Collections.singletonList(notification));

        notificationService.clearAllNotifications(1L);
        verify(notificationRepository).deleteById(10L);
    }
}

