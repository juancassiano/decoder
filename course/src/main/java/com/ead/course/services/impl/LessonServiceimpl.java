package com.ead.course.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;

@Service
public class LessonServiceimpl implements LessonService {
  
  @Autowired
  private LessonRepository lessonRepository;

  
}
