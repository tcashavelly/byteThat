/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytethat.adaptors;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TCASHAVELLY
 *
 * Copyright 2017 Thomas Cashavelly
 * All Rights Reserved
 */
public class PhotoManager extends Activity {

    public void getPhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                Uri chosenImageUri = data.getData();

                Bitmap mBitmap = null;
                mBitmap = Media.getBitmap(this.getContentResolver(), chosenImageUri);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(PhotoManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PhotoManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}
