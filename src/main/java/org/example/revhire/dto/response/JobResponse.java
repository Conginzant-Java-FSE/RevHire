package org.example.revhire.dto.response;

import org.example.revhire.enums.JobStatus;
import org.example.revhire.enums.JobType;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class JobResponse extends RepresentationModel<JobResponse> {
    private Long id;
    private Long employerId;
    private String employerName;
    private String companyName;

    private String title;
    private String description;
    private String requirements;
    private String requiredEducationLevel;
    private String location;
    private Integer salaryMin;
    private Integer salaryMax;
    private JobType jobType;
    private Integer experienceYears;
    private Integer maxExperienceYears;
    private Integer openings;
    private LocalDate deadline;
    private JobStatus status;
    private Integer viewCount;
    private LocalDateTime postedAt;

    private String skillsRequired;
    private List<String> skills;

    public JobResponse() {
    }

    public JobResponse(Long id, Long employerId, String employerName, String companyName, String title,
                       String description, String requirements, String requiredEducationLevel, String location, Integer salaryMin,
                       Integer salaryMax,
                       JobType jobType, Integer experienceYears, Integer maxExperienceYears, Integer openings, LocalDate deadline,
                       JobStatus status,
                       LocalDateTime postedAt, String skillsRequired, List<String> skills) {
        this.id = id;
        this.employerId = employerId;
        this.employerName = employerName;
        this.companyName = companyName;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.requiredEducationLevel = requiredEducationLevel;
        this.location = location;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.jobType = jobType;
        this.experienceYears = experienceYears;
        this.maxExperienceYears = maxExperienceYears;
        this.openings = openings;
        this.deadline = deadline;
        this.status = status;
        this.postedAt = postedAt;
        this.skillsRequired = skillsRequired;
        this.skills = skills;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getRequiredEducationLevel() {
        return requiredEducationLevel;
    }

    public void setRequiredEducationLevel(String requiredEducationLevel) {
        this.requiredEducationLevel = requiredEducationLevel;
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

    public Integer getMaxExperienceYears() {
        return maxExperienceYears;
    }

    public void setMaxExperienceYears(Integer maxExperienceYears) {
        this.maxExperienceYears = maxExperienceYears;
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

    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getSkillsRequired() {
        return skillsRequired;
    }

    public void setSkillsRequired(String skillsRequired) {
        this.skillsRequired = skillsRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        JobResponse that = (JobResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "JobResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", companyName='" + companyName + '\'' +
                ", status=" + status +
                '}';
    }
}
