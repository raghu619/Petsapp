/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.pets.DATA.PetContract;
import com.example.android.pets.DATA.PetCursorAdapter;
import com.example.android.pets.DATA.PetdataFeeder;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor> {

private static final int PET_LOADER=0;
    PetCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView displayView = (ListView)findViewById(R.id.list);
        View emptyview=findViewById(R.id.empty_view);
        displayView.setEmptyView(emptyview);
        adapter=new PetCursorAdapter(this,null);
        displayView.setAdapter(adapter);
        getLoaderManager().initLoader(PET_LOADER,null,this);

       displayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

               Intent intent=new Intent(CatalogActivity.this,EditorActivity.class);
               //Uri currentPetUri=Uri.parse(String.valueOf(PetContract.PetEntry.CONTENT_URI)+"/"+id);

               Uri currentPetUri= ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI,id);

               intent.setData(currentPetUri);

               startActivity(intent);
           }
       });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                   insertpet();


                // Do nothing for now
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /*public void displayDatabaseInfo() {


        Cursor cursor = getContentResolver().query(PetContract.PetEntry.CONTENT_URI, projection, null, null, null);

        if (cursor != null) {

            displayView.setText("The peta table contains " + cursor.getCount() + " pets.\n\n");
            displayView.append(PetContract.PetEntry._ID
                    + " - " +
                    PetContract.PetEntry.COLUMN_PET_NAME
                    + " - " +
                    PetContract.PetEntry.COLUMN_PET_BREED
                    + " - " +
                    PetContract.PetEntry.COLUMN_PET_GENDER
                    + " - " +
                    PetContract.PetEntry.COLUMN_PET_WEIGHT + "\n");
            int idColumnIndex = cursor.getColumnIndex(PetContract.PetEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
            int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
            int genderColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);
            while (cursor.moveToNext())

            {
                int CurrentId = cursor.getInt(idColumnIndex);

                String Name = cursor.getString(nameColumnIndex);
                String Breed = cursor.getString(breedColumnIndex);
                int Gender = cursor.getInt(genderColumnIndex);
                int Weight = cursor.getInt(weightColumnIndex);
                displayView.append(("\n" + CurrentId + " - " + Name + " - " + Breed + " - " + Gender + " - " + Weight));

            }

        }


        if (cursor != null) {


            adapter = new PetCursorAdapter(this, cursor);
            displayView.setAdapter(adapter);

        }



    }

*/
    private void insertpet(){

        ContentValues values=new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME,"Toto");
        values.put(PetContract.PetEntry.COLUMN_PET_BREED,"Terrier");
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT,7);


        //long newRowId=db.insert(PetContract.PetEntry.TABLE_NAME,null,values);
        //Log.v("Dummy Data created",""+newRowId);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String projection[] = {
                PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED,
                };

        return new CursorLoader(this, PetContract.PetEntry.CONTENT_URI,projection,null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {


        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       adapter.swapCursor(null);
    }
}
