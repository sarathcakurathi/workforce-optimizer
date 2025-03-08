package com.xorg.wo.proc;

import com.xorg.wo.model.Employee;
import com.xorg.wo.utils.GlobalConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeHierarchyBuilder {
    private Map<Integer, Employee> reportingStructure;
    private Employee ceo = null;
    private final double managerMinAvgSalaryThreshold;
    private final int managerMinAvgSalaryThresholdPercentage;
    private final double managerMaxAvgSalaryThreshold;
    private final int managerMaxAvgSalaryThresholdPercentage;
    private final int employeeMaxReportingLevel;

    public EmployeeHierarchyBuilder() {
        this.managerMinAvgSalaryThreshold = ConfigLoader.getDouble(GlobalConstants.MANAGER_MIN_AVG_SALARY_THRESHOLD, GlobalConstants.MANAGER_MIN_AVG_SALARY_THRESHOLD_DEFAULT);
        this.managerMaxAvgSalaryThreshold = ConfigLoader.getDouble(GlobalConstants.MANAGER_MAX_AVG_SALARY_THRESHOLD, GlobalConstants.MANAGER_MAX_AVG_SALARY_THRESHOLD_DEFAULT);
        this.employeeMaxReportingLevel = ConfigLoader.getInt(GlobalConstants.EMPLOYEE_DATA_MAX_REPORTING_LEVEL, GlobalConstants.EMPLOYEE_MAX_REPORTING_LEVEL_DEFAULT);
        this.managerMinAvgSalaryThresholdPercentage = (int) Math.round((this.managerMinAvgSalaryThreshold - 1)*100);
        this.managerMaxAvgSalaryThresholdPercentage = (int) Math.round((this.managerMaxAvgSalaryThreshold - 1)*100);
    }

    public double getManagerMinAvgSalaryThreshold() {
        return managerMinAvgSalaryThreshold;
    }

    public double getManagerMaxAvgSalaryThreshold() {
        return managerMaxAvgSalaryThreshold;
    }

    public int getManagerMinAvgSalaryThresholdPercentage() {
        return managerMinAvgSalaryThresholdPercentage;
    }

    public int getManagerMaxAvgSalaryThresholdPercentage() {
        return managerMaxAvgSalaryThresholdPercentage;
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
            if (employee.getManagerId() == GlobalConstants.DEFAULT_MANAGER_ID) {
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