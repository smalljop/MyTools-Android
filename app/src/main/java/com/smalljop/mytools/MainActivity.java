package com.smalljop.mytools;

import android.Manifest;
import android.Manifest.permission;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.blankj.utilcode.util.ArrayUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.Maps;
import com.smalljop.mytools.utils.EnvironmentUtils;
import com.smalljop.mytools.utils.OkHttpUtil;
import com.smalljop.mytools.utils.PermissionUtils;
import com.smalljop.mytools.utils.ShakeUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Map;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    private ShakeUtils mShakeUtils;

    private static final String TAG = "MainActivity";

    private AlertDialog alertDialog4 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.checkPermission();
//        摇一摇
        mShakeUtils = new ShakeUtils(MyApplication.getContext());
        mShakeUtils.setOnShakeListener(new ShakeUtils.OnShakeListener() {
            final String[] items4 = new String[]{"dev", "prod"};
            final String[] urlItems = new String[]{
                    EnvironmentUtils.DEV_BASE_URL,
                    EnvironmentUtils.PROD_BASE_URL,
            };

            @Override
            public void onShake() {
                alertDialog4 = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("请选择环境")
                        .setIcon(R.mipmap.ic_launcher)
                        .setSingleChoiceItems(items4, -1, new DialogInterface.OnClickListener() {//添加单选框
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "选择的是：" + items4[i], 0).show();
                                EnvironmentUtils.setBaseUrl(urlItems[i]);
                                alertDialog4.dismiss();
                            }
                        })
                        .create();
                alertDialog4.show();
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params = Maps.newConcurrentMap();
                params.put("content", "2121");
                OkHttpUtil.postDataAsync(EnvironmentUtils.getBaseUrl() + "/msg/add", new OkHttpUtil.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                    }
                }, params);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mShakeUtils.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShakeUtils.onPause();
    }


    public void checkPermission() {
        PermissionUtils instance = PermissionUtils.getInstance();
        boolean granted = instance.checkPermission(permission.RECEIVE_SMS) && instance.checkPermission(permission.READ_SMS);
        String[] permissions = new String[]{permission.RECEIVE_SMS, permission.READ_SMS};
        if (!granted) {
            instance.requestPermissiion(MainActivity.this, permissions, 0, new PermissionUtils.RequestPermissionListener() {
                        @Override
                        public void requestConfirm() {

                        }

                        @Override
                        public void requestCancel() {

                        }

                        @Override
                        public void requestCancelAgain() {

                        }

                        @Override
                        public void requestFailed() {

                        }
                    }
            );
        }
    }
}
