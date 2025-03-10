package com.xorg.wo.proc;

import com.xorg.wo.model.Employee;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.xorg.wo.model.UnprocessedEmployeeRecord;
import com.xorg.wo.utils.Constants;

public class DataLoader {
    List<Employee> employeeList;
    private static final Logger logger = Logger.getLogger( DataLoader.class.getName() );

    public List<Employee> loadEmployeeDataFile() {
        String employeeDataFile = ConfigLoader.getString(Constants.EMPLOYEE_DATA_FILE, Constants.EMPLOYEE_DATA_FILE_DEFAULT);
        employeeList = new ArrayList<>();
        if (employeeDataFile != null) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(DataLoader.class.getClassLoader().getResourceAsStream((employeeDataFile)))))) {
                String employeeRecord;
                boolean skipHeader = ConfigLoader.getBoolean(Constants.EMPLOYEE_DATA_SKIP_HEADER, true);

                while ((employeeRecord = bufferedReader.readLine()) != null) {
                    if (skipHeader) {
                        skipHeader = false;
                        continue; // Skip header row
                    }

                    UnprocessedEmployeeRecord employee = getUnprocessedEmployeeRecord(employeeRecord);

                    if (employee.isValid()) {
                        employeeList.add(employee.transform());
                    } else {
                        logger.warning(Constants.SKIP_INVALID_EMPLOYEE_RECORD + employee);
                    }
                }
            } catch (IOException | NullPointerException e) {
                logger.severe(String.format(Constants.ERROR_FILE_NOT_FOUND, employeeDataFile, e.getLocalizedMessage()));
                System.exit(Constants.EXIT_CODE_FILE_NOT_FOUND);
            }
        } else {
            logger.severe(Constants.INVALID_FILE_PATH_IN_CONFIGURATION);
            System.exit(Constants.EXIT_CODE_INVALID_FILE_PATH);
        }
        return employeeList;
    }

    private UnprocessedEmployeeRecord getUnprocessedEmployeeRecord(String employeeRecord) {
        String[] values = employeeRecord.split(",");
        String id = values[0].trim();
        String firstName = values[1].trim();
        String lastName = values[2].trim();
        String salary = values[3].trim();
        String managerId = (values.length > 4) ? values[4].trim() : "" + Constants.DEFAULT_MANAGER_ID;

        return new UnprocessedEmployeeRecord(id, firstName, lastName, salary, managerId);
    }
}