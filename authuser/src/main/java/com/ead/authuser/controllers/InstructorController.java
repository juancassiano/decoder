package com.ead.authuser.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.authuser.dtos.InstructorDto;
import com.ead.authuser.dtos.UserCourseDto;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/instructors")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InstructorController {

  @Autowired
  private UserService userService;

  @PostMapping("/subscription")
  public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid InstructorDto instructorDto){
    log.debug("POST saveSubscriptionUserInCourse userId {} ", instructorDto.getUserId());
    
    Optional<UserModel> userModelOptional = userService.findById(instructorDto.getUserId());

    if (!userModelOptional.isPresent()) {
      log.debug("User not found userId {} ", instructorDto.getUserId());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    UserModel userModel = userModelOptional.get();
    userModel.setUserType(UserType.INSTRUCTOR);
    userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
    userService.save(userModel);

    log.debug("Instructor subscription saved for userId {} ", userModel.getUserId());
    return ResponseEntity.status(HttpStatus.CREATED).body(userModel);

  }
}
