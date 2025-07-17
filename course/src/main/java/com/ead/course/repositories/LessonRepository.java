package com.ead.course.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ead.course.models.LessonModel;

@Repository
public interface LessonRepository extends JpaRepository<LessonModel, UUID> {
  
  @Query(value = "SELECT * from TB_LESSONS where module_module_id =:moduleId", nativeQuery = true)
  List<LessonModel> findAllLessonsIntoModule(@Param("moduleId") UUID moduleId);
}
