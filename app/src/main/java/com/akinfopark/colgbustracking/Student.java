package com.akinfopark.colgbustracking;

public class Student {
    private String employeeName;
    private double empLat;
    private double empLong;
    private String empFcm;

    public Student(String employeeName, double empLat, double empLong, String empFcm) {
        this.employeeName = employeeName;
        this.empLat = empLat;
        this.empLong = empLong;
        this.empFcm = empFcm;
    }


    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public double getEmpLat() {
        return empLat;
    }

    public void setEmpLat(double empLat) {
        this.empLat = empLat;
    }

    public double getEmpLong() {
        return empLong;
    }

    public void setEmpLong(double empLong) {
        this.empLong = empLong;
    }

    public String getEmpFcm() {
        return empFcm;
    }

    public void setEmpFcm(String empFcm) {
        this.empFcm = empFcm;
    }
}
