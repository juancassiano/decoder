package com.ead.course.controllers;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.clients.CourseClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/courses/{courseId}/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

  @Autowired
  private CourseClient courseClient;

  @Autowired
  private CourseService courseService;

  @Autowired
  private CourseUserService courseUserService;

  @GetMapping
  public ResponseEntity<Page<UserDto>> getAllUsersByCourse(@PathVariable (value = "courseId") UUID courseId,
    @PageableDefault (page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) {

    log.debug("GET getAllUsersByCourse");

    return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllUsersByCourse(courseId, pageable));
  }

  @PostMapping("/subscription")
  public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid SubscriptionDto subscriptionDto) {
    log.debug("POST saveSubscriptionUserInCourse");

    Optional<CourseModel> courseModelOptional= courseService.findById(courseId);
    if(!courseModelOptional.isPresent()) {
      log.warn("Course not found CourseId {}", courseId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
    }

    if(courseUserService.existsByCourseAndUserId(courseModelOptional.get(), subscriptionDto.getUserId())) {
      log.warn("Conflict: User {} already subscribed in Course {}", subscriptionDto.getUserId(), courseId);
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: User already subscribed in course");
    }

    CourseUserModel courseUserModel = courseUserService.save(courseModelOptional.get().convertCourseUserModel(subscriptionDto.getUserId()));

    return ResponseEntity.status(HttpStatus.CREATED).body("inscriptionId: " + courseUserModel.getId());

  }
  
}
