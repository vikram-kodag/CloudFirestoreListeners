package com.vk.cloudfirestorelisteners.repository;


import com.google.firebase.firestore.ListenerRegistration;
import com.vk.cloudfirestorelisteners.callback.CallBack;
import com.vk.cloudfirestorelisteners.callback.FirebaseChildCallBack;
import com.vk.cloudfirestorelisteners.model.Employee;

import java.util.HashMap;

public interface EmployeeRepository {
    void createEmployee(Employee employee, CallBack callBack);

    void updateEmployee(String employeeKey, HashMap map, CallBack callBack);

    void deleteEmployee(String employeeKey, CallBack callBack);

    void readEmployeeByKey(String employeeKey, CallBack callBack);

    void readEmployeesByDesignationAndBranch(String designation, String branch, CallBack callBack);

    void readAllEmployeesBySingleValueEvent(CallBack callBack);

    ListenerRegistration readAllEmployeesByDataChangeEvent(CallBack callBack);

    ListenerRegistration readAllEmployeesByChildEvent(FirebaseChildCallBack firebaseChildCallBack);

    void readEmployeesSalaryGraterThanLimit(long limit, CallBack callBack);
}
