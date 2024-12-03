package com.droolsproject.droolspro.config.utility;

public class Test {
    private String name;
    private Class<?> type;

    public Test(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    // Helper method to set the class type from a string representation
    public void setTypeFromString(String typeName) throws ClassNotFoundException {
        this.type = Class.forName(typeName);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        // Example usage:
    	Test fieldDefinition = new Test("fieldName", null);
        String typeName = "java.lang.Double"; // Example class name as a string

        // Set the class type from the string representation
        fieldDefinition.setTypeFromString(typeName);

        // Now the type field contains the class instance for java.lang.String
        Class<?> fieldType = fieldDefinition.getType();
        System.out.println("Field Type: " + fieldType.getName()); // Output: java.lang.String
    }
}
