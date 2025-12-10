package com.zcshou.gogogo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mengyu.location.R;
import com.zcshou.utils.GoUtils;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {
    private static boolean isPermission = false;
    private static final int SDK_PERMISSION_REQUEST = 127;
    private static final ArrayList<String> ReqPermissions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkDefaultPermissions();
        if (isPermission) {
            startMainActivity();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SDK_PERMISSION_REQUEST) {
            for (int i = 0; i < ReqPermissions.size(); i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    GoUtils.DisplayToast(this, getResources().getString(R.string.app_error_permission));
                    return;
                }
            }
            isPermission = true;
            startMainActivity();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkDefaultPermissions() {
        ReqPermissions.clear();
        // 定位精确位置
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ReqPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ReqPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        /*
         * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
         */
        // 读写权限
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ReqPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        // 读取电话状态权限
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ReqPermissions.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (ReqPermissions.isEmpty()) {
            isPermission = true;
        } else {
            requestPermissions(ReqPermissions.toArray(new String[0]), SDK_PERMISSION_REQUEST);
        }
    }

    private void startMainActivity() {
        if (!GoUtils.isNetworkAvailable(this)) {
            GoUtils.DisplayToast(this, getResources().getString(R.string.app_error_network));
            return;
        }

        if (!GoUtils.isGpsOpened(this)) {
            GoUtils.DisplayToast(this, getResources().getString(R.string.app_error_gps));
            return;
        }

        if (isPermission) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            WelcomeActivity.this.finish();
        }
    }
}
