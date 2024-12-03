package com.droolsproject.droolspro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.droolsproject.droolspro.model.ClassName;

@Repository
public interface ClassNameRepository extends JpaRepository<ClassName, Integer>{

	ClassName findByClassName(String className);

}
