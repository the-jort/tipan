package com.jort.tipan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

public class MainActivity extends AppCompatActivity {

    private Button btn_SetAppointment;
    private EditText et_FullName, et_MobileNumber;
    private Spinner spDepartment, spEmploymentType;

    private static String ip =  "sql88.apexmining.com"; //"192.168.83.230";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "AppDB";
    private static String username = "appuser";
    private static String password = "@mciAppUser";
    private static String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;
    private Connection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_SetAppointment = findViewById(R.id.bt_SetAppointment);
        et_FullName = findViewById(R.id.et_FullName);
        et_MobileNumber = findViewById(R.id.et_MobileNumber);
        spDepartment = (Spinner) findViewById(R.id.sp_Department);
        spEmploymentType = (Spinner) findViewById(R.id.sp_EmploymentType);

        ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.INTERNET},
                PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username, password);
            //Toast.makeText(this, "Connected to Database", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterDepartment = ArrayAdapter.createFromResource(this, R.array.arrDepartment, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence>  adapterEmploymentType = ArrayAdapter.createFromResource(this, R.array.arrEmploymentType, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterEmploymentType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spDepartment.setAdapter(adapterDepartment);
        spEmploymentType.setAdapter(adapterEmploymentType);

        btn_SetAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connection != null) {
                    Statement statement = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String currentDateTime = sdf.format(new Date());

                    // Get control number
                    String stringControl = getControlNumber();
                    String sFullName = et_FullName.getText().toString();
                    String sMobileNumber = et_MobileNumber.getText().toString();
                    String sDepartment = spDepartment.getSelectedItem().toString();

                    String values = "INSERT INTO [dbo].[Tipan_Trans] ([idx],[DepartmentConcern],[EmploymentType],[FullName],[DateRaised],[MobileNumber],[Status]) VALUES ('" +
                            stringControl + "','" + sDepartment + "','" + spEmploymentType.getSelectedItem().toString() + "','" +
                            sFullName + "','" + currentDateTime + "','" + sMobileNumber + "','new')";
                    try {
                        statement = connection.createStatement();
                        statement.executeUpdate(values);

                        if (statement != null) {
                            // Update Tipan Codes
                            updateControlNumber(stringControl);

                            // Show dialog
                            DialogFragment dialog = new MyDialogFragment();
                            ((MyDialogFragment) dialog).setTextToPass(stringControl);
                            dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");

                            // Send command to Raspberry Pi
                            new AsyncTask<Integer, Void, Void>() {
                                @Override
                                protected Void doInBackground(Integer... params) {
                                    try {
                                        executeRemoteCommand("root", "raspberry", "210.213.198.50", 22, sFullName, sMobileNumber, stringControl, sDepartment);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                            }.execute(1);
                            // Clear fields and refresh activity
                            et_FullName.setText("");
                            et_MobileNumber.setText("");
//                            finish();
//                            startActivity(getIntent());
                        }
                        else {
                            Toast.makeText(MainActivity.this, "No record inserted", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (SQLException e) {
                        Toast.makeText(MainActivity.this,String.valueOf(e.getStackTrace()), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this,"Connection failed", Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(MainActivity.this,spDepartment.getSelectedItem().toString() + et_FullName.getText().toString() + " " +
//                        et_MobileNumber.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateControlNumber (String ctrl) {
        if (connection != null) {
            Statement statement = null;

            String values = "UPDATE [dbo].[Tipan_Codes] set sCode = '" + ctrl + "' where sDesc = 'tipan'";
//                    Toast.makeText(MainActivity.this, ctrl, Toast.LENGTH_LONG).show();
            try {
                statement = connection.createStatement();
                statement.executeUpdate(values);
            }
            catch (SQLException e) {
                Toast.makeText(MainActivity.this,String.valueOf(e.getStackTrace()), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(MainActivity.this,"Connection failed", Toast.LENGTH_SHORT).show();
        }
    }

    private String getControlNumber () {
        String strNumber = "", strIncrement = "";
        int intIncrement;

        if (connection != null) {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM [dbo].[Tipan_Codes] where sDesc='tipan'");
                int year = Calendar.getInstance().get(Calendar.YEAR);

                if (resultSet.next()) {
                    strNumber = resultSet.getString(resultSet.findColumn("sCode")).toString();
                    strIncrement = strNumber.substring(6,10);
                    intIncrement = Integer.parseInt(strIncrement) + 1;
                    strIncrement = String.format("%05d", intIncrement);
                    strNumber = Integer.toString(year) + "-" + strIncrement;
                }
                else {
                    Toast.makeText(MainActivity.this, "Code not found", Toast.LENGTH_LONG).show();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(MainActivity.this,"Connection Failed", Toast.LENGTH_SHORT).show();
        }
        return strNumber;
    }
    public static String executeRemoteCommand(String username,String password,String hostname,int port, String name, String mobile, String ctrlNo, String department) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        // SSH Channel
        ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        //channelssh.setCommand("cd Desktop");
        //channelssh.setCommand("sudo java -classpath .:classes:/opt/pi4j/lib/'*' SimpleTextServer");
        channelssh.setCommand("asterisk -x 'dongle sms dongle0 "+ mobile +" Hi "+ name +", you have an appointment with " + department + " and your reference number is "+ ctrlNo +"'");
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }

//        private String getOtp() {
//        String stringOtp = "";
//
//        Random random = new Random();
//        stringOtp = String.format("%04d", random.nextInt(10000));
//
//        return stringOtp;
//    }
}