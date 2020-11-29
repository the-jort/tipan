package com.jort.apexqr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class SetupHelper extends SQLiteOpenHelper {

    public static final String SETUP_TABLE = "SETUP_TABLE";
    public static final String COL_SETUP_ADDRESS = "ADDRESS";
    public static final String COL_SETUP_PORT = "PORT";
    public static final String COL_SETUP_DATABASE = "DATABASENAME";
    public static final String COL_SETUP_USERNAME = "USERNAME";
    public static final String COL_SETUP_PASSWORD = "PASSWORD";
    public static final String COL_SETUP_DEVICE_ID = "DEVICE_ID";
    public static final String COL_SETUP_LAST_UPDATE = "LAST_UPDATE";

    public static final String DATA_TABLE = "DATA_TABLE";
    public static final String COL_DATA_EMPLOYEE_ID = "EMPLOYEE_ID";
    public static final String COL_DATA_EMPLOYEE_NAME = "EMPLOYEE_NAME";
    public static final String COL_DATA_EMPLOYEE_STATUS = "EMPLOYEE_STATUS"; //Allowed or Not Allowed

    public static final String LOG_TABLE = "LOG_TABLE";
    public static final String COL_LOG_EMPLOYEE_ID = "EMPLOYEE_ID";
    public static final String COL_LOG_EMPLOYEE_NAME = "EMPLOYEE_NAME";
    public static final String COL_LOG_EMPLOYEE_STATUS = "EMPLOYEE_STATUS"; //Allowed or Not Allowed
    public static final String COL_LOG_STATUS = "LOG_STATUS"; //Accept or Deny
    public static final String COL_LOG_DATE = "LOG_DATE";
    public static final String COL_LOG_TIME = "LOG_TIME";
    public static final String COL_LOG_DEVICE_ID = "DEVICE_ID";

    public SetupHelper(@Nullable Context context) {
        super(context, "apex_qr_code.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement;
        //Creating SETUP_TABLE
        createTableStatement = "CREATE TABLE " + SETUP_TABLE + " (" + COL_SETUP_DEVICE_ID + " TEXT, " +
                COL_SETUP_ADDRESS + " TEXT, " + COL_SETUP_PORT + " TEXT, " + COL_SETUP_DATABASE + " TEXT, " +
                COL_SETUP_USERNAME + " TEXT, " + COL_SETUP_PASSWORD + " TEXT, " + COL_SETUP_LAST_UPDATE + " TEXT)";
        db.execSQL(createTableStatement);

        //Creating DATA_TABLE
        createTableStatement = "CREATE TABLE " + DATA_TABLE + " (" + COL_DATA_EMPLOYEE_ID + " TEXT, " +
                COL_DATA_EMPLOYEE_NAME + " TEXT, " + COL_DATA_EMPLOYEE_STATUS + " TEXT)";
        db.execSQL(createTableStatement);

        //Creating LOG_TABLE
        createTableStatement = "CREATE TABLE " + LOG_TABLE + " (" + COL_LOG_EMPLOYEE_ID + " TEXT, " +
                COL_LOG_EMPLOYEE_NAME + " TEXT, " + COL_LOG_EMPLOYEE_STATUS + " TEXT, " + COL_LOG_STATUS + " TEXT, " +
                COL_LOG_DATE + " TEXT, " + COL_LOG_TIME + " TEXT, " + COL_LOG_DEVICE_ID + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean createSetup(SetupModel setupModel) {
        //Opening a database and LOCK it
        SQLiteDatabase db = this.getWritableDatabase();
        //Values with Keys
        ContentValues cv = new ContentValues();

        cv.put(COL_SETUP_ADDRESS,setupModel.getServerAddress());
        cv.put(COL_SETUP_PORT, setupModel.getPortNumber());
        cv.put(COL_SETUP_DATABASE,setupModel.getDatabaseName());
        cv.put(COL_SETUP_USERNAME,setupModel.getUserName());
        cv.put(COL_SETUP_PASSWORD,setupModel.getUserPassword());
        cv.put(COL_SETUP_DEVICE_ID,setupModel.getDeviceId());
        cv.put(COL_SETUP_LAST_UPDATE,"1/1/2020");

        //ColumnHack is just null but can be any String if you enter an empty row
        long insert = db.insert(SETUP_TABLE, null, cv);

        //Return if success or not
        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public int deleteSetup() {
        //Find customerModel in the database. If is found, delete it and return true,
        //If is not found, return false.
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + SETUP_TABLE;
        Cursor cursor = db.rawQuery(queryString, null);
        return cursor.getCount();
    }

    public Cursor selectSetup() {
        //Get data from the database
        String queryString = "SELECT * FROM " + SETUP_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);//Return type is cursor

        return cursor;
    }

    public void updateSetup(String deviceId, String sDate) {
        //Opening a database and LOCK it
        SQLiteDatabase db = this.getWritableDatabase();
        //Values with Keys
        db.execSQL("UPDATE " + SETUP_TABLE + " SET " + COL_SETUP_LAST_UPDATE + "='" + sDate + "' WHERE " + COL_SETUP_DEVICE_ID + "='" + deviceId + "'");
        System.out.println("Updated");
    }
}
