package com.akinfopark.colgbustracking;

public class DriverInfo {
    private String DrivName;
    private String DrivNumber;
    private String DrivBusNumber;
    private String DriveFToken;

    public String getDriveFToken() {
        return DriveFToken;
    }

    public void setDriveFToken(String driveFToken) {
        DriveFToken = driveFToken;
    }

    public String getDrivName() {
        return DrivName;
    }

    public void setDrivName(String drivName) {
        DrivName = drivName;
    }

    public String getDrivNumber() {
        return DrivNumber;
    }

    public void setDrivNumber(String drivNumber) {
        DrivNumber = drivNumber;
    }

    public String getDrivBusNumber() {
        return DrivBusNumber;
    }

    public void setDrivBusNumber(String drivBusNumber) {
        DrivBusNumber = drivBusNumber;
    }
}
