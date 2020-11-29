package com.jort.apexqr;

public class LogModel {
    String empId;
    String fullName;
    String empStatus; //Allowed or Not Allowed
    String logStatus; //Accept or Deny
    String logDate;
    String logTime;
    String deviceId;

    public LogModel(String empId, String fullName, String empStatus, String logStatus, String logDate, String logTime, String deviceId) {
        this.empId = empId;
        this.fullName = fullName;
        this.empStatus = empStatus;
        this.logStatus = logStatus;
        this.logDate = logDate;
        this.logTime = logTime;
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "LogModel{" +
                "empId='" + empId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", empStatus='" + empStatus + '\'' +
                ", logStatus='" + logStatus + '\'' +
                ", logDate='" + logDate + '\'' +
                ", logTime='" + logTime + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}

