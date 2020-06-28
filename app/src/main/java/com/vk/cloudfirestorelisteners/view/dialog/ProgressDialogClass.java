package com.vk.cloudfirestorelisteners.view.dialog;

import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressDialogClass {
    private ProgressDialog dialogObj;
    private Activity activity;

    public ProgressDialogClass(final Activity activity) {
        this.activity = activity;
        dialogObj = new ProgressDialog(activity);
    }

    public void showDialog(final String title, final String msg) {
        dialogObj.setMessage(msg);
        dialogObj.setTitle(title);
        dialogObj.show();
    }

    public void dismissDialog() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialogObj.isShowing())
                    dialogObj.dismiss();
            }
        });
    }

}
