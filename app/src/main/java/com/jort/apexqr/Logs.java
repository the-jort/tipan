package com.jort.apexqr;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Logs extends AppCompatActivity {
    private static final String TAG = "Logs Activity";
    private TextView tv_totalRecords;
    private Button btn_uploadRecords;

    LogHelper logHelper;
    Cursor logCursor;
    SetupHelper setupHelper;

    private static String ipAddress;
    private static String portNumber;
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String databaseName;
    private static String userName;
    private static String userPassword;
    private static String serverUrl;
    private static String deviceId;
    private Connection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        tv_totalRecords = findViewById(R.id.tv_totalRecords);
        btn_uploadRecords = findViewById(R.id.btn_uploadRecords);

        logHelper = new LogHelper(Logs.this);


        //Fetch total numbers of employees in local storage
        logCursor = logHelper.selectLog();
        try {
            tv_totalRecords.setText(String.valueOf(logCursor.getCount()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        btn_uploadRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Fetch last update date
                setupHelper = new SetupHelper(Logs.this);
                Cursor setupCursor = setupHelper.selectSetup();
                int countInsert = 0; //Count of inserted records

                if (setupCursor.getCount() > 0) {
                    setupCursor.moveToFirst();

                    ConnectionThread connectionThread = new ConnectionThread(
                    setupCursor.getString(0),
                    setupCursor.getString(1),
                    setupCursor.getString(2),
                    setupCursor.getString(3),
                    setupCursor.getString(4),
                    setupCursor.getString(5),
                    "jdbc:jtds:sqlserver://" + setupCursor.getString(1) + ":" + setupCursor.getString(2) + "/" + setupCursor.getString(3));

                    connectionThread.run();

                    if (connection != null) {
                        try {
                            //Get all local records then loop
                            logCursor = logHelper.selectLog();
                            if (logCursor.getCount() > 0) {
                                String empId, empName, empStatus, logStatus, logDate, logTime, deviceId;
                                Statement statement = null;
                                ResultSet resultSet;

                                statement = connection.createStatement();
                                logCursor.moveToFirst();
                                while (!logCursor.isAfterLast()) {

                                    empId = logCursor.getString(0);
                                    empName = logCursor.getString(1);
                                    empStatus = logCursor.getString(2);
                                    logStatus = logCursor.getString(3);
                                    logDate = logCursor.getString(4);
                                    logTime = logCursor.getString(5);
                                    deviceId = logCursor.getString(6);

                                    String values = "'" + empId + "','" + empName + "','" + empStatus + "','" + logStatus + "','" + logDate
                                            + "','" + logTime + "','" + deviceId + "'";
                                    values = "INSERT INTO TKQR_Logs VALUES (" + values + ")";
                                    statement.executeUpdate(values);
                                    countInsert++;
                                    logCursor.moveToNext();
                                }
                                Log.d(TAG, "Insert Count: " + countInsert + "; Log Count: " + logCursor.getCount() + String.valueOf(countInsert == logCursor.getCount()));
                                if(countInsert == logCursor.getCount()) {
                                    //Delete all existing records
                                    logHelper.deleteLog();
                                    tv_totalRecords.setText("0");
                                    Toast.makeText(Logs.this, "LOGS UPLOADED", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(Logs.this, "NO RECORDS FOUND", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (SQLException e) {
                            Toast.makeText(Logs.this,String.valueOf(e.getStackTrace()), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(Logs.this, "CAN'T CONNECT TO SERVER", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    class ConnectionThread extends Thread {
        String deviceId;
        String ipAddress;
        String portNumber;
        String databaseName;
        String userName;
        String userPassword;
        String serverUrl;

        public ConnectionThread(String deviceId, String ipAddress, String portNumber, String databaseName, String userName, String userPassword, String serverUrl) {
            this.deviceId = deviceId;
            this.ipAddress = ipAddress;
            this.portNumber = portNumber;
            this.databaseName = databaseName;
            this.userName = userName;
            this.userPassword = userPassword;
            this.serverUrl = serverUrl;
        }

        @Override
        public void run() {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                Class.forName(Classes);
                connection = DriverManager.getConnection(serverUrl, userName, userPassword);
            } catch (ClassNotFoundException | SQLException e) {
                //Toast.makeText(Logs.this, serverUrl, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}