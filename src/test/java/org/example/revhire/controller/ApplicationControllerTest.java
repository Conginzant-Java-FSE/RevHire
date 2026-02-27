package org.example.revhire.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.revhire.dto.request.ApplicationRequest;
import org.example.revhire.dto.response.ApplicationResponse;
import org.example.revhire.enums.ApplicationStatus;
import org.example.revhire.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ApplicationControllerTest {
    @org.springframework.boot.test.mock.mockito.MockBean
    private org.example.revhire.config.JwtUtils jwtUtils;
    @org.springframework.boot.test.mock.mockito.MockBean
    private org.example.revhire.repository.UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @Autowired
    private ObjectMapper objectMapper;

    private ApplicationResponse applicationResponse;

    @BeforeEach
    void setUp() {
        applicationResponse = new ApplicationResponse();
        applicationResponse.setId(1L);
        applicationResponse.setJobId(1L);
        applicationResponse.setJobTitle("Software Engineer");
        applicationResponse.setSeekerId(1L);
        applicationResponse.setSeekerName("John Doe");
        applicationResponse.setStatus(ApplicationStatus.APPLIED);
        applicationResponse.setAppliedAt(LocalDateTime.now());
    }

    @Test
    void applyForJob_Success() throws Exception {
        ApplicationRequest request = new ApplicationRequest();
        request.setJobId(1L);
        request.setSeekerId(1L);
        request.setResumeFileId(1L);

        when(applicationService.applyForJob(any(ApplicationRequest.class))).thenReturn(applicationResponse);

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.jobTitle").value("Software Engineer"));
    }

    @Test
    void getApplicationsBySeeker_Success() throws Exception {
        List<ApplicationResponse> responses = Collections.singletonList(applicationResponse);
        when(applicationService.getApplicationsBySeeker(1L)).thenReturn(responses);

        mockMvc.perform(get("/api/applications/seeker/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].jobTitle").value("Software Engineer"));
    }

    @Test
    void getApplicationsByJob_Success() throws Exception {
        List<ApplicationResponse> responses = Collections.singletonList(applicationResponse);
        when(applicationService.getApplicationsByJob(1L)).thenReturn(responses);

        mockMvc.perform(get("/api/applications/job/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void updateApplicationStatus_Success() throws Exception {
        applicationResponse.setStatus(ApplicationStatus.SHORTLISTED);
        when(applicationService.updateApplicationStatus(eq(1L), any(ApplicationStatus.class)))
                .thenReturn(applicationResponse);

        mockMvc.perform(patch("/api/applications/1/status")
                        .param("status", "SHORTLISTED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("SHORTLISTED"));
    }

    @Test
    void withdrawApplication_Success() throws Exception {
        java.util.Map<String, String> payload = new java.util.HashMap<>();
        payload.put("reason", "Found another job");

        mockMvc.perform(delete("/api/applications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }
}

