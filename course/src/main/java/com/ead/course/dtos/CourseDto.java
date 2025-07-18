package com.ead.course.dtos;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;

import lombok.Data;

@Data
public class CourseDto {

  @NotBlank(message = "Course name cannot be empty")
  private String name;
  @NotNull(message = "Course description cannot be empty")
  private String description;
  private String imageUrl;
  @NotNull(message = "Course status cannot be empty")
  private CourseStatus courseStatus;
  @NotNull(message = "Course level cannot be empty")
  private UUID userInstructor;
  @NotNull(message = "Course level cannot be empty")
  private CourseLevel courseLevel;
}
