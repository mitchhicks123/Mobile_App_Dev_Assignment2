package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    private static final String DB_NAME = "locationAddressDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "locationTable";

    //initialize column values
    private static final String ID_COL = "id";
    private static final String ADDRESS_COL = "address";
    private static final String LATITUDE_COL = "latitude";
    private static final String LONGITUDE_COL = "longitude";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ADDRESS_COL + " TEXT,"
                + LATITUDE_COL + " REAL,"
                + LONGITUDE_COL + " REAL)";

        db.execSQL(query);
    }


    // this method is use to add new course to our sqlite database.
    public void addLocation(String address, double latitude, double longitude) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // passing all values with their key value
        address = address.toLowerCase(Locale.ROOT);
        values.put(ADDRESS_COL, address);
        values.put(LATITUDE_COL, latitude);
        values.put(LONGITUDE_COL, longitude);

        db.insert(TABLE_NAME, null, values);

        // close db connection
        db.close();
    }
    //this method is used to search for an address in the database
    public double[] readLocationAddress(String address) {

        address = address.toLowerCase(Locale.ROOT);
        //query database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{LATITUDE_COL, LONGITUDE_COL}, ADDRESS_COL + "=?", new String[]{address},null, null, null);

        double[] latLong = new double[2];

        //checks to see if there are values in the cursor... otherwise empty query
        if(cursor.moveToFirst()){
            latLong[0] = cursor.getDouble(0);
            latLong[1] = cursor.getDouble(1);
            db.close();
            return latLong;

        }else{ //empty query
            latLong[0] = 200;
            db.close();
            return latLong;
        }
    }
    //this method is used to update an address in the database
    public void updateLocationAddress(String newAddress, String oldAddress) {

        oldAddress = oldAddress.toLowerCase(Locale.ROOT);
        newAddress = newAddress.toLowerCase(Locale.ROOT);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ADDRESS_COL, newAddress);
        db.update(TABLE_NAME, values, ADDRESS_COL + "=?", new String[]{oldAddress});
        db.close();
    }
    //this method is used to delete an address in the database
    public void deleteLocationAddress(String address) {

        address = address.toLowerCase(Locale.ROOT);
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(TABLE_NAME, ADDRESS_COL + "=?", new String[]{address});
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
