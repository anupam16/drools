package com.droolsproject.droolspro.config.utility;

import java.lang.reflect.Field;

import javassist.*;

public class DynamicFieldAddition {

	    public static void main(String[] args) {
	        try {
	            // Create a new class
	            ClassPool pool = ClassPool.getDefault();
	            CtClass dynamicClass = pool.makeClass("DynamicClass2");

	            // Add fields to the class
	            CtField field1 = new CtField(CtClass.doubleType, "sineWave", dynamicClass);
	            CtField field2 = new CtField(CtClass.doubleType, "daTemp", dynamicClass);

	            dynamicClass.addField(field1);
	            dynamicClass.addField(field2);

	            // Create getter and setter methods for the fields
	            CtMethod getter1 = CtNewMethod.getter("getSineWave2", field1);
	            CtMethod setter1 = CtNewMethod.setter("setSineWave2", field1);
	            dynamicClass.addMethod(getter1);
	            dynamicClass.addMethod(setter1);

	            CtMethod getter2 = CtNewMethod.getter("getDaTemp", field2);
	            CtMethod setter2 = CtNewMethod.setter("setDaTemp", field2);
	            dynamicClass.addMethod(getter2);
	            dynamicClass.addMethod(setter2);

	            // Save the class to a .class file
	            dynamicClass.writeFile("target/classes");

	            System.out.println("DynamicClass generated successfully!");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
