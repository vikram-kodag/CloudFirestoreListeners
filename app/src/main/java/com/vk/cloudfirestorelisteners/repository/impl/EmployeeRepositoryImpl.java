package com.vk.cloudfirestorelisteners.repository.impl;

import android.app.Activity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.vk.cloudfirestorelisteners.callback.CallBack;
import com.vk.cloudfirestorelisteners.callback.FirebaseChildCallBack;
import com.vk.cloudfirestorelisteners.firebase.FirebaseRepository;
import com.vk.cloudfirestorelisteners.model.Employee;
import com.vk.cloudfirestorelisteners.repository.EmployeeRepository;
import com.vk.cloudfirestorelisteners.utility.Utility;
import com.vk.cloudfirestorelisteners.view.dialog.ProgressDialogClass;
import com.vk.cloudfirestorelisteners.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.firebase.firestore.Query.Direction.ASCENDING;
import static com.vk.cloudfirestorelisteners.constants.Constant.FAIL;
import static com.vk.cloudfirestorelisteners.constants.Constant.SUCCESS;
import static com.vk.cloudfirestorelisteners.firebase.FirebaseConstants.EMPLOYEE_TABLE;
import static com.vk.cloudfirestorelisteners.firebase.FirebaseDatabaseReference.DATABASE;


public class EmployeeRepositoryImpl extends FirebaseRepository implements EmployeeRepository {
    private ProgressDialogClass progressDialog;
    private Activity activity;
    private CollectionReference employeeCollectionReference;

    public EmployeeRepositoryImpl(Activity activity) {
        this.activity = activity;
        progressDialog = new ProgressDialogClass(activity);
        employeeCollectionReference = DATABASE.collection(EMPLOYEE_TABLE);
    }

    @Override
    public void createEmployee(Employee employee, final CallBack callBack) {
        String pushKey = employeeCollectionReference.document().getId();
        if (employee != null && !Utility.isEmptyOrNull(pushKey)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            employee.setEmpKey(pushKey);
            DocumentReference documentReference = employeeCollectionReference.document(pushKey);
            fireStoreCreate(documentReference, employee, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onSuccess(SUCCESS);
                }

                @Override
                public void onError(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onError(object);
                }
            });
        } else {
            callBack.onError(FAIL);
        }
    }

    @Override
    public void updateEmployee(String employeeKey, HashMap map, final CallBack callBack) {
        if (!Utility.isEmptyOrNull(employeeKey)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            DocumentReference documentReference = employeeCollectionReference.document(employeeKey);
            fireStoreUpdate(documentReference, map, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onSuccess(SUCCESS);
                }

                @Override
                public void onError(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onError(object);
                }
            });
        } else {
            callBack.onError(FAIL);
        }
    }

    @Override
    public void deleteEmployee(String employeeKey, final CallBack callBack) {
        if (!Utility.isEmptyOrNull(employeeKey)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            DocumentReference documentReference = employeeCollectionReference.document(employeeKey);
            fireStoreDelete(documentReference, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onSuccess(SUCCESS);
                }

                @Override
                public void onError(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onError(object);
                }
            });
        } else {
            callBack.onError(FAIL);
        }
    }

    @Override
    public void readEmployeeByKey(String employeeKey, final CallBack callBack) {
        if (!Utility.isEmptyOrNull(employeeKey)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            DocumentReference documentReference = employeeCollectionReference.document(employeeKey);
            readDocument(documentReference, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    if (object != null) {
                        DocumentSnapshot document = (DocumentSnapshot) object;
                        if (document.exists()) {
                            Employee employee = document.toObject(Employee.class);
                            callBack.onSuccess(employee);
                        } else {
                            callBack.onSuccess(null);
                        }
                    } else
                        callBack.onSuccess(null);
                    progressDialog.dismissDialog();
                }

                @Override
                public void onError(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onError(object);
                }
            });
        } else {
            callBack.onError(FAIL);
        }
    }

    @Override
    public void readEmployeesByDesignationAndBranch(String designation, String branch, final CallBack callBack) {
        if (!Utility.isEmptyOrNull(designation) && !Utility.isEmptyOrNull(branch)) {
            progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
            Query query = employeeCollectionReference
                    .whereEqualTo("branchDetails.designation", designation)
                    .whereEqualTo("branch", branch)
                    .orderBy("empName", ASCENDING);
            readQueryDocuments(query, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    if (object != null) {
                        /*
                         *Here we employees data order by empName in ASCENDING ORDER
                         */
                        callBack.onSuccess(getDataFromQuerySnapshot(object));
                    } else
                        callBack.onSuccess(null);
                    progressDialog.dismissDialog();
                }

                @Override
                public void onError(Object object) {
                    progressDialog.dismissDialog();
                    callBack.onError(object);
                }
            });
        } else {
            callBack.onError(FAIL);
        }
    }

    @Override
    public void readAllEmployeesBySingleValueEvent(final CallBack callBack) {
        progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
        //get all employees order by employee name
        Query query = employeeCollectionReference.orderBy("empName");
        readQueryDocuments(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    callBack.onSuccess(getDataFromQuerySnapshot(object));
                } else
                    callBack.onSuccess(null);
                progressDialog.dismissDialog();
            }

            @Override
            public void onError(Object object) {
                progressDialog.dismissDialog();
                callBack.onError(object);
            }
        });
    }

    @Override
    public ListenerRegistration readAllEmployeesByDataChangeEvent(final CallBack callBack) {
        progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
        //get all employees order by employee name
        Query query = employeeCollectionReference.orderBy("empName");
        return readQueryDocumentsByListener(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    callBack.onSuccess(getDataFromQuerySnapshot(object));
                } else
                    callBack.onSuccess(null);
                progressDialog.dismissDialog();
            }

            @Override
            public void onError(Object object) {
                progressDialog.dismissDialog();
                callBack.onError(object);
            }
        });
    }

    @Override
    public ListenerRegistration readAllEmployeesByChildEvent(final FirebaseChildCallBack firebaseChildCallBack) {
        progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
        //get all employees order by created date time
        Query query = employeeCollectionReference.orderBy("createdDateTime");
        return readQueryDocumentsByChildEventListener(query, new FirebaseChildCallBack() {
            @Override
            public void onChildAdded(Object object) {
                if (object != null) {
                    DocumentSnapshot document = (DocumentSnapshot) object;
                    if (document.exists()) {
                        Employee employee = document.toObject(Employee.class);
                        firebaseChildCallBack.onChildAdded(employee);
                    } else {
                        firebaseChildCallBack.onChildAdded(null);
                    }
                } else {
                    firebaseChildCallBack.onChildAdded(null);
                }
                progressDialog.dismissDialog();
            }

            @Override
            public void onChildChanged(Object object) {
                if (object != null) {
                    DocumentSnapshot document = (DocumentSnapshot) object;
                    if (document.exists()) {
                        Employee employee = document.toObject(Employee.class);
                        firebaseChildCallBack.onChildChanged(employee);
                    } else {
                        firebaseChildCallBack.onChildChanged(null);
                    }
                } else {
                    firebaseChildCallBack.onChildChanged(null);
                }
            }

            @Override
            public void onChildRemoved(Object object) {
                if (object != null) {
                    DocumentSnapshot document = (DocumentSnapshot) object;
                    if (document.exists()) {
                        Employee employee = document.toObject(Employee.class);
                        firebaseChildCallBack.onChildRemoved(employee);
                    } else {
                        firebaseChildCallBack.onChildRemoved(null);
                    }
                } else {
                    firebaseChildCallBack.onChildRemoved(null);
                }
            }

            @Override
            public void onCancelled(Object object) {
                firebaseChildCallBack.onCancelled(object);
                progressDialog.dismissDialog();
            }
        });
    }

    @Override
    public void readEmployeesSalaryGraterThanLimit(long limit, final CallBack callBack) {
        progressDialog.showDialog(getString(R.string.loading), getString(R.string.please_wait));
        //get all employees order by employee name
        Query query = employeeCollectionReference.whereGreaterThan("salary", limit).orderBy("salary");
        readQueryDocuments(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    callBack.onSuccess(getDataFromQuerySnapshot(object));
                } else
                    callBack.onSuccess(null);
                progressDialog.dismissDialog();
            }

            @Override
            public void onError(Object object) {
                progressDialog.dismissDialog();
                callBack.onError(object);
            }
        });
    }

    private String getString(int id) {
        return activity.getString(id);
    }

    public List<Employee> getDataFromQuerySnapshot(Object object) {
        List<Employee> employeeList = new ArrayList<>();
        QuerySnapshot queryDocumentSnapshots = (QuerySnapshot) object;
        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
            Employee employee = snapshot.toObject(Employee.class);
            employeeList.add(employee);
        }
        return employeeList;
    }
}
