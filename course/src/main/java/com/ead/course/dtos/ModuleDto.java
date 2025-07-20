package com.ead.course.dtos;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ModuleDto {

  @NotBlank(message = "Title cannot be blank")
  private String title;
  @NotBlank(message = "Description cannot be blank")
  private String description;
}
