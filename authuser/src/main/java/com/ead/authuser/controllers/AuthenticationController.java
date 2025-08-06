package com.ead.authuser.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.models.enums.UserStatus;
import com.ead.authuser.models.enums.UserType;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

  @Autowired
  private UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class) @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){

    log.debug("POST registerUser userDto received: {}", userDto);
    if(userService.existsByEmail(userDto.getEmail())) {
      log.warn("Conflict: Email is already in use!");
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Email is already in use!");
    }
    if(userService.existsByUsername(userDto.getUsername())) {
      log.warn("Conflict: Username is already in use!");
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Username is already in use!");
    }
    var userModel = new UserModel();
    BeanUtils.copyProperties(userDto, userModel);
    userModel.setUserStatus(UserStatus.ACTIVE);
    userModel.setUserType(UserType.STUDENT);
    userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
    userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

    userService.save(userModel);

    log.debug("User registered successfully with ID: {}", userModel.getUserId());
    return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
  }

  @GetMapping()
  public String index(){
    log.trace("TRACE");
    log.debug("DEBUG");
    log.info("INFO");
    log.warn("WARN");
    log.error("ERROR");
    return "Logging Spring Boot";
  }
}
