package com.xorg.wo.model;

import com.xorg.wo.optimizer.WorkforceOptimizer;
import com.xorg.wo.proc.ConfigLoader;
import com.xorg.wo.utils.Constants;

import java.lang.reflect.Field;
import java.util.logging.Logger;

public class UnprocessedEmployeeRecord {
    private static final Logger logger = Logger.getLogger( UnprocessedEmployeeRecord.class.getName() );
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String salary;
    private final String managerId;

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
            String requiredFieldsStr = ConfigLoader.getString(Constants.EMPLOYEE_DATA_REQUIRED_FIELDS, Constants.EMPLOYEE_DATA_REQUIRED_FIELDS_DEFAULT);
            if (requiredFieldsStr != null) {
                REQUIRED_FIELDS = requiredFieldsStr.split(",");
            }
            // Using Reflection to validate the required fields
            for (String fieldName : REQUIRED_FIELDS) {
                Field field = this.getClass().getDeclaredField(fieldName);
                field.setAccessible(true); // Access private fields
                Object value = field.get(this);

                if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                    return false; // Invalid if any required field is missing or empty
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.warning(String.format(Constants.INVALID_EMPLOYEE_ATTRIBUTE_PROVIDED_IN_CONFIGURATION, e.getLocalizedMessage()));
            return false;
        }
        return true;
    }

    public Employee transform() {
        return new Employee(Integer.parseInt(id), firstName, lastName,
                (salary == null || salary.isEmpty()) ? Constants.EMPLOYEE_DEFAULT_SALARY : Double.parseDouble(salary),
                (managerId == null || managerId.isEmpty()) ? Constants.DEFAULT_MANAGER_ID : Integer.parseInt(managerId));
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
