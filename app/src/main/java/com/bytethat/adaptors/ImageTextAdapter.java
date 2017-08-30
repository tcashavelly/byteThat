/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytethat.adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.bytethat.R;
import com.bytethat.datamodel.SoundListType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Tom
 *
 * Copyright 2017 Thomas Cashavelly
 * All Rights Reserved
 */

public class ImageTextAdapter extends BaseAdapter {

    final int numberOfBytes;
    private Bitmap[] bitmap = null;
    public Context context;
    private LayoutInflater layoutInflater;
    private SoundListType soundList = null;

    public ImageTextAdapter(Context c, SoundListType soundList) {
        context = c;
        this.soundList = soundList;
        layoutInflater = LayoutInflater.from(context);

        numberOfBytes = this.soundList.getSoundList().size();
        bitmap = new Bitmap[1 + numberOfBytes];

        int i;
        for (i = 0; i < numberOfBytes; i++) {
            bitmap[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.speaker);
        }
        bitmap[i] = BitmapFactory.decodeResource(context.getResources(), com.bytethat.R.drawable.plus);

    }

    @Override
    public int getCount() {
        return bitmap.length;
    }

    @Override
    public Object getItem(int position) {
        return bitmap[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        if (convertView == null) {
            grid = new View(context);
            grid = layoutInflater.inflate(com.bytethat.R.layout.soundboard_image, parent, false);
        } else {
            grid = (View) convertView;
        }

        ImageView imageView = new ImageView(this.context);
        imageView = (ImageView) grid.findViewById(com.bytethat.R.id.thisimage);
        imageView.setImageBitmap(bitmap[position]);
        TextView textView = (TextView) grid.findViewById(com.bytethat.R.id.thistext);
        int soundListSize = soundList.getSoundList().size();

        if(position == soundListSize){
            textView.setText("Add byte");
        }else{
            textView.setText(soundList.getSoundList().get(position).getSoundName());
        }

        textView.setTextColor(Color.WHITE);

        //the spacing between the sound_create icons
        imageView.setPadding(8, 8, 8, 8);

        return grid;
    }
}