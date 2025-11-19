package com.ead.course.services.impl;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;

@Service
public class CourseUserServiceImpl implements CourseUserService {

  @Autowired
  private CourseUserRepository courseUserRepository;

  @Autowired
  private AuthUserClient authUserClient;

  @Override
  public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
    return courseUserRepository.existsByCourseAndUserId(courseModel, userId);
  }

  @Override
  public CourseUserModel save(CourseUserModel convertCourseUserModel) {
    return courseUserRepository.save(convertCourseUserModel);
  }

  @Transactional
  @Override
  public CourseUserModel saveAndSendSubscriptionUserInCourse(CourseUserModel convertCourseUserModel) {
    CourseUserModel courseUserModel = courseUserRepository.save(convertCourseUserModel);

    authUserClient.postSubscriptionUserInCourse(courseUserModel.getCourse().getCourseId(), courseUserModel.getUserId());
    return courseUserModel;
  }
}
