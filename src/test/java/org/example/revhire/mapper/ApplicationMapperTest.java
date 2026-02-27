package org.example.revhire.mapper;


import org.example.revhire.dto.response.ApplicationResponse;
import org.example.revhire.model.Applications;
import org.example.revhire.model.Job;
import org.example.revhire.model.User;
import org.example.revhire.model.ResumeFiles;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationMapperTest {

    private final ApplicationMapper applicationMapper = Mappers.getMapper(ApplicationMapper.class);

    @Test
    void toDto_Success() {
        Job job = new Job();
        job.setId(10L);
        job.setTitle("Frontend Developer");
        job.setLocation("Remote");

        User seeker = new User();
        seeker.setId(20L);
        seeker.setName("Alice Smith");
        seeker.setEmail("alice@test.com");

        ResumeFiles resume = new ResumeFiles();
        resume.setId(30L);

        Applications application = new Applications();
        application.setId(1L);
        application.setJob(job);
        application.setSeeker(seeker);
        application.setResumeFile(resume);
        application.setAppliedAt(LocalDateTime.now());

        ApplicationResponse response = applicationMapper.toDto(application);

        assertNotNull(response);
        assertEquals(10L, response.getJobId());
        assertEquals("Frontend Developer", response.getJobTitle());
        assertEquals("Remote", response.getLocation());
        assertEquals(20L, response.getSeekerId());
        assertEquals("Alice Smith", response.getSeekerName());
        assertEquals("alice@test.com", response.getSeekerEmail());
        assertEquals(30L, response.getResumeFileId());
    }
}

