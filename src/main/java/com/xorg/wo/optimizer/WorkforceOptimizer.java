package com.xorg.wo.optimizer;

import com.xorg.wo.model.Employee;
import com.xorg.wo.proc.EmployeeHierarchyBuilder;

import java.util.ArrayList;
import java.util.List;

public class WorkforceOptimizer {
    private List<Employee> reportingStructure;
    EmployeeHierarchyBuilder employeeHierarchyBuilder = new EmployeeHierarchyBuilder();

    private void prepareEmployeeData() {
        employeeHierarchyBuilder.buildReportingStructure();
        employeeHierarchyBuilder.printHierarchy(employeeHierarchyBuilder.getCEO(), 0);
    }

    private List<String> findOutlierManagersBySalary(Employee ceo) {
        List<String> result = new ArrayList<>();
        recursivelyFindOutlierManagersBySalary(ceo, result);
        return result;
    }

    private void recursivelyFindOutlierManagersBySalary(Employee employee, List<String> outliers) {
        if (employee == null || employee.getReportees().isEmpty()) {
            return;
        }

        double totalSalary = 0;
        int reporteeCount = employee.getReportees().size();

        // Calculate total salary of reportees
        for (Employee reportee : employee.getReportees()) {
            totalSalary += reportee.getSalary();
        }

        // Compute average salary of reportees
        double avgSalary = totalSalary / reporteeCount;

        // Compute thresholds
        double lowerThreshold = avgSalary * employeeHierarchyBuilder.getManagerMinAvgSalaryThreshold();
        double upperThreshold = avgSalary * employeeHierarchyBuilder.getManagerMaxAvgSalaryThreshold();

        // Check if manager's salary is an outlier
        // System.out.println(employee.getId() + ", " + employee.getSalary() + " lower: " + lowerThreshold + ", upper: " + upperThreshold + ", " + avgSalary);
        if (employee.getSalary() < lowerThreshold) {
            double difference = lowerThreshold - employee.getSalary();
            double percentageDifference = (difference / lowerThreshold) * 100;

            outliers.add(employee.getFirstName() + " " + employee.getLastName() +
                    " earns " + String.format("%.2f", percentageDifference) +
                    "% less than " + employeeHierarchyBuilder.getManagerMinAvgSalaryThresholdPercentage() + "% of the average salary of reportees.");
        } else if (employee.getSalary() > upperThreshold) {
            double difference = employee.getSalary() - upperThreshold;
            double percentageDifference = (difference / upperThreshold) * 100;

            outliers.add(employee.getFirstName() + " " + employee.getLastName() +
                    " earns " + String.format("%.2f", percentageDifference) +
                    "% more than " + employeeHierarchyBuilder.getManagerMaxAvgSalaryThresholdPercentage() + "% of the average salary of reportees.");
        }

        // Recursively check for all reportees
        for (Employee reportee : employee.getReportees()) {
            recursivelyFindOutlierManagersBySalary(reportee, outliers);
        }
    }

    private void recursivelyFindEmployeesWithDeepHierarchy(Employee employee, int level, List<String> result) {
        if (employee == null) {
            return;
        }

        // If reporting level is greater than 4 add the employee to result
        if (level > employeeHierarchyBuilder.getEmployeeMaxReportingLevel()) {
            int extraLevels = level - employeeHierarchyBuilder.getEmployeeMaxReportingLevel(); // Find how much deeper the hierarchy is
            result.add(employee.getFirstName() + " " + employee.getLastName() + " (employee id: " + employee.getId() + " ) is " + extraLevels + " level/s deeper than allowed.");
        }

        // Recursively explore all children increasing the reporting level
        for (Employee reportee: employee.getReportees()) {
            recursivelyFindEmployeesWithDeepHierarchy(reportee, level + 1, result);
        }
    }

    private List<String> findEmployeesWithDeepHierarchy(Employee ceo) {
        List<String> result = new ArrayList<>();
        recursivelyFindEmployeesWithDeepHierarchy(ceo, 1, result);
        return result;
    }

    public void optimizeWorkforce() {
        prepareEmployeeData();
        Employee ceo = employeeHierarchyBuilder.getCEO();

        if (ceo != null) {
            List<String> outlierManagersBySalary = findOutlierManagersBySalary(ceo);

            List<String> employeesWithDeepHierarchy = findEmployeesWithDeepHierarchy(ceo);
            System.out.println(outlierManagersBySalary);
            System.out.print(employeesWithDeepHierarchy);
        } else {
            // ToDo: Report error to end user
            System.out.println("CEO is not found!!");
        }

    }
}
