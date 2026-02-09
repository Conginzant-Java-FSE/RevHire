package org.example.revhire.model;
import jakarta.persistence.*;

@Entity
@Table(name = "employer_stats")
public class EmployerStats {

    @Id
    private Integer employerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "employer_id")
    private User employer;

    private Integer totalJobs = 0;
    private Integer activeJobs = 0;
    private Integer totalApplications = 0;
    private Integer pendingReviews = 0;

    public EmployerStats() {}

    public Integer getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Integer employerId) {
        this.employerId = employerId;
    }

    public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }

    public Integer getTotalJobs() {
        return totalJobs;
    }

    public void setTotalJobs(Integer totalJobs) {
        this.totalJobs = totalJobs;
    }

    public Integer getActiveJobs() {
        return activeJobs;
    }

    public void setActiveJobs(Integer activeJobs) {
        this.activeJobs = activeJobs;
    }

    public Integer getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(Integer totalApplications) {
        this.totalApplications = totalApplications;
    }

    public Integer getPendingReviews() {
        return pendingReviews;
    }

    public void setPendingReviews(Integer pendingReviews) {
        this.pendingReviews = pendingReviews;
    }
}
