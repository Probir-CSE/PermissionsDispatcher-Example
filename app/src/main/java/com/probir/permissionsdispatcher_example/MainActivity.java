package com.probir.permissionsdispatcher_example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivityPermissionsDispatcher.LocationAccessWithPermissionCheck(this);
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void LocationAccess() {
        Toast.makeText(this, "Allow Location Access", Toast.LENGTH_SHORT).show();
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void DeniedPermission(){
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
    }
    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    void NeverAsk(){

        new AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("Sorry, You Can't Use This App Without Location Access Permission. GPS permission allows us to access location data. Please allow in App Settings for additional functionality. Goto Permission > Allow Location Access")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }).show();



        Toast.makeText(this, " Will Never Ask Again",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
        MainActivityPermissionsDispatcher.LocationAccessWithPermissionCheck(this);
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("GPS permission allows us to access location data. Please allow in App Settings for additional functionality.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }




}
