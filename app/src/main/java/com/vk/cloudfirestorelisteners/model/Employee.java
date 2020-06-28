package com.vk.cloudfirestorelisteners.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Employee implements Serializable {
    private String empName;
    private String designation;
    private String empId;
    private String empKey;
    private BranchDetails branchDetails;
    @ServerTimestamp
    private Date createdDateTime, updatedDateTime;
    private int age;
    private long salary;

    public Employee() {
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public BranchDetails getBranchDetails() {
        return branchDetails;
    }

    public void setBranchDetails(BranchDetails branchDetails) {
        this.branchDetails = branchDetails;
    }

    public String getEmpKey() {
        return empKey;
    }

    public void setEmpKey(String empKey) {
        this.empKey = empKey;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    @Exclude
    public HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("empName", getEmpName());
        map.put("designation", getDesignation());
        map.put("branchDetails", getBranchDetails());
        map.put("updatedDateTime", FieldValue.serverTimestamp());
        map.put("age", getAge());
        map.put("salary", getSalary());
        return map;
    }
}
