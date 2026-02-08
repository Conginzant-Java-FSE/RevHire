package org.example.revhire.model;

import jakarta.persistence.*;
import org.apache.catalina.User;

@Entity
@Table(name = "employer_stats")
public class EmployerStats {

    @Id
    private Long employerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "employer_id")
    private User employer;

    @Column(name = "total_jobs")
    private int totalJobs = 0;

    @Column(name = "active_jobs")
    private int activeJobs = 0;

    @Column(name = "total_applications")
    private int totalApplications = 0;

    @Column(name = "pending_reviews")
    private int pendingReviews = 0;

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }

    public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }

    public int getTotalJobs() {
        return totalJobs;
    }

    public void setTotalJobs(int totalJobs) {
        this.totalJobs = totalJobs;
    }

    public int getActiveJobs() {
        return activeJobs;
    }

    public void setActiveJobs(int activeJobs) {
        this.activeJobs = activeJobs;
    }

    public int getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(int totalApplications) {
        this.totalApplications = totalApplications;
    }

    public int getPendingReviews() {
        return pendingReviews;
    }

    public void setPendingReviews(int pendingReviews) {
        this.pendingReviews = pendingReviews;
    }

}

