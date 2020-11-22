package com.jort.apexqr;

public class SetupModel {
    String serverAddress;
    String portNumber;
    String databaseName;
    String userName;
    String userPassword;
    String deviceId;

    public SetupModel(String serverAddress, String portNumber, String databaseName, String userName, String userPassword, String deviceId) {
        this.serverAddress = serverAddress;
        this.portNumber = portNumber;
        this.databaseName = databaseName;
        this.userName = userName;
        this.userPassword = userPassword;
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "SetupModel{" +
                "serverAddress='" + serverAddress + '\'' +
                ", portNumber='" + portNumber + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
