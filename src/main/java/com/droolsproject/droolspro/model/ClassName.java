package com.droolsproject.droolspro.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cnid")
public class ClassName {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cnid;
	private String className;
	@JsonIgnoreProperties("className")
	@OneToMany(mappedBy = "className", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<ClassProperty> classProperty;
}
