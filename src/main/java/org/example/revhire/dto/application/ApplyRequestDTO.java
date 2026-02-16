package org.example.revhire.dto.application;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ApplyRequestDTO {

    @NotNull(message = "Job ID cannot be null")
    private Long jobId;

    @NotNull(message = "Seeker ID cannot be null")
    private Long seekerId;

    @NotNull(message = "Resume File ID cannot be null")
    private Long resumeFileId;

    @Size(max = 2000, message = "Cover letter must not exceed 2000 characters")
    private String coverLetter;

    public ApplyRequestDTO() {
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(Long seekerId) {
        this.seekerId = seekerId;
    }

    public Long getResumeFileId() {
        return resumeFileId;
    }

    public void setResumeFileId(Long resumeFileId) {
        this.resumeFileId = resumeFileId;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
}
