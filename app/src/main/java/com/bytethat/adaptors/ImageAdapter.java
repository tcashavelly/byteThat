/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytethat.adaptors;

import android.R;
import android.content.Context;
import com.bytethat.datamodel.SoundListType;
import com.bytethat.datamodel.SoundType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Tom
 *
 * Copyright 2017 Thomas Cashavelly
 * All Rights Reserved
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private SoundListType soundList;

    public ImageAdapter(Context c, SoundListType soundList) {
        this.mContext = c;
        this.soundList = soundList;
    }

    public int getCount() {
        return this.soundList.getSoundList().size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView tv;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            tv = new TextView(mContext);
            tv.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
            tv = new TextView(mContext);
            tv.setLayoutParams(new GridView.LayoutParams(85, 85));
            //    tv = (TextView) convertView;
        }

        tv.setText("poo");
        imageView = setSounds(imageView);
        //  imageView.setImageResource(mThumbIds[position]);

        if (position == this.soundList.getSoundList().size() - 1) {
            imageView.setImageResource(mThumbIds[0]);

        } else {
            imageView.setImageResource(mThumbIds[1]);
        }

        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.btn_star, com.bytethat.R.drawable.speaker
    };

    public ImageView setSounds(ImageView view) {

        ImageView newView = new ImageView(mContext);
        newView = view;

        for (SoundType nSound : soundList.getSoundList()) {

            //     TextView text = (TextView) MainActivity.findViewById(android.sb.R.id.soundNamelabel);
            // text.setText("Soundboard Name");

        }
        return view;
    }


}