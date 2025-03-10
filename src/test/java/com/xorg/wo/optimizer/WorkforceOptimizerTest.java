package com.xorg.wo.optimizer;

import com.xorg.wo.proc.DataLoader;
import com.xorg.wo.proc.EmployeeHierarchyBuilder;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import com.xorg.wo.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.List;

class WorkforceOptimizerTest {
    private WorkforceOptimizer optimizer;

    @BeforeEach
    void setUp() {
        DataLoader dataLoader = new DataLoader();
        EmployeeHierarchyBuilder employeeHierarchyBuilder = new EmployeeHierarchyBuilder(dataLoader);
        optimizer = new WorkforceOptimizer(employeeHierarchyBuilder);
    }

    /**
     * GIVEN an employee hierarchy exceeding the depth threshold
     * WHEN analyzing deep hierarchy levels
     * THEN it should correctly identify employees beyond level 4
     */
    @Test
    void testFindEmployeesWithDeepHierarchy() {
        // GIVEN: Set up a controlled deep hierarchy
        Employee ceo = new Employee(1, "Alice", "CEO", 200000, -1);
        Employee manager1 = new Employee(2, "Bob", "Manager1", 90000, 1);
        Employee manager2 = new Employee(3, "Charlie", "Manager2", 85000, 2);
        Employee manager3 = new Employee(4, "David", "Manager3", 75000, 3);
        Employee manager4 = new Employee(5, "Eve", "Manager4", 70000, 4);
        Employee employee1 = new Employee(6, "Frank", "Employee1", 65000, 5); // Level 6
        Employee employee2 = new Employee(7, "Grace", "Employee2", 60000, 6); // Level 7

        ceo.setReportees(List.of(manager1));
        manager1.setReportees(List.of(manager2));
        manager2.setReportees(List.of(manager3));
        manager3.setReportees(List.of(manager4));
        manager4.setReportees(List.of(employee1));
        employee1.setReportees(List.of(employee2)); // Deep hierarchy case

        // WHEN: Detecting deep hierarchy
        List<String> deepHierarchyEmployees = optimizer.findEmployeesWithDeepHierarchy(ceo);

        // THEN: Validate employees beyond level 4 are detected
        assertFalse(deepHierarchyEmployees.isEmpty(), "Employees with deep hierarchy should be detected.");

        // Ensure correct employees are reported
        assertTrue(deepHierarchyEmployees.contains("Eve Manager4 (emp id: 5 ) is 1 level/s deeper than allowed."), "Eve should be identified as too deep in the hierarchy.");
        assertTrue(deepHierarchyEmployees.contains("Frank Employee1 (emp id: 6 ) is 2 level/s deeper than allowed."), "Frank should be identified as too deep in the hierarchy.");
        assertTrue(deepHierarchyEmployees.contains("Grace Employee2 (emp id: 7 ) is 3 level/s deeper than allowed."), "Grace should be identified as too deep in the hierarchy.");
    }

    /**
     * GIVEN an employee hierarchy with salary deviations
     * WHEN analyzing manager salary thresholds
     * THEN it should identify managers with salary deviations
     */
    @Test
    void testOutlierManagers() {
        // Setup a controlled salary scenario
        Employee ceo = new Employee(1, "Alice", "CEO", 200000, -1);
        Employee manager = new Employee(2, "Bob", "Manager", 90000, 1);
        Employee employee1 = new Employee(3, "Charlie", "Employee1", 50000, 2);
        Employee employee2 = new Employee(4, "David", "Employee2", 52000, 2);

        Employee manager2 = new Employee(5, "Chris", "Manager", 40000, 1);
        Employee employee3 = new Employee(6, "Charlie", "Employee1", 50000, 5);
        Employee employee4 = new Employee(7, "David", "Employee2", 52000, 5);

        ceo.setReportees(List.of(manager, manager2));
        manager.setReportees(List.of(employee1, employee2));
        manager2.setReportees(List.of(employee3, employee4));

        // Act
        List<String> outliers = optimizer.findManagersWithSalaryDeviations(ceo);

        // Assert
        assertFalse(outliers.isEmpty(), "Managers with outlier salaries should be detected.");
    }

}