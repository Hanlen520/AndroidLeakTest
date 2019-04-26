package android.example.xinxi.androidleaktest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private Button leakbutton;
    private String TAG = "MyApplication";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
    }


    private void addListenerOnButton() {
        leakbutton = (Button)findViewById(R.id.leakBtn);
        leakbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "go leak");
                goToLeak();
            }
        });
    }

    private void goToLeak() {
        context = getApplicationContext();
        Intent intent = new Intent(context, LeakActivity.class);
        startActivity(intent);
    }


}
