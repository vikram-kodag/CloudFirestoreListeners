package com.vk.cloudfirestorelisteners.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.vk.cloudfirestorelisteners.viewmodel.AddEmployeeViewModel;
import com.vk.cloudfirestorelisteners.R;
import com.vk.cloudfirestorelisteners.databinding.ActivityAddEmployeeBinding;


public class AddEmployeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddEmployeeBinding activityAddEmployeeBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_employee);
        AddEmployeeViewModel addEmployeeViewModel = new AddEmployeeViewModel(this, activityAddEmployeeBinding);
        addEmployeeViewModel.setActionBar();
        addEmployeeViewModel.initView();
        addEmployeeViewModel.addClickListener();
    }
}
