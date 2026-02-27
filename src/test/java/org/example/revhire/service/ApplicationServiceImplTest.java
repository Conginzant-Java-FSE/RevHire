package org.example.revhire.service;



import org.example.revhire.dto.request.ApplicationRequest;
import org.example.revhire.dto.response.ApplicationResponse;
import org.example.revhire.enums.ApplicationStatus;
import org.example.revhire.model.*;
import org.example.revhire.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ResumeFileRepository resumeFileRepository;
    @Mock
    private ApplicationNoteRepository noteRepo;
    @Mock
    private ApplicationStatusHistoryRepository statusHistoryRepo;
    @Mock
    private NotificationService notificationService;
    @Mock
    private WithdrawalReasonsRepository withdrawalReasonsRepository;
    @Mock
    private org.example.revhire.mapper.ApplicationMapper applicationMapper;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    private Applications applications;
    private Job job;
    private User user;
    private ResumeFiles resumeFile;
    private User employerUser;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        employerUser = new User();
        employerUser.setId(2L);
        employerUser.setName("Employer User");

        job = new Job();
        job.setId(1L);
        job.setTitle("Test Job");
        job.setEmployer(employerUser);

        resumeFile = new ResumeFiles();
        resumeFile.setId(1L);

        applications = new Applications();
        applications.setId(1L);
        applications.setJob(job);
        applications.setSeeker(user);
        applications.setResumeFile(resumeFile);
        applications.setStatus(ApplicationStatus.APPLIED);
    }

    @Test
    void applyForJob_Success() {
        ApplicationRequest req = new ApplicationRequest();
        req.setSeekerId(1L);
        req.setJobId(1L);
        req.setResumeFileId(1L);
        req.setCoverLetter("Test Cover Letter");

        when(applicationRepository.findAll()).thenReturn(new ArrayList<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(resumeFileRepository.findById(1L)).thenReturn(Optional.of(resumeFile));
        when(applicationRepository.save(any(Applications.class))).thenReturn(applications);

        ApplicationResponse res = new ApplicationResponse();
        res.setId(1L);
        when(applicationMapper.toDto(any(Applications.class))).thenReturn(res);

        ApplicationResponse result = applicationService.applyForJob(req);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(applicationRepository).save(any(Applications.class));
        verify(notificationService).createNotification(eq(2L), anyString(), eq("NEW_APPLICATION"));
    }

    @Test
    void applyForJob_AlreadyApplied() {
        ApplicationRequest req = new ApplicationRequest();
        req.setSeekerId(1L);
        req.setJobId(1L);

        List<Applications> existingApps = new ArrayList<>();
        existingApps.add(applications);

        when(applicationRepository.findAll()).thenReturn(existingApps);

        assertThrows(RuntimeException.class, () -> applicationService.applyForJob(req));
    }

    @Test
    void applyForJob_UserNotFound() {
        ApplicationRequest req = new ApplicationRequest();
        req.setSeekerId(1L);
        req.setJobId(1L);

        when(applicationRepository.findAll()).thenReturn(new ArrayList<>());
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> applicationService.applyForJob(req));
    }

    @Test
    void getApplicationsBySeeker_Success() {
        List<Applications> apps = new ArrayList<>();
        apps.add(applications);
        when(applicationRepository.findBySeekerId(1L)).thenReturn(apps);

        ApplicationResponse res = new ApplicationResponse();
        when(applicationMapper.toDto(any(Applications.class))).thenReturn(res);

        List<ApplicationResponse> result = applicationService.getApplicationsBySeeker(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getApplicationsByJob_Success() {
        List<Applications> apps = new ArrayList<>();
        apps.add(applications);
        when(applicationRepository.findByJobId(1L)).thenReturn(apps);

        ApplicationResponse res = new ApplicationResponse();
        when(applicationMapper.toDto(any(Applications.class))).thenReturn(res);

        List<ApplicationResponse> result = applicationService.getApplicationsByJob(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateApplicationStatus_Success() {
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(applications));
        when(applicationRepository.save(any(Applications.class))).thenReturn(applications);

        ApplicationResponse res = new ApplicationResponse();
        when(applicationMapper.toDto(any(Applications.class))).thenReturn(res);

        ApplicationResponse result = applicationService.updateApplicationStatus(1L, ApplicationStatus.SHORTLISTED);

        assertNotNull(result);
        assertEquals(ApplicationStatus.SHORTLISTED, applications.getStatus());
        verify(statusHistoryRepo).save(any(ApplicationStatusHistory.class));
        verify(notificationService).createNotification(eq(1L), anyString(), eq("APPLICATION_UPDATE"));
    }
}

