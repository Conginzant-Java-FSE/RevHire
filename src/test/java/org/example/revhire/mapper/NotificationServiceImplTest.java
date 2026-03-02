package org.example.revhire.mapper;


import org.example.revhire.model.Notification;
import org.example.revhire.model.User;
import org.example.revhire.repository.NotificationRepository;
import org.example.revhire.repository.UserRepository;
import org.example.revhire.service.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserNotifications_Success() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(new ArrayList<>());
        List<Notification> result = notificationService.getUserNotifications(1L);
        assertNotNull(result);
        verify(notificationRepository).findByUserIdOrderByCreatedAtDesc(1L);
    }

    @Test
    void getUnreadNotifications_Success() {
        when(notificationRepository.findByUserIdAndIsReadFalse(1L)).thenReturn(new ArrayList<>());
        List<Notification> result = notificationService.getUnreadNotifications(1L);
        assertNotNull(result);
        verify(notificationRepository).findByUserIdAndIsReadFalse(1L);
    }

    @Test
    void markAsRead_Success() {
        Notification notification = new Notification();
        notification.setId(10L);
        notification.setRead(false);

        when(notificationRepository.findById(10L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.markAsRead(10L);

        assertTrue(notification.isRead());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void markAsRead_NotFound() {
        when(notificationRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> notificationService.markAsRead(10L));
    }

    @Test
    void createNotification_Success() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(new Notification());

        notificationService.createNotification(1L, "Test Message", "INFO");

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void createNotification_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> notificationService.createNotification(1L, "Msg", "INFO"));
    }

    @Test
    void getUnreadCount_Success() {
        User user = new User();
        user.setId(1L);
        Notification n1 = new Notification();
        n1.setUser(user);
        n1.setRead(false);

        Notification n2 = new Notification();
        n2.setUser(user);
        n2.setRead(true);

        when(notificationRepository.findAll()).thenReturn(List.of(n1, n2));

        long count = notificationService.getUnreadCount(1L);
        assertEquals(1, count);
    }

    @Test
    void markAllAsRead_Success() {
        User user = new User();
        user.setId(1L);
        Notification n1 = new Notification();
        n1.setUser(user);
        n1.setRead(false);

        when(notificationRepository.findAll()).thenReturn(List.of(n1));

        notificationService.markAllAsRead(1L);

        assertTrue(n1.isRead());
        verify(notificationRepository).save(n1);
    }

    @Test
    void deleteNotification_Success() {
        notificationService.deleteNotification(10L);
        verify(notificationRepository).deleteById(10L);
    }

    @Test
    void clearAllNotifications_Success() {
        User user = new User();
        user.setId(1L);
        Notification n1 = new Notification();
        n1.setId(10L);
        n1.setUser(user);

        when(notificationRepository.findAll()).thenReturn(List.of(n1));

        notificationService.clearAllNotifications(1L);

        verify(notificationRepository).deleteById(10L);
    }
}

