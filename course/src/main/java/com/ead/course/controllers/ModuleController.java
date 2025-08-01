package com.ead.course.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;

@RestController
@RequestMapping("/courses/{courseId}/modules")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

  @Autowired
  private ModuleService moduleService;
  @Autowired
  private CourseService courseService;

  @PostMapping()
  public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseid, @RequestBody @Valid ModuleDto moduleDto) {
    Optional<CourseModel> courseModelOptional = courseService.findById(courseid);
    if (!courseModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
    }

    ModuleModel moduleModel = new ModuleModel();
    BeanUtils.copyProperties(moduleDto, moduleModel);
    moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
    moduleModel.setCourse(courseModelOptional.get());
    return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
  }

  @DeleteMapping("/{moduleId}")
  public ResponseEntity<Object> deleteModule(@PathVariable(value = "courseId") UUID courseId, @PathVariable(value = "moduleId") UUID moduleId) {
    Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
    if (!moduleModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
    }

    moduleService.delete(moduleModelOptional.get());
    return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully.");
  }

  @PutMapping("/{moduleId}")
  public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId, 
    @PathVariable(value = "moduleId") UUID moduleId, 
    @RequestBody @Valid ModuleDto moduleDto) {
    
    Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
    if (!moduleModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
    }
    ModuleModel moduleModel = moduleModelOptional.get();
    moduleModel.setTitle(moduleDto.getTitle());
    moduleModel.setDescription(moduleDto.getDescription());

    return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel));
  }  

  @GetMapping()
  public ResponseEntity<Page<ModuleModel>> getAllModules(@PathVariable(value = "courseId") UUID courseId,
  SpecificationTemplate.ModuleSpec spec, @PageableDefault(page = 0, size = 10, sort = "moduleId", direction = Sort.Direction.ASC) Pageable pageable) {

    return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllByCourse(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable));
  }

  @GetMapping("/{moduleId}")
  public ResponseEntity<Object> getOneModule(@PathVariable(value = "courseId") UUID courseId, 
    @PathVariable(value = "moduleId") UUID moduleId) {

    Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
    if (!moduleModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
    }
    return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional.get());
  }
}
