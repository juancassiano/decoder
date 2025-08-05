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

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/modules/{moduleId}/lessons")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

  @Autowired
  private LessonService lessonService;
  @Autowired
  private ModuleService moduleService;

  @PostMapping
  public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId, @RequestBody @Valid LessonDto lessonDto) {
    log.debug("POST saveLesson lessonDto received: {}", lessonDto);
    Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);
    if (!moduleModelOptional.isPresent()) {
      log.warn("Module not found with ID: {}", moduleId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.");
    }

    LessonModel lessonModel = new LessonModel();
    BeanUtils.copyProperties(lessonDto, lessonModel);
    lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
    lessonModel.setModule(moduleModelOptional.get());

    log.info("Lesson saved successfully with ID: {}", lessonModel.getLessonId());
    return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
  }

  @DeleteMapping("/{lessonId}")
  public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId, @PathVariable(value = "lessonId") UUID lessonId) {
    log.debug("DELETE deleteLesson lessonId received: {}", lessonId);
    Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
    if (!lessonModelOptional.isPresent()) {
      log.warn("Lesson not found for module ID: {} and lesson ID: {}", moduleId, lessonId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
    }
    lessonService.delete(lessonModelOptional.get());

    log.debug("Lesson deleted successfully with ID: {}", lessonId);
    log.info("Lesson deleted successfully with ID: {}", lessonId);
    return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully.");
  }

  @PutMapping("/{lessonId}")
  public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId") UUID moduleId, @PathVariable(value = "lessonId") UUID lessonId, @RequestBody @Valid LessonDto lessonDto) {
    log.debug("PUT updateLesson lessonDto received: {}", lessonDto);
    Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
    if (!lessonModelOptional.isPresent()) {
      log.warn("Lesson not found for module ID: {} and lesson ID: {}", moduleId, lessonId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module."); 
    }
    LessonModel lessonModel = lessonModelOptional.get();
    lessonModel.setTitle(lessonDto.getTitle());
    lessonModel.setDescription(lessonDto.getDescription());
    lessonModel.setVideoUrl(lessonDto.getVideoUrl());

    log.info("Lesson updated successfully with ID: {}", lessonId);
    return ResponseEntity.status(HttpStatus.OK).body(lessonService.save(lessonModel));
  } 

  @GetMapping
  public ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable(value = "moduleId") UUID moduleId,
  SpecificationTemplate.LessonSpec spec, @PageableDefault(page = 0, size = 10, sort = "lessonId", direction = Sort.Direction.ASC) Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllByModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
  }

  @GetMapping("/{lessonId}")
  public ResponseEntity<Object> getOneLesson(@PathVariable(value = "moduleId") UUID moduleId, @PathVariable(value = "lessonId") UUID lessonId) {
    Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
    if (!lessonModelOptional.isPresent()) { 
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
    }
    return ResponseEntity.status(HttpStatus.OK).body(lessonModelOptional.get());
  }

}
