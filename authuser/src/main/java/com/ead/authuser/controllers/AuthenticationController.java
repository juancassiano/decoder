package com.ead.authuser.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

  @Autowired
  private UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class) @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){
    if(userService.existsByEmail(userDto.getEmail())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Email is already in use!");
    }
    if(userService.existsByUsername(userDto.getUsername())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Username is already in use!");
    }
    var userModel = new UserModel();
    BeanUtils.copyProperties(userDto, userModel);
    userModel.setUserStatus(UserStatus.ACTIVE);
    userModel.setUserType(UserType.STUDENT);
    userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
    userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

    userService.save(userModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
  }
}
