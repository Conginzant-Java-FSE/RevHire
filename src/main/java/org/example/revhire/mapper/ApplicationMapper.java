package org.example.revhire.mapper;



import org.example.revhire.dto.response.ApplicationResponse;
import org.example.revhire.model.Applications;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {


    @Mapping(source = "job.id", target = "jobId")
    @Mapping(source = "job.title", target = "jobTitle")
    @Mapping(source = "job.location", target = "location")
    @Mapping(source = "seeker.id", target = "seekerId")
    @Mapping(source = "seeker.name", target = "seekerName")
    @Mapping(source = "seeker.email", target = "seekerEmail")
    @Mapping(source = "resumeFile.id", target = "resumeFileId")
    @Mapping(source = "appliedAt", target = "appliedAt")
    @Mapping(target = "companyName", ignore = true)
    ApplicationResponse toDto(Applications applications);
}
