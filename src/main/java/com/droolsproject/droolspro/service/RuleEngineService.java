package com.droolsproject.droolspro.service;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.TimedRuleExecutionOption;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.droolsproject.droolspro.model.AHU;
import com.droolsproject.droolspro.model.Device;
import com.droolsproject.droolspro.model.Rule;
import com.droolsproject.droolspro.repository.RuleRepository;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class RuleEngineService {
    private final RuleRepository ruleRepository;
    private KieServices kieServices = KieServices.Factory.get();
    
    @Autowired
    public RuleEngineService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }
    
    public Device executeRules(Device device) {
        // Retrieve rules from the database
        List<Rule> rules = ruleRepository.findAll();

        // Generate DRL content and write to a file
        String drlContent = generateDrlContent(rules);
        String drlFilePath = "c:\\temp\\generated-rule.drl";
        
		
		try (PrintWriter writer = new PrintWriter(drlFilePath)) {
		  writer.write(drlContent); } catch (IOException e) { e.printStackTrace();
		 return null;
		 }
		 
        // Execute rules using Drools
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
        return
        		drlContent.toString();
    }
    
    
    public List<Device> executeRulesAHU(List<Device> ahuList) {
        // Retrieve rules from the database
        List<Rule> rules = ruleRepository.findAll();
        // Generate DRL content and write to a file
        String drlContent = generateDrlContentAHU(rules);
        String drlFilePath = "c:\\temp\\generated-rule.drl";
		
		try (PrintWriter writer = new PrintWriter(drlFilePath)) {
		  writer.write(drlContent); } catch (IOException e) { e.printStackTrace();
		 return null;
		 }
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
        List<Device> ahuWithSatisfiedRules = new ArrayList<>();

        for (Device ahu : ahuList) {
            kieSession.insert(ahu);
            kieSession.fireAllRules();
            if (ahu.getOutput() == null) {
            	ahu.setOutput("OK");
            	ahuWithSatisfiedRules.add(ahu);
            } else {
            	ahuWithSatisfiedRules.add(ahu);
            }           
        }
        
        return ahuWithSatisfiedRules;
    }

    private String generateDrlContentAHU(List<Rule> rules) {
    	
        StringBuilder drlContent = new StringBuilder();
        drlContent.append("package KieRule;\n\n ")
                  .append("import com.droolsproject.droolspro.model.Device;\n");       
        for (Rule rule : rules) {
            drlContent.append("rule \"").append(rule.getRuleName()).append("\"\n")
                      .append("when\n")
                      .append("ahu : Device(")
                      .append(rule.getConditions()).append(")\n")
                      .append("then\n")
                      .append("ahu.setOutput(\"")
                      .append(rule.getAction()).append("\");\n")
                      .append("update(ahu);\nend;\n\n");
        }
        return drlContent.toString();
    }
}
