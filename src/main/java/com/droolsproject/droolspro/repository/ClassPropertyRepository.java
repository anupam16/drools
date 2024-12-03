package com.droolsproject.droolspro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.droolsproject.droolspro.model.ClassProperty;

@Repository
public interface ClassPropertyRepository extends JpaRepository<ClassProperty, Integer>{

	
}
