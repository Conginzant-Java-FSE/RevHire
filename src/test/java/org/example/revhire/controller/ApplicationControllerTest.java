package org.example.revhire.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.revhire.dto.request.ApplicationRequest;
import org.example.revhire.dto.request.BulkStatusUpdateRequest;
import org.example.revhire.dto.request.WithdrawalReasonRequest;
import org.example.revhire.dto.response.ApplicationResponse;
import org.example.revhire.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private org.example.revhire.config.JwtUtils jwtUtils;

    @MockBean
    private org.example.revhire.repository.UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void applyForJob_Success() throws Exception {
        ApplicationRequest req = new ApplicationRequest();
        req.setJobId(1L);
        req.setSeekerId(1L);
        when(applicationService.applyForJob(any())).thenReturn(new ApplicationResponse());
        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void getApplicationsBySeeker_Success() throws Exception {
        when(applicationService.getApplicationsBySeeker(1L)).thenReturn(List.of(new ApplicationResponse()));
        mockMvc.perform(get("/api/applications/seeker/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getApplicationsByJob_Success() throws Exception {
        when(applicationService.getApplicationsByJob(1L)).thenReturn(List.of(new ApplicationResponse()));
        mockMvc.perform(get("/api/applications/job/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateApplicationStatus_Success() throws Exception {
        when(applicationService.updateApplicationStatus(eq(1L), any())).thenReturn(new ApplicationResponse());
        mockMvc.perform(patch("/api/applications/1/status").param("status", "UNDER_REVIEW"))
                .andExpect(status().isOk());
    }

    @Test
    void withdrawApplication_Success() throws Exception {
        WithdrawalReasonRequest req = new WithdrawalReasonRequest();
        req.setReason("Test");
        mockMvc.perform(delete("/api/applications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void addNote_Success() throws Exception {
        mockMvc.perform(post("/api/applications/1/notes").content("Test Note"))
                .andExpect(status().isOk());
    }

    @Test
    void getNotes_Success() throws Exception {
        when(applicationService.getNotes(1L)).thenReturn(List.of());
        mockMvc.perform(get("/api/applications/1/notes"))
                .andExpect(status().isOk());
    }

    @Test
    void bulkUpdateStatus_Success() throws Exception {
        BulkStatusUpdateRequest req = new BulkStatusUpdateRequest();
        mockMvc.perform(post("/api/applications/bulk-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void getFullDetails_Success() throws Exception {
        when(applicationService.getFullApplicationDetails(1L)).thenReturn(new ApplicationResponse());
        mockMvc.perform(get("/api/applications/1/full"))
                .andExpect(status().isOk());
    }

    @Test
    void getStatusHistory_Success() throws Exception {
        when(applicationService.getApplicationStatusHistory(1L)).thenReturn(List.of());
        mockMvc.perform(get("/api/applications/1/history"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteNote_Success() throws Exception {
        mockMvc.perform(delete("/api/applications/notes/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getActiveApplications_Success() throws Exception {
        when(applicationService.getActiveApplications(1L)).thenReturn(List.of());
        mockMvc.perform(get("/api/applications/active/seeker/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getByJobAndStatus_Success() throws Exception {
        when(applicationService.getApplicationsByJobAndStatus(eq(1L), any())).thenReturn(List.of());
        mockMvc.perform(get("/api/applications/job/1/status/SHORTLISTED"))
                .andExpect(status().isOk());
    }

    @Test
    void getTotalCount_Success() throws Exception {
        when(applicationService.getTotalApplicationCount()).thenReturn(10L);
        mockMvc.perform(get("/api/applications/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(10));
    }

    @Test
    void getTodayCount_Success() throws Exception {
        when(applicationService.getTodayApplicationCount()).thenReturn(5L);
        mockMvc.perform(get("/api/applications/count/today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(5));
    }

    @Test
    void searchApplications_Success() throws Exception {
        when(applicationService.searchApplications(anyString())).thenReturn(List.of());
        mockMvc.perform(get("/api/applications/search").param("query", "test"))
                .andExpect(status().isOk());
    }

    @Test
    void getWithdrawalReasons_Success() throws Exception {
        when(applicationService.getWithdrawalReasons()).thenReturn(List.of());
        mockMvc.perform(get("/api/applications/withdrawal-reasons"))
                .andExpect(status().isOk());
    }
}

