package com.xorg.wo.proc;

import com.xorg.wo.model.Employee;
import com.xorg.wo.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeHierarchyBuilder {
    private Map<Integer, Employee> reportingStructure;
    private Employee ceo = null;
    private final double managerSalaryLowerThreshold;
    private final int managerSalaryLowerThresholdPercentage;
    private final double managerSalaryUpperThreshold;
    private final int managerSalaryUpperThresholdPercentage;
    private final int employeeMaxReportingLevel;

    public EmployeeHierarchyBuilder() {
        this.managerSalaryLowerThreshold = ConfigLoader.getDouble(Constants.MANAGER_SALARY_LOWER_THRESHOLD, Constants.MANAGER_SALARY_LOWER_THRESHOLD_DEFAULT);
        this.managerSalaryUpperThreshold = ConfigLoader.getDouble(Constants.MANAGER_SALARY_UPPER_THRESHOLD, Constants.MANAGER_SALARY_UPPER_THRESHOLD_DEFAULT);
        this.employeeMaxReportingLevel = ConfigLoader.getInt(Constants.EMPLOYEE_DATA_MAX_REPORTING_LEVEL, Constants.EMPLOYEE_MAX_REPORTING_LEVEL_DEFAULT);
        this.managerSalaryLowerThresholdPercentage = (int) Math.round((this.managerSalaryLowerThreshold - 1)*100);
        this.managerSalaryUpperThresholdPercentage = (int) Math.round((this.managerSalaryUpperThreshold - 1)*100);
    }

    public double getManagerSalaryLowerThreshold() {
        return managerSalaryLowerThreshold;
    }

    public double getManagerSalaryUpperThreshold() {
        return managerSalaryUpperThreshold;
    }

    public int getManagerSalaryLowerThresholdPercentage() {
        return managerSalaryLowerThresholdPercentage;
    }

    public int getManagerSalaryUpperThresholdPercentage() {
        return managerSalaryUpperThresholdPercentage;
    }

    public int getEmployeeMaxReportingLevel() {
        return employeeMaxReportingLevel;
    }

    public void setReportingStructure(Map<Integer, Employee> reportingStructure) {
        this.reportingStructure = reportingStructure;
    }

    public Employee getCEO() {
        return ceo;
    }

    public void setCEO(Employee ceo) {
        this.ceo = ceo;
    }

    public Map<Integer, Employee> getReportingStructure() {
        return reportingStructure;
    }

    public void buildReportingStructure() {
        reportingStructure = new HashMap<>();
        DataLoader dataLoader = new DataLoader();
        List<Employee> employeeList = dataLoader.loadEmployeeDataFile();

        // Populate map with employee objects
        for (Employee employee : employeeList) {
            reportingStructure.put(employee.getId(), employee);
            employee.setReportees(new ArrayList<>()); // Initialize reportees list
        }

        // Construct the hierarchy
        for (Employee employee : employeeList) {
            if (employee.getManagerId() == Constants.DEFAULT_MANAGER_ID) {
                ceo = employee; // CEO is the root node
            } else {
                Employee manager = reportingStructure.get(employee.getManagerId());
                if (manager != null) {
                    manager.addReportee(employee);
                }
            }
        }

        this.setCEO(ceo);
    }

    // Method to print hierarchy
    public void printHierarchy(Employee employee, int level) {
        if (employee == null) return;
        System.out.println(" ".repeat(level * 4) + "|- " + employee.getFirstName() + " " + employee.getLastName());
        for (Employee reportee : employee.getReportees()) {
            printHierarchy(reportee, level + 1);
        }
    }
}