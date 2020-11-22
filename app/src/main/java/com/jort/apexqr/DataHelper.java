package com.jort.apexqr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataHelper extends SQLiteOpenHelper {

    public static final String DATA_TABLE = "DATA_TABLE";
    public static final String COL_DATA_EMPLOYEE_ID = "EMPLOYEE_ID";
    public static final String COL_DATA_EMPLOYEE_NAME = "EMPLOYEE_NAME";
    public static final String COL_DATA_EMPLOYEE_STATUS = "EMPLOYEE_STATUS"; //Allowed or Not Allowed

    public DataHelper(@Nullable Context context) { super(context, "apex_qr_code.db", null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public int deleteData() {
        //Find customerModel in the database. If is found, delete it and return true,
        //If is not found, return false.
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + DATA_TABLE;
        Cursor cursor = db.rawQuery(queryString, null);
        return cursor.getCount();
    }

    public Cursor selectData() {
        //Get data from the database
        String queryString = "SELECT * FROM " + DATA_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);//Return type is cursor

        return cursor;
    }

    public boolean createData(DataModel dataModel) {
        //Opening a database and LOCK it
        SQLiteDatabase db = this.getWritableDatabase();
        //Values with Keys
        ContentValues cv = new ContentValues();

        cv.put(COL_DATA_EMPLOYEE_ID,dataModel.getEmployeeId());
        cv.put(COL_DATA_EMPLOYEE_NAME, dataModel.getEmployeeName());
        cv.put(COL_DATA_EMPLOYEE_STATUS,dataModel.getEmployeeStatus());

        //ColumnHack is just null but can be any String if you enter an empty row
        long insert = db.insert(DATA_TABLE, null, cv);

        //Return if success or not
        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }
}
