package org.example.revhire.model;

import jakarta.persistence.*;
import org.example.revhire.model.User;
import org.example.revhire.enums.JobStatus;
import org.example.revhire.enums.JobType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_views")
public class JobView {


    protected JobView() {
    }


    public JobView(Job job, User user) {
        this.job = job;
        this.user = user;
        this.viewedAt = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime viewedAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getViewedAt() {
        return viewedAt;
    }
}