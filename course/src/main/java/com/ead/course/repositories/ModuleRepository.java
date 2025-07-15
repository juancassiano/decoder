package com.ead.course.repositories;

import java.util.UUID;

import org.aspectj.apache.bcel.classfile.ModuleMainClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ModuleRepository extends JpaRepository<ModuleMainClass, UUID> {
  
}
