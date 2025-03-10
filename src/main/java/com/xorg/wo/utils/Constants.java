package com.xorg.wo.utils;

public final class Constants {

    private Constants() {
        // Private constructor to prevent instantiation
    }

    // File-related constants
    public static final String CONFIG_FILE = "config.properties";

    // Configuration properties
    public static final String EMPLOYEE_DATA_FILE="xorg.wo.emp.datafile";
    public static final String EMPLOYEE_DATA_SKIP_HEADER="xorg.wo.emp.skipheader";
    public static final String EMPLOYEE_DATA_REQUIRED_FIELDS="xorg.wo.emp.required.fields";
    public static final String EMPLOYEE_DATA_MAX_REPORTING_LEVEL="xorg.wo.emp.max.reporting.level";
    public static final String MANAGER_SALARY_LOWER_THRESHOLD="xorg.wo.manager.salary.lower.threshold";
    public static final String MANAGER_SALARY_UPPER_THRESHOLD="xorg.wo.manager.salary.upper.threshold";

    // Employee/Manager defaults
    public static final int DEFAULT_MANAGER_ID = -1;
    public static final double EMPLOYEE_DEFAULT_SALARY = 50000.0;
    public static final double MANAGER_SALARY_LOWER_THRESHOLD_DEFAULT = 1.2;
    public static final double MANAGER_SALARY_UPPER_THRESHOLD_DEFAULT = 1.5;
    public static final int EMPLOYEE_MAX_REPORTING_LEVEL_DEFAULT = 4;
    public static final String EMPLOYEE_DATA_REQUIRED_FIELDS_DEFAULT = "id,salary";
    public static final String EMPLOYEE_DATA_FILE_DEFAULT = "data/xorg-employees.csv";

    // Logging messages
    public static final String ERROR_FILE_NOT_FOUND = "Error: File not found. Looking for file: %s, Reason: %s";
    public static final String SKIP_INVALID_EMPLOYEE_RECORD = "Skipping invalid employee record - ";
    public static final String INVALID_FILE_PATH_IN_CONFIGURATION = "Invalid file path specified, please correct the configuration";
    public static final String ERROR_PROCESSING_EMPLOYEE_RECORD_INTEGER_DATA="Error parsing integer for key: %s. Using default.";
    public static final String ERROR_PROCESSING_EMPLOYEE_RECORD_DOUBLE_DATA="Error parsing double for key: %s. Using default.";
    public static final String OPTIMIZATION_CATEGORY_EMPLOYEES_WITH_DEEP_HIERARCHY = "managing employees with deep hierarchy";
    public static final String OPTIMIZATION_CATEGORY_MANAGERS_WITH_SALARY_DEVIATIONS = "correcting managers salary";
    public static final String INVALID_EMPLOYEE_ATTRIBUTE_PROVIDED_IN_CONFIGURATION = "Invalid employee attribute provided in the configuration - %s";
    public static final String CEO_NOT_FOUND = "Error: CEO not found in the hierarchy!";
    public static final String CONSOLE_LINE_SEPARATOR = "-".repeat(100);

    // Custom exit codes
    public static final int EXIT_CODE_INVALID_FILE_PATH = 100;
    public static final int EXIT_CODE_FILE_NOT_FOUND = 101;
    public static final int EXIT_CODE_CEO_NOT_FOUND = 102;
}
