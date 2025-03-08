package com.xorg.wo.proc;

import com.xorg.wo.model.Employee;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.xorg.wo.model.UnprocessedEmployeeRecord;
import com.xorg.wo.utils.GlobalConstants;

class DataLoader {
    List<Employee> employeeList;

    public List<Employee> loadEmployeeDataFile() {
        String employeeDataFile = ConfigLoader.getString(GlobalConstants.EMPLOYEE_DATA_FILE, GlobalConstants.EMPLOYEE_DATA_FILE_DEFAULT);
        employeeList = new ArrayList<>();
        if (employeeDataFile != null) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(DataLoader.class.getClassLoader().getResourceAsStream((employeeDataFile)))))) {
                String employeeRecord;
                boolean skipHeader = ConfigLoader.getBoolean(GlobalConstants.EMPLOYEE_DATA_SKIP_HEADER, true);

                while ((employeeRecord = bufferedReader.readLine()) != null) {
                    if (skipHeader) {
                        skipHeader = false;
                        continue; // Skip header row
                    }

                    String[] values = employeeRecord.split(",");
                    String id = values[0].trim();
                    String firstName = values[1].trim();
                    String lastName = values[2].trim();
                    String salary = values[3].trim();
                    String managerId = (values.length > 4) ? values[4].trim() : "-1";

                    UnprocessedEmployeeRecord employee = new UnprocessedEmployeeRecord(id, firstName, lastName, salary, managerId);

                    if (employee.isValid()) {
                        employeeList.add(employee.transform());
                    } else {
                        System.err.println("Skipping invalid record: " + employee); // ToDo: Replace with logger
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // ToDo: Replace with logger
            }
        } else {
            System.err.println("Invalid file path specified, please correct the configuration");
        }
        return employeeList;
    }
}