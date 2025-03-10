package com.xorg.wo.proc;

import static org.junit.jupiter.api.Assertions.*;

import com.xorg.wo.model.Employee;
import com.xorg.wo.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeHierarchyBuilderTest {

    private EmployeeHierarchyBuilder builder;
    private DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        dataLoader = mock(DataLoader.class);
        builder = new EmployeeHierarchyBuilder(dataLoader);
    }

    @Test
    void buildReportingStructure_validData_buildsHierarchy() {
        // Given
        List<Employee> employeeList = new ArrayList<>();
        Employee ceo = new Employee(1, "John", "Doe", 100000, Constants.DEFAULT_MANAGER_ID);
        Employee manager = new Employee(2, "Jane", "Smith", 80000, 1);
        Employee employee = new Employee(3, "Alice", "Johnson", 60000, 2);
        employeeList.add(ceo);
        employeeList.add(manager);
        employeeList.add(employee);

        when(dataLoader.loadEmployeeDataFile()).thenReturn(employeeList);

        // When
        builder.buildReportingStructure();

        // Then
        Employee actualCeo = builder.getCEO();
        assertNotNull(actualCeo);
        assertEquals(ceo, actualCeo);
        assertEquals(1, actualCeo.getReportees().size());
        assertEquals(manager, actualCeo.getReportees().getFirst());
        assertEquals(1, manager.getReportees().size());
        assertEquals(employee, manager.getReportees().getFirst());
        Map<Integer, Employee> reportingStructure = builder.getReportingStructure();
        assertNotNull(reportingStructure);
        assertEquals(3, reportingStructure.size());
        assertEquals(ceo, reportingStructure.get(1));
        assertEquals(manager, reportingStructure.get(2));
        assertEquals(employee, reportingStructure.get(3));
    }

    @Test
    void buildReportingStructure_emptyData_ceoIsNull() {
        // Given
        List<Employee> employeeList = new ArrayList<>();
        when(dataLoader.loadEmployeeDataFile()).thenReturn(employeeList);

        // When
        builder.buildReportingStructure();

        // Then
        assertNull(builder.getCEO());
        assertNotNull(builder.getReportingStructure());
        assertTrue(builder.getReportingStructure().isEmpty());
    }


    @Test
    void buildReportingStructure_managerNotFound_reporteeNotAdded() {
        // Given
        List<Employee> employeeList = new ArrayList<>();
        Employee ceo = new Employee(1, "John", "Doe", 100000, Constants.DEFAULT_MANAGER_ID);
        Employee employee = new Employee(3, "Alice", "Johnson", 60000, 2); // Manager with ID 2 does not exist
        employeeList.add(ceo);
        employeeList.add(employee);

        when(dataLoader.loadEmployeeDataFile()).thenReturn(employeeList);

        // When
        builder.buildReportingStructure();

        // Then
        Employee actualCeo = builder.getCEO();
        assertNotNull(actualCeo);
        assertEquals(ceo, actualCeo);
        assertTrue(actualCeo.getReportees().isEmpty());
        Map<Integer, Employee> reportingStructure = builder.getReportingStructure();
        assertNotNull(reportingStructure);
        assertEquals(2, reportingStructure.size());
        assertEquals(ceo, reportingStructure.get(1));
        assertEquals(employee, reportingStructure.get(3));
    }

    @Test
    void constructor_configLoaded_thresholdsSet() {
        // Given
        try (MockedStatic<ConfigLoader> utilities = mockStatic(ConfigLoader.class)) {
            utilities.when(() -> ConfigLoader.getDouble(Constants.MANAGER_SALARY_LOWER_THRESHOLD, Constants.MANAGER_SALARY_LOWER_THRESHOLD_DEFAULT)).thenReturn(0.8);
            utilities.when(() -> ConfigLoader.getDouble(Constants.MANAGER_SALARY_UPPER_THRESHOLD, Constants.MANAGER_SALARY_UPPER_THRESHOLD_DEFAULT)).thenReturn(1.2);
            utilities.when(() -> ConfigLoader.getInt(Constants.EMPLOYEE_DATA_MAX_REPORTING_LEVEL, Constants.EMPLOYEE_MAX_REPORTING_LEVEL_DEFAULT)).thenReturn(5);

            // When
            EmployeeHierarchyBuilder builder = new EmployeeHierarchyBuilder(mock(DataLoader.class)); // pass a mock DataLoader

            // Then
            assertEquals(0.8, builder.getManagerSalaryLowerThreshold());
            assertEquals(1.2, builder.getManagerSalaryUpperThreshold());
            assertEquals(-20, builder.getManagerSalaryLowerThresholdPercentage());
            assertEquals(20, builder.getManagerSalaryUpperThresholdPercentage());
            assertEquals(5, builder.getEmployeeMaxReportingLevel());
        }
    }
}