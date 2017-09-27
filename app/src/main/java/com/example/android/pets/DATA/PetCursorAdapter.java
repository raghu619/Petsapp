package com.example.android.pets.DATA;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.R;

/**
 * Created by raghvendra on 27/9/17.
 */

public class PetCursorAdapter extends CursorAdapter {


    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {


        return LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView Tname=(TextView) view.findViewById(R.id.name);
        TextView Tsummary=(TextView) view.findViewById(R.id.summary);


        String name=cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME));
        String breed=cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED));
        Tname.setText(name);
        Tsummary.setText(breed);

    }
}
