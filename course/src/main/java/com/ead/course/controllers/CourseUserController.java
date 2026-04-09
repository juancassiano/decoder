package com.ead.course.controllers;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.UserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import com.ead.course.specifications.SpecificationTemplate;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

  @Autowired
  private CourseService courseService;

  @Autowired
  private UserService userService;

  @GetMapping("/courses/{courseId}/users")
  public ResponseEntity<Object> getAllUsersByCourse(SpecificationTemplate.UserSpec spec, @PathVariable (value = "courseId") UUID courseId,
    @PageableDefault (page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) {

    log.debug("GET getAllUsersByCourse");
    Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
    if (!courseModelOptional.isPresent()) {
      log.warn("Course not found with ID: {}", courseId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
    }

    return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(
      SpecificationTemplate.userCourseId(courseId).and(spec), pageable));
  }

  @PostMapping("/courses/{courseId}/users/subscription")
  public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid SubscriptionDto subscriptionDto) {
    log.debug("POST saveSubscriptionUserInCourse");

    Optional<CourseModel> courseModelOptional= courseService.findById(courseId);
    if(!courseModelOptional.isPresent()) {
      log.warn("Course not found CourseId {}", courseId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
    }
    if(courseService.existsByCourseAndUser(courseId, subscriptionDto.getUserId())) {
      log.warn("User {} already subscribed in course {}", subscriptionDto.getUserId(), courseId);
      return ResponseEntity.status(HttpStatus.CONFLICT).body("User already subscribed in this course");
    }
    Optional<UserModel> userModelOptional = userService.findById(subscriptionDto.getUserId());
    if(!userModelOptional.isPresent()) {
      log.warn("User not found UserId {}", subscriptionDto.getUserId());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    if(userModelOptional.get().getUserStatus().equals(UserStatus.BLOCKED.toString())) {
      log.warn("User {} is blocked", subscriptionDto.getUserId());
      return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked");
    }
    courseService.saveSubscriptionUserInCourse(courseModelOptional.get().getCourseId(), userModelOptional.get().getUserId());
    
    return ResponseEntity.status(HttpStatus.CREATED).body("");

  }

}
