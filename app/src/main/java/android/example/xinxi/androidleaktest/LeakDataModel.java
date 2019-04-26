package android.example.xinxi.androidleaktest;

import android.widget.TextView;

public class LeakDataModel {

    private static LeakDataModel sInstance;
    private TextView mRetainedTextView;

    public static LeakDataModel getInstance() {
        if (sInstance == null) {
            sInstance = new LeakDataModel();
        }
        return sInstance;
    }

    public void setRetainedTextView(TextView textView) {
        mRetainedTextView = textView;
    }
}

