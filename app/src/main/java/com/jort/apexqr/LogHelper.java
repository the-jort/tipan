package com.jort.apexqr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class LogHelper extends SQLiteOpenHelper {

    public static final String LOG_TABLE = "LOG_TABLE";
    public static final String COL_LOG_EMPLOYEE_ID = "EMPLOYEE_ID";
    public static final String COL_LOG_EMPLOYEE_NAME = "EMPLOYEE_NAME";
    public static final String COL_LOG_EMPLOYEE_STATUS = "EMPLOYEE_STATUS"; //Allowed or Not Allowed
    public static final String COL_LOG_STATUS = "LOG_STATUS"; //Accept or Deny
    public static final String COL_LOG_DATE = "LOG_DATE";
    public static final String COL_LOG_TIME = "LOG_TIME";
    public static final String COL_LOG_DEVICE_ID = "DEVICE_ID";

    public LogHelper(@Nullable Context context) { super(context, "apex_qr_code.db", null, 1);}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }

    public void deleteLog() {
        //Find customerModel in the database. If is found, delete it and return true,
        //If is not found, return false.
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + LOG_TABLE;
        db.execSQL(queryString);
        db.close();
    }

    public Cursor selectLog() {
        //Get data from the database
        String queryString = "SELECT * FROM " + LOG_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);//Return type is cursor

        return cursor;
    }

    public boolean createLog(LogModel logModel) {
        //Opening a database and LOCK it
        SQLiteDatabase db = this.getWritableDatabase();
        //Values with Keys
        ContentValues cv = new ContentValues();

        cv.put(COL_LOG_EMPLOYEE_ID,logModel.getEmpId());
        cv.put(COL_LOG_EMPLOYEE_NAME, logModel.getFullName());
        cv.put(COL_LOG_EMPLOYEE_STATUS,logModel.getEmpStatus());
        cv.put(COL_LOG_STATUS,logModel.getLogStatus());
        cv.put(COL_LOG_DATE,logModel.getLogDate());
        cv.put(COL_LOG_TIME,logModel.getLogTime());
        cv.put(COL_LOG_DEVICE_ID,logModel.getDeviceId());

        //ColumnHack is just null but can be any String if you enter an empty row
        long insert = db.insert(LOG_TABLE, null, cv);

        //Return if success or not
        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }
}
