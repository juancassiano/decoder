package com.ead.course.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseUserRepository courseUserRepository;

  @Autowired
  private CourseRepository courseRepository;
  @Autowired
  private ModuleRepository moduleRepository;
  @Autowired
  private LessonRepository lessonRepository;

    CourseServiceImpl(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }

  @Override
  @Transactional
  public void delete(CourseModel courseModel) {
    List<ModuleModel> modulesModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
    if (!modulesModelList.isEmpty()) {
      for (ModuleModel module : modulesModelList) {
        List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(module.getModuleId()); 
        if (!lessonModelList.isEmpty()) {
          lessonRepository.deleteAll(lessonModelList);    
        }
      }
      moduleRepository.deleteAll(modulesModelList);
    }
    List<CourseUserModel> courseUserModelList = courseUserRepository.findAllCourseUsersIntoCourse(courseModel.getCourseId());
    if(!courseUserModelList.isEmpty()) {
      courseUserRepository.deleteAll(courseUserModelList);
    }
    courseRepository.delete(courseModel);
  }

  @Override
  public CourseModel save(CourseModel courseModel) {
    return courseRepository.save(courseModel);
  }

  @Override
  public Optional<CourseModel> findById(UUID courseId) {
    return courseRepository.findById(courseId);
  }

  @Override
  public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
    return courseRepository.findAll(spec, pageable);
  }
  
}
