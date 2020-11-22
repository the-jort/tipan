package com.jort.apexqr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.DataOutputStream;

public class MainActivity extends AppCompatActivity {

    CardView cv_scan, cv_logs, cv_data, cv_setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cv_scan = findViewById(R.id.cv_scan);
        cv_logs = findViewById(R.id.cv_logs);
        cv_data = findViewById(R.id.cv_data);
        cv_setup = findViewById(R.id.cv_setup);

        cv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "SCAN", Toast.LENGTH_SHORT).show();
            }
        });

        cv_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "LOGS", Toast.LENGTH_SHORT).show();
            }
        });

        cv_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Data.class);
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "DATA", Toast.LENGTH_SHORT).show();
            }
        });

        cv_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Setup.class);
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "SETUP", Toast.LENGTH_SHORT).show();
            }
        });
    }
}