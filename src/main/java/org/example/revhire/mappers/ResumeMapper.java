package org.example.revhire.mappers;


import org.example.revhire.dto.request.*;
import org.example.revhire.dto.response.*;
import org.example.revhire.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResumeMapper {

    ResumeMapper INSTANCE = Mappers.getMapper(ResumeMapper.class);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "user", ignore = true)
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    @org.mapstruct.Mapping(target = "createdBy", ignore = true)
    @org.mapstruct.Mapping(target = "updatedBy", ignore = true)
    ResumeObjective toEntity(ResumeObjectiveRequest request);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "user", ignore = true)
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    @org.mapstruct.Mapping(target = "createdBy", ignore = true)
    @org.mapstruct.Mapping(target = "updatedBy", ignore = true)
    ResumeEducation toEntity(ResumeEducationRequest request);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "user", ignore = true)
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    @org.mapstruct.Mapping(target = "createdBy", ignore = true)
    @org.mapstruct.Mapping(target = "updatedBy", ignore = true)
    ResumeExperience toEntity(ResumeExperienceRequest request);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "user", ignore = true)
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    @org.mapstruct.Mapping(target = "createdBy", ignore = true)
    @org.mapstruct.Mapping(target = "updatedBy", ignore = true)
    ResumeSkills toEntity(ResumeSkillsRequest request);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "user", ignore = true)
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    @org.mapstruct.Mapping(target = "createdBy", ignore = true)
    @org.mapstruct.Mapping(target = "updatedBy", ignore = true)
    ResumeProjects toEntity(ResumeProjectsRequest request);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "user", ignore = true)
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    @org.mapstruct.Mapping(target = "createdBy", ignore = true)
    @org.mapstruct.Mapping(target = "updatedBy", ignore = true)
    ResumeCertifications toEntity(ResumeCertificationsRequest request);

    ResumeObjectiveResponse toDto(ResumeObjective objective);

    ResumeEducationResponse toDto(ResumeEducation education);

    ResumeExperienceResponse toDto(ResumeExperience experience);

    ResumeSkillsResponse toDto(ResumeSkills skill);

    ResumeProjectsResponse toDto(ResumeProjects project);

    ResumeCertificationsResponse toDto(ResumeCertifications certification);

    @org.mapstruct.Mapping(source = "uploadedAt", target = "uploadedAt")
    ResumeFilesResponse toDto(ResumeFiles file);

    List<ResumeObjectiveResponse> toObjectiveDtoList(List<ResumeObjective> objectives);

    List<ResumeEducationResponse> toEducationDtoList(List<ResumeEducation> educations);

    List<ResumeExperienceResponse> toExperienceDtoList(List<ResumeExperience> experiences);

    List<ResumeSkillsResponse> toSkillsDtoList(List<ResumeSkills> skills);

    List<ResumeProjectsResponse> toProjectsDtoList(List<ResumeProjects> projects);

    List<ResumeCertificationsResponse> toCertificationsDtoList(List<ResumeCertifications> certifications);

    List<ResumeFilesResponse> toFilesDtoList(List<ResumeFiles> files);
}
