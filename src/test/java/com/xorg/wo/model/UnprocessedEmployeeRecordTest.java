package com.xorg.wo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnprocessedEmployeeRecordTest {

    /**
     * GIVEN a valid UnprocessedEmployeeRecord
     * WHEN transformed into Employee
     * THEN it should correctly populate attributes
     */
    @Test
    void testUnprocessedEmployeeTransformation() {
        UnprocessedEmployeeRecord record = new UnprocessedEmployeeRecord("1", "John", "Doe", "50000", "-1");
        Employee employee = record.transform();

        assertEquals(1, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals(50000, employee.getSalary(), 0.01);
        assertEquals(-1, employee.getManagerId());
    }

    /**
     * GIVEN a missing required field in UnprocessedEmployeeRecord
     * WHEN validated
     * THEN it should be marked as invalid
     */
    @Test
    void testUnprocessedEmployeeIsInvalid() {
        // id is invalid
        UnprocessedEmployeeRecord record1 = new UnprocessedEmployeeRecord("", "Jane", "Smith", "10000", "2");
        assertFalse(record1.isValid());

        // salary is invalid
        UnprocessedEmployeeRecord record2 = new UnprocessedEmployeeRecord("123", "Jane", "Smith", "", "2");
        assertFalse(record2.isValid());
    }


    /**
     * GIVEN a valid UnprocessedEmployeeRecord
     * WHEN validated
     * THEN it should be marked as valid
     */
    @Test
    void testUnprocessedEmployeeIsValid() {
        UnprocessedEmployeeRecord record1 = new UnprocessedEmployeeRecord("1", "Jane", "Smith", "10000", "2");
        assertTrue(record1.isValid());
    }
}