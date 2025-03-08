package com.xorg.wo.model;

import java.util.List;

public class Employee {

    private int id;
    private String firstName;
    private String lastName;
    private double salary;
    private int managerId;
    private List<Employee> reportees;

    public Employee(int id, String firstName, String lastName, double salary, int managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public List<Employee> getReportees() {
        return reportees;
    }

    public void setReportees(List<Employee> reportees) {
        this.reportees = reportees;
    }

    public void addReportee(Employee reportee) {
        reportees.add(reportee);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", managerId=" + managerId +
                ", reportees=" + reportees +
                '}';
    }
}