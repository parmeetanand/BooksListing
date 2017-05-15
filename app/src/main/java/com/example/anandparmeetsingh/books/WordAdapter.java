package com.example.anandparmeetsingh.books;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ParmeetSingh on 5/9/2017.
 */

public class WordAdapter extends BaseAdapter {
    ArrayList<Word> Word = new ArrayList<>();
    Activity mContext;

    public WordAdapter(Activity context, ArrayList<Word> Word) {
        mContext = context;
        this.Word = Word;
    }

    @Override
    public int getCount() {
        return Word.size();
    }

    @Override
    public Object getItem(int pos) {
        return Word.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final CampaignItemViewHolders holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.activity_list_display, null);
//            v = li.inflate(R.layout.aa, null);
            holder = new CampaignItemViewHolders(v);
            v.setTag(holder);
        } else {
            holder = (CampaignItemViewHolders) v.getTag();
        }


        Object currentBook = getItem(position);
        String magnitude = Word.get(position).getTitle();
        holder.title.setText(magnitude);

        String description = Word.get(position).getDate();
        holder.desc.setText(description);

        //String set =Word.get(position).getmSet();
        //holder.setv.setText(set);

        String set1 = Word.get(position).getAuthor();
        holder.set1.setText(set1);

        String[] url = Word.get(position).getDescription();
        holder.setv.setText(TextUtils.join(",", url));
        //TextView authorsView = (TextView) convertView.findViewById(R.id.primary_location);
        //authorsView.setText(TextUtils.join(", ", currentBook.getUrl()));

        return v;
    }

}

class CampaignItemViewHolders {
    TextView title;
    TextView setv;
    TextView desc;
    TextView set1;

    public CampaignItemViewHolders(View base) {

        title = (TextView) base.findViewById(R.id.magnitude);
        setv = (TextView) base.findViewById(R.id.date);
        desc = (TextView) base.findViewById(R.id.location_offset);
        set1 = (TextView) base.findViewById(R.id.primary_location);


    }


}
