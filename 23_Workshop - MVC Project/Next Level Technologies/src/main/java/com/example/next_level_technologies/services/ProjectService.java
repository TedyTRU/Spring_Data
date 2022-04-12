package com.example.next_level_technologies.services;

import com.example.next_level_technologies.entities.dto.ExportedProjectDto;
import com.example.next_level_technologies.entities.dto.ProjectDto;
import com.example.next_level_technologies.entities.models.Project;

import java.io.IOException;
import java.util.List;

public interface ProjectService {

    String FILE_PATH = "files/xmls/projects.xml";

    boolean exist();

    String getXmlForImport() throws IOException;

    Long create (ProjectDto request);

    Project find(Long id);

    List<ExportedProjectDto> finishedProjects();
}
