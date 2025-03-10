package com.xorg.wo.optimizer;

import com.xorg.wo.model.Employee;
import com.xorg.wo.proc.EmployeeHierarchyBuilder;
import com.xorg.wo.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WorkforceOptimizer {
    private final EmployeeHierarchyBuilder employeeHierarchyBuilder;
    private static final Logger logger = Logger.getLogger( WorkforceOptimizer.class.getName() );

    public WorkforceOptimizer(EmployeeHierarchyBuilder employeeHierarchyBuilder) {
        this.employeeHierarchyBuilder = employeeHierarchyBuilder;
    }

    private void prepareEmployeeData() {
        employeeHierarchyBuilder.buildReportingStructure();

        System.out.println(Constants.CONSOLE_LINE_SEPARATOR);
        System.out.println("Employee Hierarchy");
        System.out.println(Constants.CONSOLE_LINE_SEPARATOR);
        employeeHierarchyBuilder.printHierarchy(employeeHierarchyBuilder.getCEO(), 0);
        System.out.println(Constants.CONSOLE_LINE_SEPARATOR);
    }

    private void recursivelyFindManagersWithSalaryDeviations(Employee employee, List<String> outliers) {
        if (employee == null || employee.getReportees() == null || (employee.getReportees() != null && employee.getReportees().isEmpty())) {
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
        double lowerThreshold = avgSalary * employeeHierarchyBuilder.getManagerSalaryLowerThreshold();
        double upperThreshold = avgSalary * employeeHierarchyBuilder.getManagerSalaryUpperThreshold();

        // Check if manager's salary is an outlier
        // System.out.println(employee.getId() + ", " + employee.getSalary() + " lower: " + lowerThreshold + ", upper: " + upperThreshold + ", " + avgSalary);
        if (employee.getSalary() < lowerThreshold) {
            double difference = lowerThreshold - employee.getSalary();
            double percentageDifference = (difference / lowerThreshold) * 100;

            outliers.add(employee.getFirstName() + " " + employee.getLastName() + " (emp id: " + employee.getId() + ")" +
                    " earns " + String.format("%.2f", percentageDifference) +
                    "% less than " + employeeHierarchyBuilder.getManagerSalaryLowerThresholdPercentage() + "% of the average salary of reportees.");
        } else if (employee.getSalary() > upperThreshold) {
            double difference = employee.getSalary() - upperThreshold;
            double percentageDifference = (difference / upperThreshold) * 100;

            outliers.add(employee.getFirstName() + " " + employee.getLastName() + " (emp id: " + employee.getId() + ")" +
                    " earns " + String.format("%.2f", percentageDifference) +
                    "% more than " + employeeHierarchyBuilder.getManagerSalaryUpperThresholdPercentage() + "% of the average salary of reportees.");
        }

        // Recursively check for all reportees
        if (employee.getReportees() != null) {
            for (Employee reportee : employee.getReportees()) {
                recursivelyFindManagersWithSalaryDeviations(reportee, outliers);
            }
        }
    }

    public List<String> findManagersWithSalaryDeviations(Employee ceo) {
        List<String> result = new ArrayList<>();
        recursivelyFindManagersWithSalaryDeviations(ceo, result);
        return result;
    }

    private void recursivelyFindEmployeesWithDeepHierarchy(Employee employee, int level, List<String> result) {
        if (employee == null) {
            return;
        }

        // If reporting level is greater than 4 add the employee to result
        if (level > employeeHierarchyBuilder.getEmployeeMaxReportingLevel()) {
            int extraLevels = level - employeeHierarchyBuilder.getEmployeeMaxReportingLevel(); // Find how much deeper the hierarchy is
            result.add(employee.getFirstName() + " " + employee.getLastName() + " (emp id: " + employee.getId() + " ) is " + extraLevels + " level/s deeper than allowed.");
        }

        // Recursively explore all children increasing the reporting level
        if (employee.getReportees() != null) {
            for (Employee reportee: employee.getReportees()) {
                recursivelyFindEmployeesWithDeepHierarchy(reportee, level + 1, result);
            }
        }
    }

    public List<String> findEmployeesWithDeepHierarchy(Employee ceo) {
        List<String> result = new ArrayList<>();
        recursivelyFindEmployeesWithDeepHierarchy(ceo, 1, result);
        return result;
    }

    public void optimizeWorkforce() {
        prepareEmployeeData();
        Employee ceo = employeeHierarchyBuilder.getCEO();

        if (ceo != null) {
            // Find and print managers with salary deviations
            List<String> managersWithSalaryDeviations = findManagersWithSalaryDeviations(ceo);
            printOptimizations(Constants.OPTIMIZATION_CATEGORY_MANAGERS_WITH_SALARY_DEVIATIONS, managersWithSalaryDeviations);

            // Find and print employees with deep hierarchy
            List<String> employeesWithDeepHierarchy = findEmployeesWithDeepHierarchy(ceo);
            printOptimizations(Constants.OPTIMIZATION_CATEGORY_EMPLOYEES_WITH_DEEP_HIERARCHY, employeesWithDeepHierarchy);
        } else {
            logger.severe(Constants.CEO_NOT_FOUND);
            System.exit(Constants.EXIT_CODE_CEO_NOT_FOUND);
        }

    }

    public void printOptimizations(String category, List<String> optimizations) {
        System.out.println("Optimizations for " + category + " have been identified as follows");
        System.out.println(Constants.CONSOLE_LINE_SEPARATOR);
        for (String optimization: optimizations) {
            System.out.println(optimization);
        }
        System.out.println(Constants.CONSOLE_LINE_SEPARATOR);
        System.out.println("Total optimizations identified for " + category + " are " + optimizations.size());
        System.out.println(Constants.CONSOLE_LINE_SEPARATOR);
    }
}
