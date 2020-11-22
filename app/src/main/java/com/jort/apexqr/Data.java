package com.jort.apexqr;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Data extends AppCompatActivity {

    Button btn_checkUpdate;
    TextView tv_lastUpdate, tv_totalNumber;

    SetupHelper setupHelper;
    DataModel dataModel;
    DataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        setupHelper = new SetupHelper(Data.this); //Get the last update date in SETUP_TABLE
        dataHelper = new DataHelper(Data.this); //Get all records in DATA_TABLE

        btn_checkUpdate = findViewById(R.id.btn_checkUpdate);
        tv_lastUpdate = findViewById(R.id.tv_lastUpdate);
        tv_totalNumber = findViewById(R.id.tv_totalNumber);

        //Fetch last update date
        Cursor setupCursor = setupHelper.selectSetup();
        if (setupCursor.getCount() > 0) {
            setupCursor.moveToFirst();
            try {
                tv_lastUpdate.setText(setupCursor.getString(6).toString());
            }
            catch (Exception e) {
                tv_lastUpdate.setText("NOT INITIALIZED");
                e.printStackTrace();
            }
        }

        //Fetch total numbers of employees in local storage
        Cursor dataCursor = dataHelper.selectData();
        try {
            tv_totalNumber.setText(String.valueOf(dataCursor.getCount()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        btn_checkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Connect to MS SQL Server and download records then add to DATA_TABLE

            }
        });

    }


}