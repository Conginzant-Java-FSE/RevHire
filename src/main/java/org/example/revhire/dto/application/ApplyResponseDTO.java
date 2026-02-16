package org.example.revhire.dto.application;

import java.time.LocalDateTime;

public class ApplyResponseDTO {

    private Long applicationId;
    private Long jobId;
    private Long seekerId;
    private String status;
    private LocalDateTime appliedAt;

    public ApplyResponseDTO() {
    }

    public ApplyResponseDTO(Long applicationId,
                            Long jobId,
                            Long seekerId,
                            String status,
                            LocalDateTime appliedAt) {
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.seekerId = seekerId;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public Long getJobId() {
        return jobId;
    }

    public Long getSeekerId() {
        return seekerId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }
}
