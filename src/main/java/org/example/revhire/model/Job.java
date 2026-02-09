package org.example.revhire.model;

import jakarta.persistence.*;
import org.example.revhire.enums.JobStatus;
import org.example.revhire.enums.JobType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {


    protected Job() {
    }


    public Job(User employer,
               String title,
               String description,
               String requirements,
               String skillsRequired,
               String location,
               Integer salaryMin,
               Integer salaryMax,
               JobType jobType,
               Integer experienceYears,
               Integer openings,
               LocalDate deadline) {

        this.employer = employer;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.skillsRequired = skillsRequired;
        this.location = location;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.jobType = jobType;
        this.experienceYears = experienceYears;
        this.openings = openings;
        this.deadline = deadline;
        this.status = JobStatus.OPEN;
        this.postedAt = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private User employer;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    private String skillsRequired;

    private String location;

    private Integer salaryMin;
    private Integer salaryMax;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private Integer experienceYears;

    private Integer openings;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.OPEN;

    private LocalDateTime postedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobSkill> jobSkills;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobView> jobViews;



    public Long getId() {
        return id;
    }

    public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getSkillsRequired() {
        return skillsRequired;
    }

    public void setSkillsRequired(String skillsRequired) {
        this.skillsRequired = skillsRequired;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(Integer salaryMin) {
        this.salaryMin = salaryMin;
    }

    public Integer getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(Integer salaryMax) {
        this.salaryMax = salaryMax;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Integer getOpenings() {
        return openings;
    }

    public void setOpenings(Integer openings) {
        this.openings = openings;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }
}
