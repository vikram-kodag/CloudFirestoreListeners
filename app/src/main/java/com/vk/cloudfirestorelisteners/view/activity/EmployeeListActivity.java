package com.vk.cloudfirestorelisteners.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.vk.cloudfirestorelisteners.R;
import com.vk.cloudfirestorelisteners.databinding.ActivityEmployeeListBinding;
import com.vk.cloudfirestorelisteners.viewmodel.EmployeeListViewModel;


public class EmployeeListActivity extends AppCompatActivity {
    EmployeeListViewModel employeeListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEmployeeListBinding activityEmployeeListBinding = DataBindingUtil.setContentView(this, R.layout.activity_employee_list);
        employeeListViewModel = new EmployeeListViewModel(this, activityEmployeeListBinding);
        employeeListViewModel.setActionBar();
        employeeListViewModel.init();
        employeeListViewModel.setFabClickListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        employeeListViewModel.removeListener();
    }
}
