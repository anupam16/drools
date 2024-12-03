package com.droolsproject.droolspro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.droolsproject.droolspro.model.ClassName;
import com.droolsproject.droolspro.model.ClassProperty;
import com.droolsproject.droolspro.service.ClassPropertyService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ClassPropertyController {
	@Autowired
	private ClassPropertyService classPropertyService;	
	
	@GetMapping("/classproperty/{classID}")
	public ResponseEntity<List<ClassProperty>> getClassList(@PathVariable("classID") String className) {
		List<ClassProperty> list = classPropertyService.getClasPropertyList(className);
		return new ResponseEntity<List<ClassProperty>>(list, HttpStatus.OK);
	}
	@GetMapping("/clasnamelist")
	public ResponseEntity<List<ClassName>> getClassNameList() {
		List<ClassName> list = classPropertyService.getClasNameList();
		return new ResponseEntity<List<ClassName>>(list, HttpStatus.OK);
	}

}
