package com.jort.apexqr;

public class DataModel {
    String employeeId;
    String employeeName;
    String employeeStatus;

    public DataModel(String employeeId, String employeeName, String employeeStatus) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeStatus = employeeStatus;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "employeeId='" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employeeStatus='" + employeeStatus + '\'' +
                '}';
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }
}
