package com.droolsproject.droolspro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.droolsproject.droolspro.model.Rule;

public interface RuleRepository extends JpaRepository<Rule, Long>{

}
