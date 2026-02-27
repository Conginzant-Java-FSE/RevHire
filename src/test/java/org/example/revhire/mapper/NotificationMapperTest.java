package org.example.revhire.mapper;

import org.example.revhire.dto.response.NotificationResponse;
import org.example.revhire.model.Notification;
import org.example.revhire.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationMapperTest {

    private final NotificationMapper mapper = NotificationMapper.INSTANCE;

    @Test
    void toDto_ShouldMapProperly() {
        Notification entity = new Notification();
        entity.setId(1L);
        entity.setMessage("Hello testing");
        entity.setType("INFO");
        entity.setRead(true);
        entity.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0));

        User user = new User();
        user.setId(2L);
        entity.setUser(user);

        NotificationResponse dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Hello testing", dto.getMessage());
        assertEquals("INFO", dto.getType());
        assertTrue(dto.isRead());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), dto.getCreatedAt());
    }
}

