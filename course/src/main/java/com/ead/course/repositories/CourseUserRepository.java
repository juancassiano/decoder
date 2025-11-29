package com.ead.course.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;

public interface CourseUserRepository extends JpaRepository<CourseUserModel, UUID> {
  
  boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

  @Query(value = "SELECT * FROM tb_courses_users WHERE course_course_id = :courseId", nativeQuery = true)
  List<CourseUserModel> findAllCourseUsersIntoCourse(UUID courseId);

  boolean existsByUserId(UUID userId);

  void deleteAllByUserId(UUID userId);
}
