package com.example.anandparmeetsingh.books;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ParmeetSingh on 5/9/2017.
 */

public class WordAdapter extends ArrayAdapter<Word> {



    public WordAdapter(Activity context, ArrayList<Word> books) {
        super(context, 0, books);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (convertView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_list_display, parent, false);
        }

        Word currentBook = getItem(position);

        TextView authorText = (TextView) listItemView.findViewById(R.id.primary_location);
        authorText.setText(currentBook.getAuthor());

        TextView titleText = (TextView) listItemView.findViewById(R.id.magnitude);
        titleText.setText(currentBook.getTitle());

        String[] url = currentBook.getDescription();
        TextView publisherText = (TextView) listItemView.findViewById(R.id.date);
        publisherText.setText(TextUtils.join(",", url));

        TextView set = (TextView) listItemView.findViewById(R.id.location_offset);
        set.setText(currentBook.getDate());

        ImageView booksView = (ImageView) listItemView.findViewById(R.id.imageView);
        if(currentBook.getBookImage() != null) {
            Picasso.with(getContext()).load(currentBook.getBookImage()).resize(160,200).into(booksView);

        }
        return listItemView;
    }
}