package com.xorg.wo.utils;

public final class GlobalConstants {

    private GlobalConstants() {
        // Private constructor to prevent instantiation
    }

    // File-related constants
    public static final String CONFIG_FILE = "config.properties";

    // Configuration properties
    public static final String EMPLOYEE_DATA_FILE="xorg.wo.emp.datafile";
    public static final String EMPLOYEE_DATA_SKIP_HEADER="xorg.wo.emp.skipheader";
    public static final String EMPLOYEE_DATA_REQUIRED_FIELDS="xorg.wo.emp.required.fields";
    public static final String EMPLOYEE_DATA_MAX_REPORTING_LEVEL="xorg.wo.emp.max.reporting.level";
    public static final String MANAGER_MIN_AVG_SALARY_THRESHOLD="xorg.wo.manager.min.avg.salary";
    public static final String MANAGER_MAX_AVG_SALARY_THRESHOLD="xorg.wo.manager.max.avg.salary";

    // Validation messages
    public static final String ERROR_INVALID_EMPLOYEE = "Invalid employee record found: ";
    public static final String ERROR_FILE_NOT_FOUND = "Error: File not found - ";

    // Default values
    public static final int DEFAULT_MANAGER_ID = -1;

    // Employee/Manager defaults
    public static final double MANAGER_MIN_AVG_SALARY_THRESHOLD_DEFAULT = 1.2;
    public static final double MANAGER_MAX_AVG_SALARY_THRESHOLD_DEFAULT = 1.5;
    public static final int EMPLOYEE_MAX_REPORTING_LEVEL_DEFAULT = 4;
    public static final String EMPLOYEE_DATA_REQUIRED_FIELDS_DEFAULT="id,salary";
    public static final String EMPLOYEE_DATA_FILE_DEFAULT="data/xorg-employees.csv";

    // Logging messages
    public static final String CEO_NOT_FOUND = "Error: CEO not found in the hierarchy!";
}
