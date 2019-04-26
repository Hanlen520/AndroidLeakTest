package android.example.xinxi.androidleaktest;

import android.app.Application;
import android.content.Context;
import android.example.xinxi.leaktest.LeakInstall;
import android.util.Log;


public class MyApplication extends Application {

    private static MyApplication mInstance = null;
    public static MyApplication getInstance() {
        return mInstance;
    }
    private static Context context;
    private String tag = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        new LeakInstall(this,context).install();
        Log.i(tag,"MyApplication onCreate");
    }
}
