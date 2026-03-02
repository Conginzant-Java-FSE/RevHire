package org.example.revhire.service;

import org.example.revhire.dto.response.EmployerStatsResponse;
import org.example.revhire.dto.response.PlatformStatsResponse;
import org.example.revhire.dto.response.JobStatsResponse;
import org.example.revhire.enums.JobStatus;
import org.example.revhire.enums.ApplicationStatus;
import org.example.revhire.enums.Role;
import org.example.revhire.enums.JobType;
import org.example.revhire.model.Applications;
import org.example.revhire.model.Job;
import org.example.revhire.model.User;
import org.example.revhire.repository.ApplicationRepository;
import org.example.revhire.repository.JobRepository;
import org.example.revhire.repository.ResumeObjectiveRepository;
import org.example.revhire.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatisticsServiceImplTest {

    @Mock
    private JobRepository jobRepository;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ResumeObjectiveRepository resumeRepository;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEmployerStats_Success() {
        User employerUser = new User();
        employerUser.setId(1L);

        Job openJob = new Job();
        openJob.setId(10L);
        openJob.setStatus(JobStatus.OPEN);
        openJob.setEmployer(employerUser);

        Job filledJob = new Job();
        filledJob.setId(11L);
        filledJob.setStatus(JobStatus.FILLED);
        filledJob.setEmployer(employerUser);

        Job otherJob = new Job();
        otherJob.setId(12L);
        User otherEmployer = new User();
        otherEmployer.setId(2L);
        otherJob.setEmployer(otherEmployer);

        when(jobRepository.findAll()).thenReturn(List.of(openJob, filledJob, otherJob));

        Applications app1 = new Applications();
        app1.setJob(openJob);
        app1.setStatus(ApplicationStatus.APPLIED);

        Applications app2 = new Applications();
        app2.setJob(filledJob);
        app2.setStatus(ApplicationStatus.SHORTLISTED);

        when(applicationRepository.findAll()).thenReturn(List.of(app1, app2));

        EmployerStatsResponse response = statisticsService.getEmployerStats(11);

        assertNotNull(response);
        assertEquals(1, response.getActiveJobs());
        assertEquals(1, response.getFilledPositions());
        assertEquals(2, response.getTotalApplications());
        assertEquals(1, response.getNewApplications());
    }

    @Test
    void getPlatformOverview_Success() {
        User user1 = new User();
        user1.setRole(Role.JOB_SEEKER);
        User user2 = new User();
        user2.setRole(Role.EMPLOYER);

        Job job1 = new Job();
        job1.setStatus(JobStatus.OPEN);

        Applications app1 = new Applications();
        app1.setAppliedAt(LocalDateTime.now().minusHours(1));

        Applications app2 = new Applications();
        app2.setAppliedAt(LocalDateTime.now().minusDays(1));

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(jobRepository.findAll()).thenReturn(List.of(job1));
        when(applicationRepository.findAll()).thenReturn(List.of(app1, app2));
        when(resumeRepository.count()).thenReturn(5L);

        PlatformStatsResponse response = statisticsService.getPlatformOverview();

        assertNotNull(response);
        assertEquals(2, response.getTotalUsers());
        assertEquals(1, response.getTotalJobs());
        assertEquals(2, response.getTotalApplications());
        assertEquals(5L, response.getTotalResumes());
        assertEquals(1, response.getTodayApplications());
        assertTrue(response.getUsersByRole().containsKey("JOB_SEEKER"));
        assertTrue(response.getJobsByStatus().containsKey("OPEN"));
    }

    @Test
    void getJobAnalytics_Success() {
        Job job1 = new Job();
        job1.setStatus(JobStatus.OPEN);
        job1.setJobType(JobType.FULLTIME);
        job1.setLocation("Remote");
        job1.setExperienceYears(2);

        Job job2 = new Job();
        job2.setStatus(JobStatus.FILLED);
        job2.setJobType(JobType.PARTTIME);
        job2.setLocation("New York");
        job2.setExperienceYears(5);

        when(jobRepository.findAll()).thenReturn(List.of(job1, job2));

        JobStatsResponse response = statisticsService.getJobAnalytics();

        assertNotNull(response);
        assertEquals(2, response.getTotalJobs());
        assertEquals(1, response.getOpenJobs());
        assertEquals(1, response.getFilledJobs());
        assertEquals(1, response.getJobsByType().get("FULLTIME"));
        assertEquals(1, response.getJobsByLocation().get("Remote"));
        assertEquals(1, response.getJobsByExperience().get("2"));
    }

    @Test
    void getApplicationTrends_Success() {
        Applications app1 = new Applications();
        app1.setStatus(ApplicationStatus.APPLIED);
        Applications app2 = new Applications();
        app2.setStatus(ApplicationStatus.SHORTLISTED);

        when(applicationRepository.findAll()).thenReturn(List.of(app1, app2));

        List<Map<String, Object>> trends = statisticsService.getApplicationTrends();

        assertEquals(2, trends.size());
        assertTrue(trends.stream().anyMatch(m -> m.get("status").equals("APPLIED")));
    }

    @Test
    void getEmployerActivity_Success() {
        User employer = new User();
        employer.setId(1L);
        Job job = new Job();
        job.setId(10L);
        job.setEmployer(employer);
        job.setStatus(JobStatus.OPEN);

        Applications app = new Applications();
        app.setJob(job);

        when(jobRepository.findAll()).thenReturn(List.of(job));
        when(applicationRepository.findAll()).thenReturn(List.of(app));

        Map<String, Object> activity = statisticsService.getEmployerActivity(11);

        assertEquals(1, activity.get("totalJobs"));
        assertEquals(1L, activity.get("activeJobs"));
        assertEquals(1L, activity.get("totalApplicationsReceived"));
    }

    @Test
    void getSeekerEngagement_Success() {
        User seeker = new User();
        seeker.setId(1L);
        Applications app1 = new Applications();
        app1.setSeeker(seeker);
        app1.setStatus(ApplicationStatus.SHORTLISTED);
        Applications app2 = new Applications();
        app2.setSeeker(seeker);
        app2.setStatus(ApplicationStatus.REJECTED);

        when(applicationRepository.findAll()).thenReturn(List.of(app1, app2));

        Map<String, Object> engagement = statisticsService.getSeekerEngagement(11);

        assertEquals(2, engagement.get("totalApplications"));
        assertEquals(1L, engagement.get("shortlistedCount"));
        assertEquals(1L, engagement.get("rejectedCount"));
    }
}
