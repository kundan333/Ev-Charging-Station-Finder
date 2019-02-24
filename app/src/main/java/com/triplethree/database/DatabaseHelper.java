package com.triplethree.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;


public class DatabaseHelper  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EV_CHARGER";
    private static final String TABLE_NAME = "CHARGER_AVAILABILITY";
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String ADDRESS= "ADDRESS";
    private static final String AMOUNT = "AMOUNT";
     private static final String TYPE = "TYPE";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + "int, " + NAME
            + " TEXT," +  ADDRESS+ "TEXT, " + AMOUNT + "FLOATE," + TYPE + "TEXT ); ";


    private static final String TABLE_NAME_LOCATION="LOCATION";
        private static final String COL_1 = "ID";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String CREATE_TABLE_LOCATION = "CREATE TABLE " + TABLE_NAME + " ( " + ID + "int, " + LATITUDE
            + " FLOATE," +  LONGITUDE+ "FLOATE ); ";


    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_LOCATION);

    }
    public boolean insertData(int ID,String NAME,String ADDRESS,long AMOUNT,String TYPE){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValue=new ContentValues();
        contentValue.put("ID",ID);
        contentValue.put("NAME",NAME);
        contentValue.put("ADDRESS",ADDRESS);
        contentValue.put("AMOUNT",AMOUNT);
        contentValue.put("TYPE",TYPE);
 long result =db.insert(TABLE_NAME,null,contentValue);
 if(result==-1){
     return false;

 }
 else {
     return true;
 }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
