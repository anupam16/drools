package com.droolsproject.droolspro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@Entity
@Table(name="ClassProperty")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cpid")
public class ClassProperty {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cpid;
	private String fieldName;
	private String fieldType; 
	@JsonIgnoreProperties("classProperty")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cnid", referencedColumnName = "cnid")
	private ClassName className;
}
