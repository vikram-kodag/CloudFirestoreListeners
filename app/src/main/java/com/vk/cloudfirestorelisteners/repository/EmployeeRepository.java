package com.vk.cloudfirestorelisteners.repository;


import com.vk.cloudfirestorelisteners.callback.CallBack;
import com.vk.cloudfirestorelisteners.callback.FirebaseChildCallBack;
import com.vk.cloudfirestorelisteners.firebase.FirebaseRequestModel;
import com.vk.cloudfirestorelisteners.model.Employee;

import java.util.HashMap;

public interface EmployeeRepository {
    void createEmployee(Employee employee, CallBack callBack);

    void updateEmployee(String employeeKey, HashMap map, CallBack callBack);

    void deleteEmployee(String employeeKey, CallBack callBack);

    void readEmployeeByKey(String employeeKey, CallBack callBack);

    void readEmployeeByName(String employeeName, CallBack callBack);

    void readAllEmployeesBySingleValueEvent(CallBack callBack);

    FirebaseRequestModel readAllEmployeesByDataChangeEvent(CallBack callBack);

    FirebaseRequestModel readAllEmployeesByChildEvent(FirebaseChildCallBack firebaseChildCallBack);
}
