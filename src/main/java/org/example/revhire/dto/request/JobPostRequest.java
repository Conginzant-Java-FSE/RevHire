package org.example.revhire.dto.request;

import org.example.revhire.enums.JobType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class JobPostRequest {
    @NotNull
    private Long employerId;

    @NotBlank
    private String title;

    private String description;
    private String requirements;
    private String requiredEducationLevel;
    private String skillsRequired;
    private String location;
    private Integer salaryMin;
    private Integer salaryMax;

    @NotNull
    private JobType jobType;

    private Integer experienceYears;
    private Integer maxExperienceYears;
    private Integer openings;

    @Future
    private LocalDate deadline;

    private List<String> skills; // For JobSkill table

    public JobPostRequest() {
    }

    public JobPostRequest(Long employerId, String title, String description, String requirements,
                          String requiredEducationLevel,
                          String skillsRequired, String location, Integer salaryMin, Integer salaryMax, JobType jobType,
                          Integer experienceYears, Integer maxExperienceYears, Integer openings, LocalDate deadline,
                          List<String> skills) {
        this.employerId = employerId;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.requiredEducationLevel = requiredEducationLevel;
        this.skillsRequired = skillsRequired;
        this.location = location;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.jobType = jobType;
        this.experienceYears = experienceYears;
        this.maxExperienceYears = maxExperienceYears;
        this.openings = openings;
        this.deadline = deadline;
        this.skills = skills;
    }

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
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

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        JobPostRequest that = (JobPostRequest) o;
        return Objects.equals(employerId, that.employerId) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employerId, title);
    }

    @Override
    public String toString() {
        return "JobPostRequest{" +
                "employerId=" + employerId +
                ", title='" + title + '\'' +
                ", jobType=" + jobType +
                '}';
    }
}
