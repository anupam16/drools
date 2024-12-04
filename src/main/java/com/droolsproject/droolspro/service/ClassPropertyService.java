package com.droolsproject.droolspro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.droolsproject.droolspro.model.ClassName;
import com.droolsproject.droolspro.model.ClassProperty;
import com.droolsproject.droolspro.repository.ClassNameRepository;

@Service
public class ClassPropertyService {
	// @Autowired
	// private ClassPropertyRepository classPropertyRepository;

	@Autowired
	private ClassNameRepository classNameRepository;
	
	public List<ClassProperty> getClasPropertyList(String className) {
		ClassName cn = classNameRepository.findByClassName(className);
		List<ClassProperty> list = null;
		if (cn != null) {
			list = cn.getClassProperty();
		}
		return list;
	}
	
	public List<ClassName> getClasNameList() {
		return classNameRepository.findAll();
	}

}
