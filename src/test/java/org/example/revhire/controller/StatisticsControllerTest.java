package org.example.revhire.controller;

import org.example.revhire.dto.response.EmployerStatsResponse;
import org.example.revhire.dto.response.JobStatsResponse;
import org.example.revhire.dto.response.PlatformStatsResponse;
import org.example.revhire.service.StatisticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatisticsController.class)
@AutoConfigureMockMvc(addFilters = false)
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;

    @MockBean
    private org.example.revhire.config.JwtUtils jwtUtils;

    @MockBean
    private org.example.revhire.repository.UserRepository userRepository;

    @Test
    void getEmployerStats_Success() throws Exception {
        when(statisticsService.getEmployerStats(11)).thenReturn(new EmployerStatsResponse());
        mockMvc.perform(get("/api/statistics/employer/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getPlatformOverview_Success() throws Exception {
        when(statisticsService.getPlatformOverview()).thenReturn(new PlatformStatsResponse());
        mockMvc.perform(get("/api/statistics/platform"))
                .andExpect(status().isOk());
    }

    @Test
    void getJobAnalytics_Success() throws Exception {
        when(statisticsService.getJobAnalytics()).thenReturn(new JobStatsResponse());
        mockMvc.perform(get("/api/statistics/jobs"))
                .andExpect(status().isOk());
    }

    @Test
    void getApplicationTrends_Success() throws Exception {
        when(statisticsService.getApplicationTrends()).thenReturn(List.of());
        mockMvc.perform(get("/api/statistics/trends"))
                .andExpect(status().isOk());
    }

    @Test
    void getEmployerActivity_Success() throws Exception {
        when(statisticsService.getEmployerActivity(11)).thenReturn(Map.of());
        mockMvc.perform(get("/api/statistics/activity/employer/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getSeekerEngagement_Success() throws Exception {
        when(statisticsService.getSeekerEngagement(11)).thenReturn(Map.of());
        mockMvc.perform(get("/api/statistics/engagement/seeker/1"))
                .andExpect(status().isOk());
    }
}
