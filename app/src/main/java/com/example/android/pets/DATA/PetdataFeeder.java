package com.example.android.pets.DATA;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by raghvendra on 21/9/17.
 */

public class PetdataFeeder extends SQLiteOpenHelper {

  public static final String LOG_TAG=PetdataFeeder.class.getSimpleName();
    public static final String DATABASE_NAME="shelter.db";
    public static final int DATABASE_VERSION=1;

    public PetdataFeeder(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
         String SQL_CREATE_PETS_TABLE="CREATE TABLE "+ PetContract.PetEntry.TABLE_NAME+"("
                 +PetContract.PetEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                 PetContract.PetEntry.COLUMN_PET_NAME+" TEXT NO NULL,"+
                 PetContract.PetEntry.COLUMN_PET_BREED+" TEXT,"+
                 PetContract.PetEntry.COLUMN_PET_GENDER+" INTEGER NOT NULL,"+
                 PetContract.PetEntry.COLUMN_PET_WEIGHT+" INTEGER NOT NULL DEFAULT 0);";

        Log.v(LOG_TAG,SQL_CREATE_PETS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
