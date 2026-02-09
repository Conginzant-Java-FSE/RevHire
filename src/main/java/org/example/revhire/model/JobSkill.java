package org.example.revhire.model;

import jakarta.persistence.*;
import org.example.revhire.model.User;
import org.example.revhire.enums.JobStatus;
import org.example.revhire.enums.JobType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_skills")
public class JobSkill {


    protected JobSkill() {
    }


    public JobSkill(Job job, String skill) {
        this.job = job;
        this.skill = skill;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private String skill;

    public Long getId() {
        return id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }
}