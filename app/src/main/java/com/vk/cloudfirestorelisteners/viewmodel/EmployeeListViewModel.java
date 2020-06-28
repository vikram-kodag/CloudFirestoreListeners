package com.vk.cloudfirestorelisteners.viewmodel;


import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.ListenerRegistration;
import com.vk.cloudfirestorelisteners.R;
import com.vk.cloudfirestorelisteners.callback.FirebaseChildCallBack;
import com.vk.cloudfirestorelisteners.databinding.ActivityEmployeeListBinding;
import com.vk.cloudfirestorelisteners.exception.ExceptionUtil;
import com.vk.cloudfirestorelisteners.helper.IndexedLinkedHashMap;
import com.vk.cloudfirestorelisteners.model.Employee;
import com.vk.cloudfirestorelisteners.repository.EmployeeRepository;
import com.vk.cloudfirestorelisteners.repository.impl.EmployeeRepositoryImpl;
import com.vk.cloudfirestorelisteners.view.activity.AddEmployeeActivity;
import com.vk.cloudfirestorelisteners.view.activity.EmployeeListActivity;
import com.vk.cloudfirestorelisteners.view.adapter.EmployeeDetailsAdapter;

import static com.vk.cloudfirestorelisteners.constants.Constant.ADD;
import static com.vk.cloudfirestorelisteners.constants.Constant.DELETE;
import static com.vk.cloudfirestorelisteners.constants.Constant.UPDATE;


public class EmployeeListViewModel {
    private ActivityEmployeeListBinding activityEmployeeListBinding;
    private EmployeeListActivity employeeListActivity;
    private EmployeeDetailsAdapter employeeDetailsAdapter;
    private EmployeeRepository employeeRepository;
    private ListenerRegistration listenerRegistration;
    private String TAG = "EmployeeListViewModel";

    public EmployeeListViewModel(EmployeeListActivity employeeListActivity, ActivityEmployeeListBinding activityEmployeeListBinding) {
        this.activityEmployeeListBinding = activityEmployeeListBinding;
        this.employeeListActivity = employeeListActivity;
        employeeRepository = new EmployeeRepositoryImpl(employeeListActivity);
    }

    public void setActionBar() {
        Toolbar tb = activityEmployeeListBinding.toolbar;
        employeeListActivity.setSupportActionBar(tb);
        ActionBar actionBar = employeeListActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(R.string.app_name);
        }
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employeeListActivity.onBackPressed();
            }
        });
    }

    public void init() {
        getAllEmployees();
    }

    private void setAdapter(IndexedLinkedHashMap<String, Employee> employeeIndexedLinkedHashMap) {
        if (employeeDetailsAdapter == null) {
            employeeDetailsAdapter = new EmployeeDetailsAdapter(employeeListActivity, employeeIndexedLinkedHashMap);
            activityEmployeeListBinding.rvEmployeeList.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(employeeListActivity);
            activityEmployeeListBinding.rvEmployeeList.setLayoutManager(mLayoutManager);
            activityEmployeeListBinding.rvEmployeeList.setItemAnimator(new DefaultItemAnimator());
            activityEmployeeListBinding.rvEmployeeList.setAdapter(employeeDetailsAdapter);
        } else {
            employeeDetailsAdapter.reloadList(employeeIndexedLinkedHashMap);
        }
    }

    private void getAllEmployees() {
        if (listenerRegistration != null)
            listenerRegistration.remove();
        listenerRegistration = employeeRepository.readAllEmployeesByChildEvent(new FirebaseChildCallBack() {
            @Override
            public void onChildAdded(Object object) {
                if (object != null) {
                    Employee employee = (Employee) object;
                    if (employeeDetailsAdapter == null)
                        setAdapter(new IndexedLinkedHashMap<String, Employee>());
                    employeeDetailsAdapter.getEmployeeList().add(employee.getEmpKey(), employee);
                    employeeDetailsAdapter.reloadList(employeeDetailsAdapter.getEmployeeList().size() - 1, ADD);
                }
            }

            @Override
            public void onChildChanged(Object object) {
                if (object != null) {
                    Employee employee = (Employee) object;
                    employeeDetailsAdapter.getEmployeeList().update(employee.getEmpKey(), employee);
                    employeeDetailsAdapter.reloadList(employeeDetailsAdapter.getEmployeeList().getIndexByKey(employee.getEmpKey()), UPDATE);
                }
            }

            @Override
            public void onChildRemoved(Object object) {
                if (object != null) {
                    Employee employee = (Employee) object;
                    employeeDetailsAdapter.getEmployeeList().update(employee.getEmpKey(), employee);
                    employeeDetailsAdapter.reloadList(employeeDetailsAdapter.getEmployeeList().getIndexByKey(employee.getEmpKey()), DELETE);
                }
            }

            @Override
            public void onCancelled(Object object) {
                if (object != null)
                    ExceptionUtil.errorMessage(TAG, "getAllEmployees", new Exception(object.toString()));
            }
        });
    }

    public void removeListener() {
        if (listenerRegistration != null)
            listenerRegistration.remove();
        Glide.get(employeeListActivity).clearMemory();//clear memory
    }

    public void setFabClickListener() {
        activityEmployeeListBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employeeListActivity.startActivity(new Intent(employeeListActivity, AddEmployeeActivity.class));
            }
        });
    }
}
