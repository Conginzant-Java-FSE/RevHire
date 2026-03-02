package org.example.revhire.mapper;

import org.example.revhire.dto.request.ApplicationRequest;
import org.example.revhire.dto.response.ApplicationResponse;
import org.example.revhire.enums.ApplicationStatus;
import org.example.revhire.model.Applications;
import org.example.revhire.model.Job;
import org.example.revhire.model.User;
import org.example.revhire.repository.ApplicationRepository;
import org.example.revhire.repository.JobRepository;
import org.example.revhire.repository.UserRepository;
import org.example.revhire.mapper.ApplicationMapper;
import org.example.revhire.service.ApplicationServiceImpl;
import org.example.revhire.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.example.revhire.dto.request.BulkStatusUpdateRequest;
import org.example.revhire.model.ApplicationNotes;
import org.example.revhire.model.ApplicationStatusHistory;
import org.example.revhire.model.WithdrawalReasons;
import org.example.revhire.model.ResumeFiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationMapper applicationMapper;
    @Mock
    private NotificationService notificationService;
    @Mock
    private org.example.revhire.repository.ResumeFileRepository resumeFileRepository;
    @Mock
    private org.example.revhire.repository.ApplicationStatusHistoryRepository statusHistoryRepo;
    @Mock
    private org.example.revhire.repository.ApplicationNoteRepository noteRepo;
    @Mock
    private org.example.revhire.repository.WithdrawalReasonsRepository withdrawalReasonsRepository;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void applyForJob_Success() {
        ApplicationRequest req = new ApplicationRequest();
        req.setSeekerId(1L);
        req.setJobId(10L);
        req.setResumeFileId(5L);

        User seekerUser = new User();
        seekerUser.setId(1L);

        Job job = new Job();
        job.setId(10L);
        User employerUser = new User();
        employerUser.setId(2L);
        job.setEmployer(employerUser);

        when(userRepository.findById(1L)).thenReturn(Optional.of(seekerUser));
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));
        when(applicationRepository.findAll()).thenReturn(Collections.emptyList());
        when(resumeFileRepository.findById(5L)).thenReturn(Optional.of(new ResumeFiles()));

        Applications savedApp = new Applications();
        savedApp.setId(100L);
        when(applicationRepository.save(any(Applications.class))).thenReturn(savedApp);

        ApplicationResponse appResp = new ApplicationResponse();
        appResp.setId(100L);
        when(applicationMapper.toDto(any(Applications.class))).thenReturn(appResp);

        ApplicationResponse response = applicationService.applyForJob(req);

        assertNotNull(response);
        verify(applicationRepository).save(any(Applications.class));
        verify(notificationService).createNotification(any(), any(), any());
    }

    @Test
    void applyForJob_AlreadyApplied() {
        ApplicationRequest req = new ApplicationRequest();
        req.setSeekerId(1L);
        req.setJobId(10L);

        Applications existingApp = new Applications();
        Job job = new Job();
        job.setId(10L);
        existingApp.setJob(job);
        User seeker = new User();
        seeker.setId(1L);
        existingApp.setSeeker(seeker);

        when(applicationRepository.findAll()).thenReturn(List.of(existingApp));

        assertThrows(RuntimeException.class, () -> applicationService.applyForJob(req));
    }

    @Test
    void getApplicationsBySeeker_Success() {
        when(applicationRepository.findBySeekerId(1L)).thenReturn(new ArrayList<>());
        List<ApplicationResponse> result = applicationService.getApplicationsBySeeker(1L);
        assertNotNull(result);
    }

    @Test
    void getApplicationsByJob_Success() {
        when(applicationRepository.findByJobId(10L)).thenReturn(new ArrayList<>());
        List<ApplicationResponse> result = applicationService.getApplicationsByJob(10L);
        assertNotNull(result);
    }

    @Test
    void updateApplicationStatus_Success() {
        Applications app = new Applications();
        app.setId(100L);
        app.setStatus(ApplicationStatus.APPLIED);
        User seekerUser = new User();
        seekerUser.setId(1L);
        app.setSeeker(seekerUser);
        Job job = new Job();
        job.setId(10L);
        job.setTitle("Job");
        app.setJob(job);

        when(applicationRepository.findById(100L)).thenReturn(Optional.of(app));
        when(applicationRepository.save(any(Applications.class))).thenReturn(app);
        when(applicationMapper.toDto(any())).thenReturn(new ApplicationResponse());

        applicationService.updateApplicationStatus(100L, ApplicationStatus.SHORTLISTED);

        assertEquals(ApplicationStatus.SHORTLISTED, app.getStatus());
        verify(statusHistoryRepo).save(any());
        verify(notificationService).createNotification(any(), any(), any());
    }

    @Test
    void withdrawApplication_Success() {
        Applications app = new Applications();
        app.setStatus(ApplicationStatus.APPLIED);
        Job job = new Job();
        job.setId(10L);
        User emp = new User();
        emp.setId(2L);
        job.setEmployer(emp);
        app.setJob(job);

        when(applicationRepository.findById(100L)).thenReturn(Optional.of(app));

        applicationService.withdrawApplication(100L, "Found another job");

        assertEquals(ApplicationStatus.WITHDRAWN, app.getStatus());
        verify(withdrawalReasonsRepository).save(any());
        verify(notificationService).createNotification(any(), any(), any());
    }

    @Test
    void addNote_Success() {
        Applications app = new Applications();
        when(applicationRepository.findById(100L)).thenReturn(Optional.of(app));

        applicationService.addNote(100L, "Good candidate");

        verify(noteRepo).save(any());
    }

    @Test
    void getNotes_Success() {
        when(noteRepo.findByApplicationId(100L)).thenReturn(new ArrayList<>());
        List<ApplicationNotes> result = applicationService.getNotes(100L);
        assertNotNull(result);
    }

    @Test
    void bulkUpdateStatus_Success() {
        BulkStatusUpdateRequest req = new BulkStatusUpdateRequest();
        req.setApplicationIds(List.of(100L));
        req.setStatus(ApplicationStatus.SHORTLISTED);

        Applications app = new Applications();
        app.setStatus(ApplicationStatus.APPLIED);
        User seeker = new User();
        seeker.setId(1L);
        app.setSeeker(seeker);
        Job job = new Job();
        job.setId(10L);
        app.setJob(job);

        when(applicationRepository.findById(100L)).thenReturn(Optional.of(app));
        when(applicationMapper.toDto(any())).thenReturn(new ApplicationResponse());

        applicationService.bulkUpdateStatus(req);

        verify(applicationRepository).save(any());
    }

    @Test
    void getFullApplicationDetails_Success() {
        when(applicationRepository.findById(100L)).thenReturn(Optional.of(new Applications()));
        applicationService.getFullApplicationDetails(100L);
        verify(applicationMapper).toDto(any());
    }

    @Test
    void getApplicationStatusHistory_Success() {
        Applications app = new Applications();
        app.setId(100L);
        ApplicationStatusHistory h = new ApplicationStatusHistory();
        h.setApplication(app);
        when(statusHistoryRepo.findAll()).thenReturn(List.of(h));

        List<ApplicationStatusHistory> result = applicationService.getApplicationStatusHistory(100L);
        assertEquals(1, result.size());
    }

    @Test
    void deleteApplicationNote_Success() {
        applicationService.deleteApplicationNote(10L);
        verify(noteRepo).deleteById(10);
    }

    @Test
    void getActiveApplications_Success() {
        Applications app = new Applications();
        app.setStatus(ApplicationStatus.APPLIED);
        when(applicationRepository.findBySeekerId(1L)).thenReturn(List.of(app));
        when(applicationMapper.toDto(any())).thenReturn(new ApplicationResponse());

        List<ApplicationResponse> result = applicationService.getActiveApplications(1L);
        assertEquals(1, result.size());
    }

    @Test
    void counts_Success() {
        when(applicationRepository.count()).thenReturn(5L);
        assertEquals(5L, applicationService.getTotalApplicationCount());

        Applications app = new Applications();
        app.setAppliedAt(LocalDateTime.now());
        when(applicationRepository.findAll()).thenReturn(List.of(app));

        assertEquals(1, applicationService.getTodayApplicationCount());
    }

    @Test
    void searchApplications_Success() {
        Applications app = new Applications();
        User seeker = new User();
        seeker.setName("John");
        app.setSeeker(seeker);
        Job job = new Job();
        job.setTitle("Dev");
        app.setJob(job);
        when(applicationRepository.findAll()).thenReturn(List.of(app));
        when(applicationMapper.toDto(any())).thenReturn(new ApplicationResponse());

        List<ApplicationResponse> result = applicationService.searchApplications("john");
        assertEquals(1, result.size());
    }

    @Test
    void getApplicationsByJobAndStatus_Success() {
        Applications app = new Applications();
        app.setStatus(ApplicationStatus.APPLIED);
        when(applicationRepository.findByJobId(10L)).thenReturn(List.of(app));
        when(applicationMapper.toDto(any())).thenReturn(new ApplicationResponse());

        List<ApplicationResponse> result = applicationService.getApplicationsByJobAndStatus(10L,
                ApplicationStatus.APPLIED);
        assertEquals(1, result.size());
    }

    @Test
    void getWithdrawalReasons_Success() {
        when(withdrawalReasonsRepository.findAll()).thenReturn(new ArrayList<>());
        List<WithdrawalReasons> result = applicationService.getWithdrawalReasons();
        assertNotNull(result);
    }

    @Test
    void applyForJob_JobNotFound_Throws() {
        ApplicationRequest req = new ApplicationRequest();
        req.setJobId(999L);
        req.setSeekerId(1L);

        when(applicationRepository.findAll()).thenReturn(new ArrayList<>());
        when(jobRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> applicationService.applyForJob(req));
    }

    @Test
    void applyForJob_SeekerNotFound_Throws() {
        ApplicationRequest req = new ApplicationRequest();
        req.setJobId(1L);
        req.setSeekerId(999L);

        Job job = new Job();
        job.setId(1L);

        when(applicationRepository.findAll()).thenReturn(new ArrayList<>());
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> applicationService.applyForJob(req));
    }

    @Test
    void updateApplicationStatus_NotFound_Throws() {
        when(applicationRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> applicationService.updateApplicationStatus(999L, ApplicationStatus.SHORTLISTED));
    }

    @Test
    void withdrawApplication_NotFound_Throws() {
        when(applicationRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> applicationService.withdrawApplication(999L, "reason"));
    }

    @Test
    void addNote_ApplicationNotFound_Throws() {
        when(applicationRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> applicationService.addNote(999L, "note"));
    }

    @Test
    void getFullApplicationDetails_NotFound_Throws() {
        when(applicationRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> applicationService.getFullApplicationDetails(999L));
    }

    @Test
    void withdrawApplication_WithoutReason_Success() {
        Applications app = new Applications();
        app.setId(1L);
        app.setStatus(ApplicationStatus.APPLIED);
        Job job = new Job();
        job.setId(1L);
        job.setTitle("Dev");
        User employer = new User();
        employer.setId(10L);
        job.setEmployer(employer);
        app.setJob(job);
        app.setSeeker(new User());

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(app));

        applicationService.withdrawApplication(1L, "");
        assertEquals(ApplicationStatus.WITHDRAWN, app.getStatus());
        verify(applicationRepository).save(app);
    }

    @Test
    void getApplicationsByJobAndStatus_NoMatch_ReturnsEmpty() {
        Applications app = new Applications();
        app.setStatus(ApplicationStatus.APPLIED);
        when(applicationRepository.findByJobId(10L)).thenReturn(List.of(app));

        List<ApplicationResponse> result = applicationService.getApplicationsByJobAndStatus(10L,
                ApplicationStatus.REJECTED);
        assertEquals(0, result.size());
    }

    // --- Additional Edge Case Tests ---

    @Test
    void applyForJob_NullResumeFileId_Success() {
        ApplicationRequest req = new ApplicationRequest();
        req.setSeekerId(1L);
        req.setJobId(10L);
        req.setResumeFileId(null); // no resume file

        User seekerUser = new User();
        seekerUser.setId(1L);
        seekerUser.setName("John");

        Job job = new Job();
        job.setId(10L);
        job.setTitle("Dev");
        User employerUser = new User();
        employerUser.setId(2L);
        job.setEmployer(employerUser);

        when(applicationRepository.findAll()).thenReturn(Collections.emptyList());
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));
        when(userRepository.findById(1L)).thenReturn(Optional.of(seekerUser));

        Applications savedApp = new Applications();
        savedApp.setId(100L);
        when(applicationRepository.save(any())).thenReturn(savedApp);
        when(applicationMapper.toDto(any())).thenReturn(new ApplicationResponse());

        ApplicationResponse result = applicationService.applyForJob(req);
        assertNotNull(result);
        verify(resumeFileRepository, never()).findById(any());
    }

    @Test
    void applyForJob_ResumeFileNotFound_Throws() {
        ApplicationRequest req = new ApplicationRequest();
        req.setSeekerId(1L);
        req.setJobId(10L);
        req.setResumeFileId(99L);

        Job job = new Job();
        job.setId(10L);
        User employerUser = new User();
        employerUser.setId(2L);
        job.setEmployer(employerUser);

        User seeker = new User();
        seeker.setId(1L);

        when(applicationRepository.findAll()).thenReturn(Collections.emptyList());
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));
        when(userRepository.findById(1L)).thenReturn(Optional.of(seeker));
        when(resumeFileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> applicationService.applyForJob(req));
    }

    @Test
    void withdrawApplication_NullReason_SkipsSaveReason() {
        Applications app = new Applications();
        app.setStatus(ApplicationStatus.APPLIED);
        Job job = new Job();
        job.setId(1L);
        job.setTitle("Dev");
        User employer = new User();
        employer.setId(10L);
        job.setEmployer(employer);
        app.setJob(job);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(app));

        applicationService.withdrawApplication(1L, null);

        assertEquals(ApplicationStatus.WITHDRAWN, app.getStatus());
        verify(withdrawalReasonsRepository, never()).save(any());
    }

    @Test
    void getActiveApplications_FiltersRejectedAndWithdrawn() {
        Applications rejectedApp = new Applications();
        rejectedApp.setStatus(ApplicationStatus.REJECTED);
        Applications withdrawnApp = new Applications();
        withdrawnApp.setStatus(ApplicationStatus.WITHDRAWN);
        Applications activeApp = new Applications();
        activeApp.setStatus(ApplicationStatus.UNDER_REVIEW);

        when(applicationRepository.findBySeekerId(1L)).thenReturn(List.of(rejectedApp, withdrawnApp, activeApp));
        when(applicationMapper.toDto(any())).thenReturn(new ApplicationResponse());

        List<ApplicationResponse> result = applicationService.getActiveApplications(1L);
        assertEquals(1, result.size());
    }

    @Test
    void searchApplications_ByJobTitle() {
        Applications app = new Applications();
        User seeker = new User();
        seeker.setName("Jane");
        app.setSeeker(seeker);
        Job job = new Job();
        job.setTitle("Backend Developer");
        app.setJob(job);
        when(applicationRepository.findAll()).thenReturn(List.of(app));
        when(applicationMapper.toDto(any())).thenReturn(new ApplicationResponse());

        List<ApplicationResponse> result = applicationService.searchApplications("backend");
        assertEquals(1, result.size());
    }

    @Test
    void searchApplications_NoMatch_ReturnsEmpty() {
        Applications app = new Applications();
        User seeker = new User();
        seeker.setName("Jane");
        app.setSeeker(seeker);
        Job job = new Job();
        job.setTitle("Frontend");
        app.setJob(job);
        when(applicationRepository.findAll()).thenReturn(List.of(app));

        List<ApplicationResponse> result = applicationService.searchApplications("python");
        assertEquals(0, result.size());
    }

    @Test
    void getTodayApplicationCount_OldDates_ReturnsZero() {
        Applications app = new Applications();
        app.setAppliedAt(LocalDateTime.now().minusDays(5)); // old
        when(applicationRepository.findAll()).thenReturn(List.of(app));

        assertEquals(0, applicationService.getTodayApplicationCount());
    }

    @Test
    void getTodayApplicationCount_NullAppliedAt_ReturnsZero() {
        Applications app = new Applications();
        app.setAppliedAt(null); // null applied at
        when(applicationRepository.findAll()).thenReturn(List.of(app));

        assertEquals(0, applicationService.getTodayApplicationCount());
    }

    @Test
    void getApplicationStatusHistory_NoMatch_ReturnsEmpty() {
        Applications app = new Applications();
        app.setId(1L);
        ApplicationStatusHistory h = new ApplicationStatusHistory();
        h.setApplication(app);
        when(statusHistoryRepo.findAll()).thenReturn(List.of(h));

        List<ApplicationStatusHistory> result = applicationService.getApplicationStatusHistory(999L);
        assertEquals(0, result.size());
    }
}
