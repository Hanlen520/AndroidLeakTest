package android.example.xinxi.leaktest;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.leakcanary.internal.DisplayLeakActivity;
import com.squareup.leakcanary.internal.LeakCanaryInternals;

public class LeakInstall {

    public static Application myapplication;
    public static Context mycontext;
    public RefWatcher refWatcher;
    public static String TAG = "LeakInstall";

    public LeakInstall(Application application,Context context) {
        this.myapplication = application;
        this.mycontext = context;
    }


    public void install(){
        LeakCanary.install(this.myapplication, LeakUploadService.class,AndroidExcludedRefs.createAppDefaults().build());
        LeakCanaryInternals.setEnabled(this.mycontext , DisplayLeakActivity.class, false);
        //true展示 false不展示
        Log.i(TAG,"LeakInstall Complete");

    }

    public static void leakprint(){
        Log.i(TAG,"leakprint");
    }




}
