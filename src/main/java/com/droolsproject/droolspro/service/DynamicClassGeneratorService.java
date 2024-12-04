package com.droolsproject.droolspro.service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.droolsproject.droolspro.config.utility.DynamicClassGenerator;
import com.droolsproject.droolspro.model.ClassName;
import com.droolsproject.droolspro.repository.ClassPropertyRepository;
import com.droolsproject.droolspro.repository.DynamicClassGeneratorRepository;

@Service
public class DynamicClassGeneratorService {
	@Autowired
	private DynamicClassGeneratorRepository dynamicClassGeneratorRepository;
	@Autowired
	private ClassPropertyRepository classPropertyRepository;
	// @Autowired
	// private DynamicClassGenerator dynamicClassGenerator;
	private Map<String, Object> dynamicObjects = new HashMap<>();

	public ClassName addClassDetails(ClassName obj) {
		ClassName dbObj = dynamicClassGeneratorRepository.findByClassName(obj.getClassName());
		if (dbObj != null) {
			 obj.getClassProperty().forEach(x -> {
		            x.setClassName(dbObj); // Set the ClassName in ClassProperty
		        });
			classPropertyRepository.saveAll(obj.getClassProperty());
		} else {
			dynamicClassGeneratorRepository.saveAndFlush(obj);
		}		
		return obj;
	}
	
	// public Object generateDynamicClasses(Map<String, Object> dynamicObjects) {
	// 	List<ClassName> cnList = dynamicClassGeneratorRepository.findAll();
	// 	return dynamicClassGenerator.generateDynamicClass(cnList, dynamicObjects);
	// }
	
	
	// public void generateDynamicClassGlobal()
	// 		throws JsonProcessingException, IllegalArgumentException, IllegalAccessException {
	// 	Object obj = generateDynamicClasses(dynamicObjects);
	// 	Class<?> dynamicClass = obj.getClass();
	// 	Field[] fields = dynamicClass.getDeclaredFields();

	// 	for (Field field : fields) {
	// 		field.setAccessible(true); // Make the field accessible (even if it's private)
	// 		String fieldName = field.getName();
	// 		Object fieldValue = field.get(obj); // Get the field value for the instance
	// 	}
	// 	ObjectMapper objectMapper = new ObjectMapper();
	// 	String json = objectMapper.writeValueAsString(obj);
	// }

}
