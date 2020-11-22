package com.jort.apexqr;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Setup extends AppCompatActivity {

    TextView tv_deviceId;
    EditText et_serverAddress, et_portNumber, et_databaseName, et_userName, et_password;
    Button btn_Save;
    SetupModel setupModel;
    SetupHelper setupHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        setupHelper = new SetupHelper(Setup.this);

        //Initialization
        et_serverAddress = findViewById(R.id.et_serverAddress);
        et_portNumber = findViewById(R.id.et_portNumber);
        et_databaseName = findViewById(R.id.et_databaseName);
        et_userName = findViewById(R.id.et_userName);
        et_password = findViewById(R.id.et_password);
        tv_deviceId = findViewById(R.id.tv_deviceId);
        btn_Save = findViewById(R.id.btn_save);

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        tv_deviceId.setText(deviceId);

        Cursor cursor = setupHelper.selectSetup();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            tv_deviceId.setText(cursor.getString(0).toString());
            et_serverAddress.setText(cursor.getString(1).toString());
            et_portNumber.setText(cursor.getString(2).toString());
            et_databaseName.setText(cursor.getString(3).toString());
            et_userName.setText(cursor.getString(4).toString());
            et_password.setText(cursor.getString(5).toString());
        }

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Delete all records first before adding new one
                int count = setupHelper.deleteSetup();
                //Toast.makeText(Setup.this, "Record count" + count, Toast.LENGTH_SHORT).show();

                setupModel = new SetupModel(et_serverAddress.getText().toString(), et_portNumber.getText().toString(),
                        et_databaseName.getText().toString(), et_userName.getText().toString(), et_password.getText().toString(), tv_deviceId.getText().toString());

                boolean addResult = setupHelper.createSetup(setupModel);
                if (addResult) {
                    Toast.makeText(Setup.this, "Record saved.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Setup.this, "Saving failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}