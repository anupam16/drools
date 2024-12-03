package com.droolsproject.droolspro.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.droolsproject.droolspro.model.Device;
import com.droolsproject.droolspro.model.Rule;
import com.droolsproject.droolspro.repository.RuleRepository;
import com.droolsproject.droolspro.service.RuleEngineService;

import javassist.ClassPool;
import javassist.CtClass;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RuleController {
    private final RuleRepository ruleRepository;
    private final RuleEngineService ruleEngineService;

    @Autowired
    public RuleController(RuleRepository ruleRepository, RuleEngineService ruleEngineService) {
        this.ruleRepository = ruleRepository;
        this.ruleEngineService = ruleEngineService;
    }

    @PostMapping("/addrule")
    public Rule createRule(@RequestBody Rule rule) {
        return ruleRepository.save(rule);        
    }

    @PostMapping("/executeRule")
    public Device executeRules(@RequestBody Device ahu) {
        return ruleEngineService.executeRules(ahu);
    }
    
    
    @PostMapping("/executeRulesbackup")
    public Device executeRulebackup(@RequestBody Map<String, Object> requestData) {
    	  try {
              // Load the dynamically generated class Device
              Class<?> dynamicClass = Class.forName("com.droolsproject.droolspro.model.Device");

              // Create an instance of the dynamically generated class
              Object device = dynamicClass.newInstance();

              // Iterate over the keys (attribute names) in the requestData map
              for (Map.Entry<String, Object> entry : requestData.entrySet()) {
                  String attributeName = entry.getKey();
                  Object attributeValue = entry.getValue();

                  // Use reflection to set attributes in the dynamically generated class
                  String setterMethodName = "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
                  Method setterMethod = dynamicClass.getMethod(setterMethodName, attributeValue.getClass());
                  setterMethod.invoke(device, attributeValue);
              }

              // Ensure that the dynamically generated class implements the Device interface
              if (device instanceof Device) {
                  // Call the rule engine service to execute rules
                  return ruleEngineService.executeRules((Device) device);
              } else {
                  // Handle the case where the dynamically generated class does not implement Device
                  return null; // or throw an exception
              }
          } catch (Exception e) {
              e.printStackTrace();
              return null;
          }
    }
    
    @PostMapping("/executeRules")
    public Object executeRule(@RequestBody Map<String, Object> requestData) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass dynamicClass = pool.get("com.droolsproject.droolspro.model.Device");
            DynamicClassLoaderLocal classLoader = new DynamicClassLoaderLocal(Thread.currentThread().getContextClassLoader());
            Class<?> cClass = classLoader.loadClass("com.droolsproject.droolspro.model.Device", dynamicClass.toBytecode());
            Object deviceInstance = cClass.newInstance();

            Field[] fields = cClass.getDeclaredFields();

            for (Field field : fields) {
                String fieldName = field.getName();

                // Check if the field name exists in the requestData map
                if (requestData.containsKey(fieldName)) {
                    Object value = requestData.get(fieldName);
                    // Set the field's value using reflection
                    field.setAccessible(true); // Allow access to private fields
                    field.set(deviceInstance, value);
                }
            }
         return executeRulesL(deviceInstance, classLoader);
        } catch (Exception e) {
        	System.err.println(" "+ e);
            // Log the error and handle it gracefully
            return null; // or return an error response
        }
    }
    
    public Object executeRulesL(Object device, DynamicClassLoaderLocal classLoader) {
        // Retrieve rules from the database
        List<Rule> rules = ruleRepository.findAll();

        // Generate DRL content and write to a file
        String drlContent = generateDrlContent(rules);
        String drlFilePath = "c:\\temp\\generated-rule.drl";
        
		
		try (PrintWriter writer = new PrintWriter(drlFilePath)) {
		  writer.write(drlContent); } catch (IOException e) { e.printStackTrace();
		 return null;
		 }
		 
        // Execute rules using Drools\
        Thread.currentThread().setContextClassLoader(classLoader);
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.write(ResourceFactory.newFileResource(new File(drlFilePath)));
        
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();

        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Rule compilation errors: " + kieBuilder.getResults());
        }

        ReleaseId releaseId = kieServices.newReleaseId("org.default", "artifact", "1.0.0");
        KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        KieSession kieSession = kieContainer.newKieSession();

        // Insert fact and fire rules
        kieSession.insert(device);
        kieSession.fireAllRules();
        return device;
    }

    private String generateDrlContent(List<Rule> rules) {
    	
        StringBuilder drlContent = new StringBuilder();
        drlContent.append("package KieRule;\n\n ")
                  .append("import com.droolsproject.droolspro.model.Device;\n");       
        for (Rule rule : rules) {
            drlContent.append("rule \"").append(rule.getRuleName()).append("\"\n")
                      .append("when\n")
                      .append("device : Device(")
                      .append(rule.getConditions()).append(")\n")
                      .append("then\n")
                      .append("device.setOutput(\"")
                      .append(rule.getAction()).append("\");\n")
                      .append("update(device);\nend;\n\n");
        }
        return drlContent.toString();
    }


    private Object convertToType(CtClass targetType, Object value) {
        // Implement conversion logic as needed
        if (targetType.equals(CtClass.doubleType)) {
            return Double.parseDouble(value.toString());
        } else if (targetType.equals(CtClass.intType)) {
            return Integer.parseInt(value.toString());
        }
        // Add more type conversions as necessary

        return value;
    }

    class DynamicClassLoaderLocal extends ClassLoader {
        public DynamicClassLoaderLocal(ClassLoader parent) {
            super(parent);
        }

        public Class<?> loadClass(String name, byte[] bytecode) {
            return defineClass(name, bytecode, 0, bytecode.length);
        }
    }

    
    @GetMapping("getrules")
    public ResponseEntity<List<Rule>> getRules() {
    	List<Rule> ruleList = ruleRepository.findAll();
    	return new ResponseEntity<List<Rule>>(ruleList, HttpStatus.OK);
    }
    
    @PutMapping("/updaterule")
	public ResponseEntity<Rule> updateRule(@RequestBody Rule rule) {
		Rule stationResponse = ruleRepository.saveAndFlush(rule);
	return new ResponseEntity<Rule>(stationResponse, HttpStatus.OK);	
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteRule(@PathVariable("id") Long id) {
		ruleRepository.deleteById(id);
	return new ResponseEntity<String>("Success", HttpStatus.ACCEPTED);	
	}	
}

