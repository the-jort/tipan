package com.jort.apexqr;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Data extends AppCompatActivity {

    Button btn_getUpdate;
    TextView tv_lastUpdate, tv_totalNumber, tv_availableUpdate;

    SetupHelper setupHelper;
    DataModel dataModel;
    DataHelper dataHelper;

    private static String ipAddress;
    private static String portNumber;
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String databaseName;
    private static String userName;
    private static String userPassword;
    private static String serverUrl;
    private static String deviceId;
    private Connection connection = null;
    private String availableUpdateDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        setupHelper = new SetupHelper(Data.this); //Get the last update date in SETUP_TABLE
        dataHelper = new DataHelper(Data.this); //Get all records in DATA_TABLE

        btn_getUpdate = findViewById(R.id.btn_getUpdate);
        tv_lastUpdate = findViewById(R.id.tv_lastUpdate);
        tv_totalNumber = findViewById(R.id.tv_totalNumber);
        tv_availableUpdate = findViewById(R.id.tv_availableUpdate);

        //Fetch last update date
        Cursor setupCursor = setupHelper.selectSetup();
        if (setupCursor.getCount() > 0) {
            setupCursor.moveToFirst();
            try {
                deviceId = setupCursor.getString(0);
                ipAddress = setupCursor.getString(1);
                portNumber = setupCursor.getString(2);
                databaseName = setupCursor.getString(3);
                userName = setupCursor.getString(4);
                userPassword = setupCursor.getString(5);
                tv_lastUpdate.setText(setupCursor.getString(6).toString());
                serverUrl = "jdbc:jtds:sqlserver://" + ipAddress + ":" + portNumber + "/" + databaseName;

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                Class.forName(Classes);
                connection = DriverManager.getConnection(serverUrl, userName, userPassword);

                if (connection != null) {
                    Statement statement = null;
                    try {
                        statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT convert(varchar(12),lastupdate,101) as lastupdate FROM TKQR_Setup");
                        while (resultSet.next()) {
                            availableUpdateDate = resultSet.getString(1);
                            //Toast.makeText(Data.this, serverUrl + resultSet.getString(1), Toast.LENGTH_LONG).show();
                        }

                        System.out.println("Server =*" + availableUpdateDate + "* vs Local =*" + tv_lastUpdate.getText().toString() + "*");
                        if (tv_lastUpdate.getText().toString().trim().equals(availableUpdateDate)) {
                            tv_availableUpdate.setText("No available update");
                            btn_getUpdate.setVisibility(View.INVISIBLE);
                        }
                        else {
                            tv_availableUpdate.setText("Server was updated on " + availableUpdateDate);
                            btn_getUpdate.setVisibility(View.VISIBLE);
                        }
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    tv_availableUpdate.setText("Connection is empty");
                }
            }
            catch (Exception e) {
                tv_availableUpdate.setText("Connection is empty");
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

        btn_getUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Connect to MS SQL Server and download records then add to DATA_TABLE
                if (connection != null) {

                    //Get the current date
//                    java.util.Date date = new java.util.Date();
//                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
//                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//                    availableUpdateDate = dateFormat.format(sqlDate);

                    Statement statement = null;
                    try {
                        //dataHelper = new DataHelper(Data.this);
                        //setupHelper = new SetupHelper(Data.this);

                        statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM TKQR_Data");

                        //Delete all records first
                        dataHelper.deleteData();

                        int rowCount = 0;

                        while (resultSet.next()) {
                            DataModel dataModel = new DataModel(resultSet.getString(1), resultSet.getString(2),resultSet.getString(3));
                            boolean resultData = dataHelper.createData(dataModel);
                            if (resultData) {
                                rowCount++;
                            }
                        }

                        //Update the date on local DB
                        setupHelper.updateSetup(deviceId, availableUpdateDate);
                        Toast.makeText(Data.this, "Local database updated.", Toast.LENGTH_LONG);

                        finish();
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    tv_availableUpdate.setText("Connection is empty");
                }
            }
        });

    }


}