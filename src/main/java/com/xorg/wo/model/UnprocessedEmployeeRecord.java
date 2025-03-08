package com.xorg.wo.model;

import com.opencsv.bean.CsvBindByName;
import com.xorg.wo.proc.ConfigLoader;
import com.xorg.wo.utils.GlobalConstants;

import java.lang.reflect.Field;

public class UnprocessedEmployeeRecord {
    @CsvBindByName(column = "id")
    private String id;

    @CsvBindByName(column = "firstName")
    private String firstName;

    @CsvBindByName(column = "lastName")
    private String lastName;

    @CsvBindByName(column = "salary")
    private String salary;

    @CsvBindByName(column = "managerId")
    private String managerId;

    // Constructor for OpenCSV
    public UnprocessedEmployeeRecord() {}

    public UnprocessedEmployeeRecord(String id, String firstName, String lastName, String salary, String managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    public boolean isValid() {
        String[] REQUIRED_FIELDS = new String[0];
        try {
            String requiredFieldsStr = ConfigLoader.getString(GlobalConstants.EMPLOYEE_DATA_REQUIRED_FIELDS, GlobalConstants.EMPLOYEE_DATA_REQUIRED_FIELDS_DEFAULT);
            if (requiredFieldsStr != null) {
                REQUIRED_FIELDS = requiredFieldsStr.split(",");
            }
            for (String fieldName : REQUIRED_FIELDS) {
                Field field = this.getClass().getDeclaredField(fieldName);
                field.setAccessible(true); // Access private fields
                Object value = field.get(this);

                if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                    return false; // Invalid if any required field is missing or empty
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace(); //ToDo: replace with proper logger
            return false;
        }
        return true;
    }

    public Employee transform() {
        return new Employee(Integer.parseInt(id), firstName, lastName, Double.parseDouble(salary),
                (managerId == null || managerId.isEmpty()) ? GlobalConstants.DEFAULT_MANAGER_ID : Integer.parseInt(managerId));
    }

    @Override
    public String toString() {
        return "UnprocessedEmployeeRecord{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary='" + salary + '\'' +
                ", managerId='" + managerId + '\'' +
                '}';
    }
}
