package com.example.android.pets.DATA;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by raghvendra on 23/9/17.
 */

public class PetProvider extends ContentProvider {

    /**
     * Initialize the provider and the database helper object.
     */
   private  static final int PETS=100;
    private static final int PETS_ID=101;
    private static final String LOG_TAG=PetProvider.class.getSimpleName();


    private PetdataFeeder mPetdataFeeder;

    private static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    static {



        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS,PETS);


        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS+"/#",PETS_ID);


    }
    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.

        mPetdataFeeder=new PetdataFeeder(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase db=mPetdataFeeder.getReadableDatabase();
        Cursor cursor;
        int match=sUriMatcher.match(uri);
        switch (match)
        {

            case PETS:
                cursor=db.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
               break;
            case PETS_ID:
                selection= PetContract.PetEntry._ID+"=?";
                selectionArgs= new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }


        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match=sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri,contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }

    }

   private Uri insertPet(Uri uri,ContentValues contentValues){

       String name=contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);

       Integer gender=contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
       Integer weight=contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
       if(name==null ){

          throw new IllegalArgumentException("Pet requires a name");
       }
        if(gender==null || !PetContract.PetEntry.isValidGender(gender))
        {
            throw new IllegalArgumentException("Pet requires a valid gender");

        }
      if(weight!=null  && weight<0){
         throw new  IllegalArgumentException("Pet requires valid weight");

      }
       SQLiteDatabase db=mPetdataFeeder.getWritableDatabase();
       long id=db.insert(PetContract.PetEntry.TABLE_NAME,null,contentValues);
        if(id==-1){

            Log.e(LOG_TAG,"Failed to insert row for"+uri);
        }

       getContext().getContentResolver().notifyChange(uri,null);
         return ContentUris.withAppendedId(uri,id);
   }


    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

       final int match=sUriMatcher.match(uri);
        switch (match){
            case PETS:
                return updatePet(uri,contentValues,selection,selectionArgs);
            case PETS_ID:
                selection=PetContract.PetEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                 return updatePet(uri,contentValues,selection,selectionArgs);
             default:
                 throw new IllegalArgumentException("Update is not supported for " + uri);
        }



    }

    private int updatePet(Uri uri,ContentValues contentValues ,String selection,String[] selectionArgs) {
        if (contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_NAME)) {

            String name = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
            if (name == null) {

                throw new IllegalArgumentException("Pet requires a name");

            }


        }


        if (contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !PetContract.PetEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }

        if (contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_WEIGHT)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }

                 }
            if (contentValues.size() == 0) {
                return 0;
            }

            SQLiteDatabase db = mPetdataFeeder.getWritableDatabase();


        int rowsUpdated=db.update(PetContract.PetEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        if(rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

             return rowsUpdated;
        }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
      SQLiteDatabase db=mPetdataFeeder.getWritableDatabase();
        final  int match=sUriMatcher.match(uri);
        switch (match){

            case PETS:

                 rowsDeleted=db.delete(PetContract.PetEntry.TABLE_NAME,selection,selectionArgs);
                if(rowsDeleted!=0)
                    getContext().getContentResolver().notifyChange(uri,null);
                return rowsDeleted;

            case PETS_ID:
                selection= PetContract.PetEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                getContext().getContentResolver().notifyChange(uri,null);
                rowsDeleted=db.delete(PetContract.PetEntry.TABLE_NAME,selection,selectionArgs);
                if(rowsDeleted!=0)
                    getContext().getContentResolver().notifyChange(uri,null);
                return rowsDeleted;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);

        }





    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {

        int match=sUriMatcher.match(uri);
        switch (match)
        {

            case PETS:
                return PetContract.PetEntry.CONTENT_LIST_TYPE;

            case PETS_ID:
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);

        }



    }
}
