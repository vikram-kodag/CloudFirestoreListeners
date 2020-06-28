package com.vk.cloudfirestorelisteners.viewmodel;

import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.vk.cloudfirestorelisteners.callback.CallBack;
import com.vk.cloudfirestorelisteners.constants.Constant;
import com.vk.cloudfirestorelisteners.model.BranchDetails;
import com.vk.cloudfirestorelisteners.model.Employee;
import com.vk.cloudfirestorelisteners.repository.EmployeeRepository;
import com.vk.cloudfirestorelisteners.repository.impl.EmployeeRepositoryImpl;
import com.vk.cloudfirestorelisteners.utility.Utility;
import com.vk.cloudfirestorelisteners.view.activity.AddEmployeeActivity;
import com.vk.cloudfirestorelisteners.R;
import com.vk.cloudfirestorelisteners.databinding.ActivityAddEmployeeBinding;

import static com.vk.cloudfirestorelisteners.utility.Utility.isEmptyOrNull;


public class AddEmployeeViewModel {
    private AddEmployeeActivity addEmployeeActivity;
    private ActivityAddEmployeeBinding activityAddEmployeeBinding;
    private EmployeeRepository employeeRepository;
    private Employee employee;
    private Object employee1;

    public AddEmployeeViewModel(AddEmployeeActivity addEmployeeActivity, ActivityAddEmployeeBinding activityAddEmployeeBinding) {
        this.addEmployeeActivity = addEmployeeActivity;
        this.activityAddEmployeeBinding = activityAddEmployeeBinding;
        employeeRepository = new EmployeeRepositoryImpl(addEmployeeActivity);
    }

    public void setActionBar() {
        Toolbar tb = activityAddEmployeeBinding.toolbar;
        addEmployeeActivity.setSupportActionBar(tb);
        ActionBar actionBar = addEmployeeActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(R.string.app_name);
        }
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmployeeActivity.onBackPressed();
            }
        });
    }

    public void initView() {
        employee = (Employee) addEmployeeActivity.getIntent().getSerializableExtra(Constant.EMPLOYEE_MODEL);
        if (employee != null) {
            activityAddEmployeeBinding.btSave.setText(addEmployeeActivity.getText(R.string.update));
            activityAddEmployeeBinding.etEmpName.setText(employee.getEmpName());
            activityAddEmployeeBinding.etEmpDesignation.setText(employee.getDesignation());
            activityAddEmployeeBinding.etEmpAge.setText(String.valueOf(employee.getAge()));
            activityAddEmployeeBinding.etEmpSalary.setText(String.valueOf(employee.getSalary()));
            if (employee.getBranchDetails() != null) {
                activityAddEmployeeBinding.etBranchId.setText(employee.getBranchDetails().getBranchId().replace(Constant.BR, ""));
                activityAddEmployeeBinding.etBranchName.setText(employee.getBranchDetails().getBranchName());
                activityAddEmployeeBinding.etBranchLocation.setText(employee.getBranchDetails().getBranchLocation());
            }
        }
    }

    public void addClickListener() {

        activityAddEmployeeBinding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Employee employeeModel = fillEmployeeDetails();
                    if (employee == null)
                        saveData(employeeModel);
                    else
                        updateData(employeeModel);
                }
            }
        });
    }

    private void saveData(Employee employee) {
        employeeRepository.createEmployee(employee, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                addEmployeeActivity.finish();
            }

            @Override
            public void onError(Object object) {
                Utility.showMessage(addEmployeeActivity, addEmployeeActivity.getString(R.string.some_thing_went_wrong));
            }
        });
    }

    private void updateData(Employee employee) {
        employeeRepository.updateEmployee(this.employee.getEmpKey(), employee.getMap(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                addEmployeeActivity.finish();
            }

            @Override
            public void onError(Object object) {
                Utility.showMessage(addEmployeeActivity, addEmployeeActivity.getString(R.string.some_thing_went_wrong));
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        if (isEmptyOrNull(activityAddEmployeeBinding.etEmpName.getText().toString())) {
            activityAddEmployeeBinding.etEmpName.setError(addEmployeeActivity.getString(R.string.emp_name_error));
            isValid = false;
        }
        return isValid;
    }

    private Employee fillEmployeeDetails() {
        Employee employee = new Employee();
        employee.setEmpName(activityAddEmployeeBinding.etEmpName.getText().toString());
        employee.setDesignation(activityAddEmployeeBinding.etEmpDesignation.getText().toString());
        if (!isEmptyOrNull(activityAddEmployeeBinding.etEmpAge.getText().toString().trim()))
            employee.setAge(Integer.parseInt(activityAddEmployeeBinding.etEmpAge.getText().toString()));
        else
            employee.setAge(0);
        if (!isEmptyOrNull(activityAddEmployeeBinding.etEmpSalary.getText().toString().trim()))
            employee.setSalary(Long.parseLong(activityAddEmployeeBinding.etEmpSalary.getText().toString()));
        else
            employee.setSalary(0);
        if (this.employee == null)
            employee.setEmpId(Utility.getNewId());
        BranchDetails branchDetails = new BranchDetails();
        branchDetails.setBranchName(activityAddEmployeeBinding.etBranchName.getText().toString());
        branchDetails.setBranchId(Constant.BR + activityAddEmployeeBinding.etBranchId.getText().toString());
        branchDetails.setBranchLocation(activityAddEmployeeBinding.etBranchLocation.getText().toString());

        employee.setBranchDetails(branchDetails);
        return employee;
    }
}
