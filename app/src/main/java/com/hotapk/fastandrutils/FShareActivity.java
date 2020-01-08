package com.hotapk.fastandrutils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import cn.hotapk.fastandrutils.utils.FLogUtils;
import cn.hotapk.fastandrutils.utils.FShare;

public class FShareActivity extends AppCompatActivity {

    private Button sysshare;
    private Button customshare;
    private boolean isSharing;  //是否调起了分享。如果调起分享，这个值为true。
    private boolean isResume;  //Activity是否处于前台。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fshare);
        this.customshare = (Button) findViewById(R.id.custom_share);
        this.sysshare = (Button) findViewById(R.id.sys_share);

        customshare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new FShare.FShareBuilder(FShareActivity.this, "cn.hotapk.goodfilemanager.fileprovider")
                        .setShareContent("0000")
                        .setShareSubject("dddd")
                        .setShareFilter(new String[]{ "cn.andouya", "com.qihoo360.feichuan", "com.lenovo.anyshare", "com.sand.airdroid","com.tencent.mm"})
                        .build()
                        .shareByCustom();
            }
        });

        sysshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FShare.FShareBuilder(FShareActivity.this, null)
                        .setShareContent("0000")
                        .setShareSubject("dddd")
                        .build()
                        .shareBySystem();
            }
        });
    }

    @Override
    protected void onRestart() {

        Intent nowIntent = getIntent();
        String action =  nowIntent.getAction();

        String cPackage = getCallingPackage();

        ComponentName aActivity = getCallingActivity();

        final ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        am.getRunningAppProcesses();
        super.onRestart();
//        Log.i("TAG", "onRestart");
        FLogUtils.getInstance().e("onRestart");

        if (isSharing) {
            isSharing = false;
            //这里要延时0.2秒在判断是否回调了onResume，因为onRestart在onResume之前执行。
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 如果0.2秒后没有调用onResume，则认为是分享成功并且留着微信。
                    if (!isResume) {
//                        Log.i("TAG", "分享成功，留在微信");
                    }
                    FLogUtils.getInstance().e("分享成功，留在微信");
                }
            }, 200);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.i("TAG", "onStart");
        getAppTrafficList();
        FLogUtils.getInstance().e("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.i("TAG", "onResume");
        FLogUtils.getInstance().e("onResume");

        isSharing = false;
        isResume = true;

        Intent nowIntent = getIntent();
        String action =  nowIntent.getAction();

        String cPackage = getCallingPackage();

        ComponentName aActivity = getCallingActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.i("TAG", "onPause");
        FLogUtils.getInstance().e("onPause");

        isResume = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Log.i("TAG", "onStop");
//        getAppTrafficList();

        FLogUtils.getInstance().e("onStop");

    }

    public void getAppTrafficList(){
        //获取所有的安装在手机上的应用软件的信息，并且获取这些软件里面的权限信息
        PackageManager pm=getPackageManager();//获取系统应用包管理
        //获取每个包内的androidmanifest.xml信息，它的权限等等
        List<PackageInfo> pinfos=pm.getInstalledPackages
                (PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
        //遍历每个应用包信息
        for(PackageInfo info:pinfos){
            //请求每个程序包对应的androidManifest.xml里面的权限
            String[] premissions=info.requestedPermissions;
            if(premissions!=null && premissions.length>0){
                //找出需要网络服务的应用程序
                for(String premission : premissions){
                    if("android.permission.INTERNET".equals(premission)){
                        //获取每个应用程序在操作系统内的进程id
                        int uId=info.applicationInfo.uid;
                        //如果返回-1，代表不支持使用该方法，注意必须是2.2以上的
                        long rx= TrafficStats.getUidRxBytes(uId);
                        //如果返回-1，代表不支持使用该方法，注意必须是2.2以上的
                        long tx=TrafficStats.getUidTxBytes(uId);
                        if(rx<0 || tx<0){
                            continue;
                        }else{
       Toast.makeText(this, info.applicationInfo.loadLabel(pm)+"消耗的流量--"+
      Formatter.formatFileSize(this, rx+tx), Toast.LENGTH_SHORT);
                        }
                    }
                }
            }
        }
    }

}
