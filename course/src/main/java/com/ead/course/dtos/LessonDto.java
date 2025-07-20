package com.ead.course.dtos;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LessonDto {

  @NotBlank(message = "Title cannot be empty")
  private String title;
  private String description;
  @NotBlank(message = "Video URL cannot be empty")
  private String videoUrl;

}
