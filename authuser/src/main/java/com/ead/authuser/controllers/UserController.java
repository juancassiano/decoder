package com.ead.authuser.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
   @PageableDefault (page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) {

    Page<UserModel> userModelPage = userService.findAll(pageable, spec);

      userModelPage.forEach(userModel -> userModel.add(
          linkTo(methodOn(UserController.class).getOneUser(userModel.getUserId())).withSelfRel()));
      
    return ResponseEntity.status(HttpStatus.OK)
        .body(userModelPage);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId) {
    Optional<UserModel> userModelOptional = userService.findById(userId);
    if (!userModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
  }
    return ResponseEntity.status(HttpStatus.OK)
        .body(userModelOptional.get());
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
    Optional<UserModel> userModelOptional = userService.findById(userId);
    if (!userModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    userService.delete(userModelOptional.get());
    return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
  }
  
  @PutMapping("/{userId}")
  public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId, @RequestBody @Validated(UserDto.UserView.UserPut.class) @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {
    Optional<UserModel> userModelOptional = userService.findById(userId);
    if (!userModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    var userModel = userModelOptional.get();
    userModel.setFullName(userDto.getFullName());
    userModel.setPhoneNumber(userDto.getPhoneNumber());
    userModel.setCpf(userDto.getCpf());
    userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
    userService.save(userModel);

    return ResponseEntity.status(HttpStatus.OK).body(userModel);
  }

  @PutMapping("/{userId}/password")
  public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId, @RequestBody @Validated(UserDto.UserView.PasswordPut.class) @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {
    Optional<UserModel> userModelOptional = userService.findById(userId);
    if (!userModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    if (!userModelOptional.get().getPassword().equals(userDto.getOldPassword())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Mismatched old password!");
    }

    var userModel = userModelOptional.get();
    userModel.setPassword(userDto.getPassword());
    userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
    userService.save(userModel);

    return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully");
  }

  @PutMapping("/{userId}/image")
  public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId, @RequestBody @Validated(UserDto.UserView.ImagePut.class) @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto) {
    Optional<UserModel> userModelOptional = userService.findById(userId);
    if (!userModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    var userModel = userModelOptional.get();
    userModel.setImageUrl(userDto.getImageUrl());
    userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
    userService.save(userModel);

    return ResponseEntity.status(HttpStatus.OK).body("Image updated successfully");
  }
}
