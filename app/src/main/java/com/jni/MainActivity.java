package com.jni;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView sample_text;
    private JNI jni;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sample_text = findViewById(R.id.sample_text);
        jni = new JNI(getApplicationContext());
        findViewById(R.id.java_test).setOnClickListener(this);
        findViewById(R.id.bt_javaInt).setOnClickListener(this);
        findViewById(R.id.bt_javanull).setOnClickListener(this);
        findViewById(R.id.bt_javaString).setOnClickListener(this);
        findViewById(R.id.bt_static).setOnClickListener(this);
        findViewById(R.id.btn).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.java_test:
                startActivity(new Intent(MainActivity.this, MoHuActivity.class));
                break;
            case R.id.btn:
                sample_text.setText(jni.stringTest());
                break;
            case R.id.btn1:
                Toast.makeText(this, "---" + jni.addJia(3, 4), Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_javanull:
                jni.callbackmethod();
                break;
            case R.id.bt_javaInt:
                jni.callbackIntmethod();
                break;
            case R.id.bt_javaString:
                jni.callbackStringmethod();
                break;
            case R.id.bt_static:
                jni.callStaticmethod();
                break;
        }
    }
}
