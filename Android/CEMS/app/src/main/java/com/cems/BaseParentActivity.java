package com.cems;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewbinding.BuildConfig;

import java.util.ArrayList;
import java.util.List;

public class BaseParentActivity extends AppCompatActivity {
    private static final String TAG = BaseParentActivity.class.getName();
    static boolean a_NeedPermission = false;
    static AlertDialog m_SettingDialog = null;
    public static ProgressDialog progressDialog;
    int REQUEST_ID_MULTIPLE_PERMISSIONS = 100;
    BroadcastReceiver mBroadcastReceiver;
    AlertDialog m_DateTimeDialog;
    String[] permissions = new String[]{/* "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE" */};

    /* Access modifiers changed, original: protected */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
    }

    public void onResume() {
        super.onResume();

        if (VERSION.SDK_INT >= 23) {
            if (a_NeedPermission) {
                boolean a_IsNeeded = false;
                for (String p : this.permissions) {
                    if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                        a_IsNeeded = true;
                    }
                }
                if (a_IsNeeded) {
                    showAlertDialog("Please enable permission from App Settings.", 0);
                }
            } else {
                checkPermissionsNew();
            }
        }
    }

    private void showAlertDialog(String msg, final int action) {
        AlertDialog alertDialog = m_SettingDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            m_SettingDialog.dismiss();
            m_SettingDialog = null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg).setCancelable(false).setPositiveButton("Settings", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (action == 0) {
                    BaseParentActivity.a_NeedPermission = false;
                    dialog.dismiss();
                    BaseParentActivity.m_SettingDialog = null;
                    BaseParentActivity.this.goToAppSettings();
                }
            }
        });
        m_SettingDialog = builder.show();
    }

    private void goToAppSettings() {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + BuildConfig.LIBRARY_PACKAGE_NAME));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkPermissionsNew() {
        List<String> listPermissionsNeeded = new ArrayList();
        for (String p : this.permissions) {
            if (ContextCompat.checkSelfPermission(this, p) != 0) {
                listPermissionsNeeded.add(p);
            }
        }
        if (listPermissionsNeeded.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this, (String[]) listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), this.REQUEST_ID_MULTIPLE_PERMISSIONS);
        return false;
    }

    public void showErrorDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage(msg);

        builder.setPositiveButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create();
        builder.show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == this.REQUEST_ID_MULTIPLE_PERMISSIONS) {
            int i = 0;
            while (i < permissions.length) {
                if (!(grantResults[i] == 0 || ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]))) {
                    a_NeedPermission = true;
                }
                i++;
            }
            if (a_NeedPermission) {
                showAlertDialog("Please enable permission from App Settings.", 0);
            }
        }
    }


}
