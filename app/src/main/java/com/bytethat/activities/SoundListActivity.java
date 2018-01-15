/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytethat.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;

import com.bytethat.Manifest;
import com.bytethat.R;
import com.bytethat.database.DatabaseHelper;
import com.bytethat.database.Utilities;
import com.bytethat.adaptors.ImageTextAdapter;
import com.bytethat.adaptors.Recorder;
import com.bytethat.datamodel.SoundBoardType;
import com.bytethat.datamodel.SoundListType;
import com.bytethat.datamodel.SoundType;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.bytethat.adaptors.ImageUtilities.rotateBitmap;

/**
 * @author Tom
 *         <p>
 *         http://icons.mysitemyway.com/ultra-glossy-silver-buttons-icons-media/
 *         http://icons.mysitemyway.com/terms-of-use/
 *         Free of use for personal and commercial
 *         <p>
 *         Copyright 2017 Thomas Cashavelly
 *         All Rights Reserved
 */
public class SoundListActivity extends Activity {

    GridView gridView = null;
    SoundListType soundList = new SoundListType();
    SoundBoardType currentSoundBoard = new SoundBoardType();
    ImageTextAdapter imageAdapter = null;
    RelativeLayout soundLayout;
    Utilities util = null;
    SoundType workingSound = new SoundType();
    Context context = null;
    Recorder recorder;
    EditText nameBox = null;
    ImageView photoIcon = null;
    private AdView mAdView;
    DatabaseHelper db;
    Runnable run;
    private Handler handler = new Handler();
    Dialog dialogDeleteSound = null;
    Dialog dialogRerecordSound = null;
    Dialog dialogEditSoundBoard = null;
    Dialog dialogDeleteSoundBoard = null;
    Button saveSoundButton;
    Button cancelSoundButton;
    Button deleteSoundButton = null;
    ImageButton recordButton;
    ImageButton playButton;
    boolean editingText = false;
    boolean editing = false;
    boolean recording = false;
    boolean editSound = false;
    boolean recordedNewSound = false;
    int workingSoundBoard = 0;
    int numWorkingSound = 0;
    int change = 0;
    String waveUUID = "";
    String currentSoundBoardId;
    String filepathTempPhoto;
    static int CHANGEINSOUND = 50;
    static int NOCHANGEINSOUND = 51;
    static int CHOOSEEXISTINGPHOTO = 11;


    @Override
    public void onCreate(Bundle icicle) {

        Bundle extras = getIntent().getExtras();
        this.workingSoundBoard = getIntent().getExtras().getInt("CurrentSB");
        this.currentSoundBoardId = (String) (extras != null ? extras.get("SoundboardId") : "nothing passed in");
        this.util = (Utilities) (extras != null ? extras.get("Util") : "nothing passed in");

        ActivityCompat.requestPermissions(SoundListActivity.this,
                new String[]{android.Manifest.permission.RECORD_AUDIO},
                1);


        super.onCreate(icicle);
        db = new DatabaseHelper(util.getContext());
        recorder = new Recorder(util.getContext());
        setUpSoundboard();

        run = new Runnable() {
            public void run() {
                showToastMessage("Max 20 seconds");
                stopRecording();
            }
        };

    }


    public void setUpSoundboard() {

        setContentView(R.layout.soundboard_grid);
        gridView = (GridView) findViewById(R.id.grid);
        TextView label = (TextView) findViewById(R.id.soundboardLabel);
        label.setText(db.getSoundBoardById(this.currentSoundBoardId).getSoundBoardName());
        ImageView settingsIcon = (ImageView) findViewById(R.id.settingsIcon);
        settingsIcon.setImageResource(R.drawable.ic_edit_white_48dp);
        settingsIcon.setOnClickListener(editSoundBoardListener);
        label.setTextColor(Color.WHITE);
        label.setBackgroundColor(Color.BLACK);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        soundList = db.getSoundsBySoundboardId(currentSoundBoardId);
        Collections.reverse(Arrays.asList(soundList.getSoundList()));
        imageAdapter = new ImageTextAdapter(this, this.soundList);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(openSoundListener);
        gridView.setOnItemLongClickListener(editSoundListener);
        gridView.invalidateViews();
        gridView.getSelector().setAlpha(100);
        this.editingText = false;
        this.recordedNewSound = false;
        this.context = util.getContext();
        this.currentSoundBoard = db.getSoundBoardById(this.currentSoundBoardId);
        drawBackground();

    }

    public void drawBackground() {
        String photoLocation = this.currentSoundBoard.getSoundBoardPhotoLocation();

        if (photoLocation.contentEquals("none")) {
            gridView.setBackgroundColor(Color.BLACK);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(photoLocation);
            Bitmap bitmapOriented = orientBitmap(bitmap, photoLocation);
            Drawable draw = new BitmapDrawable(getResources(), bitmapOriented);
            gridView.setBackground(draw);
        }
    }

    public Bitmap scaleBitmap(Bitmap bitmap) {

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
   //     Matrix matrix = new Matrix();
   //     matrix.postRotate(90);

    //    Bitmap bmRotated = rotateBitmap(bitmap, orientation);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, height, width, true);
    //    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
        return scaledBitmap;
    }

    public Bitmap orientBitmap(Bitmap bitmap, String path){
        Bitmap orientedBitmap = null;

/*        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);


        orientedBitmap = rotateBitmap(bitmap, orientation);
*/

      //  Bitmap myBitmap = getBitmap(imgFile.getAbsolutePath());
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            }
            else if (orientation == 3) {
                matrix.postRotate(180);
            }
            else if (orientation == 8) {
                matrix.postRotate(270);
            }
            orientedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return orientedBitmap;
    }

    @Override
    public void onBackPressed() {

        Intent mIntent = new Intent();

        if (change > 0) {
            setResult(CHANGEINSOUND, mIntent);

            if (editing) {
                editing = false;
                setUpSoundboard();
            } else {
                super.onBackPressed();
            }

        } else {
            setResult(NOCHANGEINSOUND, mIntent);

            if (editing) {
                editing = false;
                setUpSoundboard();
            } else {
                super.onBackPressed();
            }

        }
    }

    public void getPhotoFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, CHOOSEEXISTINGPHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSEEXISTINGPHOTO) {
                onSelectFromGalleryResult(data);
                drawBackground();
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        String photoID = util.generateUUID();
        String filepath = this.context.getFilesDir() + File.separator + photoID + ".jpg";
        File file = new File(filepath);
        filepathTempPhoto = filepath;
        FileOutputStream out = null;



        Bitmap bitmap = this.scaleBitmap(bm);

        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance

        } catch (Exception e) {
            e.printStackTrace();
        }




        finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void killKeyboard() {

        if (this.editingText) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isAcceptingText()) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        this.editingText = false;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private OnItemClickListener openSoundListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            int numOfNames = soundList.getSoundList().size();
            if (arg2 == numOfNames) {
                editSound = false;
                createNewSound();
            } else {
                playSound(soundList.getSoundList().get(arg2).getWaveId());
            }
        }
    };
    /*
     * Edit Sound
     */
    private OnItemLongClickListener editSoundListener = new OnItemLongClickListener() {

        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (arg2 < soundList.getSoundList().size()) {
                editSound = true;
                editSound(arg2);
            }

            return true;
        }
    };

    private OnClickListener editPhotoListener = new OnClickListener() {

        public void onClick(View arg0) {
            //   pickPhoto();
            getPhotoFromGallery();

        }
    };

    private OnClickListener editSoundBoardListener = new OnClickListener() {

        public void onClick(View arg0) {
            //   pickPhoto();
            editSoundBoard();

        }
    };


    public void createNewSound() {
        setContentView(R.layout.sound_create);
        waveUUID = "";
        nameBox = (EditText) findViewById(R.id.soundNameTextBox);
        soundLayout = (RelativeLayout) findViewById(R.id.createSoundLayout);
        nameBox.setHint("Name");
        addRecordButton();
        addPlayButton();
        addCancelButton();
        addSaveButton();
        addTextBoxListener();
        addTextBoxKeyboardListener();
        editing = false;
        recording = false;
        recordedNewSound = false;
    }

    public void editSound(int s) {
        editing = true;
        this.workingSound = this.soundList.getSoundList().get(s);
        waveUUID = this.workingSound.getWaveId();
        this.numWorkingSound = s + 1;
        setContentView(R.layout.sound_edit);
        addRecordButton();
        addPlayButton();
        addCancelButton();
        addSoundName(s);
        addDeleteButton();
        addSaveButton();
        recording = false;
        recordedNewSound = false;
    }

    public void addRecordButton() {
        recordButton = (ImageButton) findViewById(R.id.recordButton);
        recordButton.setOnClickListener(recordSoundListener);
        recordButton.setBackgroundResource(R.drawable.record);
    }

    public void addPlayButton() {
        playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setBackgroundResource(R.drawable.play);
        playButton.setOnClickListener(playSoundListener);
        disablePlayButton();
        if (editing) {
            enablePlayButton();
        }
    }

    public void disablePlayButton() {
        playButton.setClickable(false);
        playButton.setAlpha(.1f);
    }

    public void enablePlayButton() {
        playButton.setClickable(true);
        playButton.setAlpha(1f);
    }

    public void addCancelButton() {
        cancelSoundButton = (Button) findViewById(R.id.cancelSoundButton);
        cancelSoundButton.setOnClickListener(cancelSoundListener);
    }

    public void addSaveButton() {
        saveSoundButton = (Button) findViewById(R.id.saveSoundButton);
        saveSoundButton.setOnClickListener(saveSoundListener);

    }

    public void addDeleteButton() {
        deleteSoundButton = (Button) findViewById(R.id.deleteSoundButton);
        deleteSoundButton.setOnClickListener(confirmDeleteSoundListener);
    }

    public void confirmDeleteSound() {
        dialogDeleteSound = new Dialog(this);
        dialogDeleteSound.setContentView(R.layout.sound_delete);
        dialogDeleteSound.setTitle("Delete byte, Are you Sure? ");
        dialogDeleteSound.setCancelable(true);

        Button buttonCancel = (Button) dialogDeleteSound.findViewById(R.id.cancelSoundButton);
        buttonCancel.setOnClickListener(cancelButtonDeleteSoundListener);

        Button buttonDelete = (Button) dialogDeleteSound.findViewById(R.id.deleteSoundButton);
        buttonDelete.setOnClickListener(deleteSoundListener);

        dialogDeleteSound.show();

    }

    public void addSoundName(int s) {
        nameBox = (EditText) findViewById(R.id.soundNameTextBox);
        nameBox.setHint("Name");
        nameBox.setText(this.soundList.getSoundList().get(s).getSoundName());
        nameBox.setOnFocusChangeListener(editingTextListener);
    }

    public void addTextBoxListener() {
        nameBox = (EditText) findViewById(R.id.soundNameTextBox);
        nameBox.setOnFocusChangeListener(editingTextListener);
    }

    public void addTextBoxKeyboardListener() {
        nameBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                    recordButton.setClickable(true);
                }
            }
        });
    }

    private OnFocusChangeListener editingTextListener = new OnFocusChangeListener() {

        public void onFocusChange(View arg0, boolean arg1) {
            editingText();
        }
    };


    //Action when Record Button is clicked by User
    private OnClickListener recordSoundListener = new OnClickListener() {

        public void onClick(View arg0) {
            killKeyboard();
            if (!waveUUID.isEmpty()) {
                verifyRecordAgain();
                //TODO: status text also contain timer
            } else {
                //TODO: Kill keyboard when editing text first

                recordButton.setBackgroundResource(R.drawable.stop);
                saveSoundButton.setClickable(false);
                //TODO: status text also contain timer
                recordButton.setOnClickListener(stopRecordListener);
                waveUUID = recorder.startRecording();
                recording = true;
                handler.postDelayed(run, 20000);

            }


        }
    };


    private OnClickListener rerecordSoundListener = new OnClickListener() {

        public void onClick(View arg0) {
            dialogRerecordSound.dismiss();
            recordButton.setBackgroundResource(R.drawable.stop);
            disablePlayButton();
            saveSoundButton.setClickable(false);
            deleteSoundButton.setClickable(false);
            //TODO: status text also contain timer
            recordButton.setOnClickListener(stopRecordListener);
            waveUUID = recorder.startRecording();
            recording = true;
            recordedNewSound = true;
            handler.postDelayed(run, 20000);
        }
    };

    //Action when User clicks stops while recording
    private OnClickListener stopRecordListener = new OnClickListener() {

        public void onClick(View arg0) {
            stopRecording();
        }
    };

    public void stopRecording() {
        handler.removeCallbacks(run);
        recordButton.setBackgroundResource(R.drawable.record);
        recordButton.setOnClickListener(recordSoundListener);
        enablePlayButton();
        saveSoundButton.setClickable(true);
        recorder.stopRecording();
        recording = false;
    }

    public void verifyRecordAgain() {
        dialogRerecordSound = new Dialog(this);
        dialogRerecordSound.setContentView(R.layout.sound_overwrite);
        dialogRerecordSound.setTitle("Record over byte?");
        dialogRerecordSound.setCancelable(true);

        Button buttonCancel = (Button) dialogRerecordSound.findViewById(R.id.cancelSoundOverwriteButton);
        buttonCancel.setOnClickListener(cancelButtonRerecordSoundListener);

        Button buttonRerecord = (Button) dialogRerecordSound.findViewById(R.id.overwriteSoundButton);
        buttonRerecord.setOnClickListener(rerecordSoundListener);

        dialogRerecordSound.show();
    }

    private OnClickListener playSoundListener = new OnClickListener() {

        public void onClick(View arg0) {
            try {
                playSound(waveUUID);

                //TODO: Timer for playing sound_create in status
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(SoundListActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    private OnClickListener cancelSoundListener = new OnClickListener() {

        public void onClick(View arg0) {
            if (recording) {
                recorder.stopRecording();
                handler.removeCallbacks(run);
            }

            editing = false;
            recording = false;
            killKeyboard();
            setUpSoundboard();
        }
    };

    private OnClickListener saveSoundListener = new OnClickListener() {

        public void onClick(View arg0) {

            EditText temp = (EditText) findViewById(R.id.soundNameTextBox);

            if (waveUUID.isEmpty() && temp.getText().length() == 0) {
                showToastMessage("You forgot to byteThat!");
            } else if (waveUUID.isEmpty()) {
                showToastMessage("You forgot to byteThat!");
            } else if (temp.getText().length() == 0) {
                showToastMessage("Add a byteName");
            } else {
                killKeyboard();
                if (editing) {
                    updateSound();
                } else {
                    saveNewSound();
                }
                imageAdapter.notifyDataSetChanged();
                showToastMessage("byte Saved");
                setUpSoundboard();
                change++;
            }
        }
    };

    private OnClickListener deleteSoundListener = new OnClickListener() {

        public void onClick(View arg0) {
            dialogDeleteSound.dismiss();
            deleteSound();
            imageAdapter.notifyDataSetChanged();
            showToastMessage("byte Deleted");
            setUpSoundboard();
            change++;
        }
    };
    private OnClickListener confirmDeleteSoundListener = new OnClickListener() {

        public void onClick(View arg0) {
            confirmDeleteSound();
        }
    };

    private OnClickListener cancelButtonRerecordSoundListener = new OnClickListener() {

        public void onClick(View arg0) {
            //dismiss dialog
            dialogRerecordSound.dismiss();
        }
    };
    private OnClickListener cancelButtonDeleteSoundListener = new OnClickListener() {

        public void onClick(View arg0) {
            //dismiss dialog
            dialogDeleteSound.dismiss();
        }
    };

    public void editingText() {
        this.editingText = true;
    }

    public void deleteSound() {
        String soundId = soundList.getSoundList().get(numWorkingSound - 1).getSoundId();
        this.context.deleteFile(soundId + ".wav");
        db.deleteSound(soundId);
        this.soundList = this.currentSoundBoard.getSoundList();
    }

    public void saveNewSound() {
        EditText temp = (EditText) findViewById(R.id.soundNameTextBox);
        SoundType newSound = util.create(temp.getText().toString());
        newSound.setSoundId(util.generateUUID());
        newSound.setWaveId(waveUUID);
        newSound.setSoundboardId(this.currentSoundBoardId);
        db.createSound(newSound);
        this.soundList.getSoundList().add(newSound);
    }

    public void updateSound() {

        EditText temp = (EditText) findViewById(R.id.soundNameTextBox);
        this.workingSound.setSoundName(temp.getText().toString());

        if (recordedNewSound) {
            String soundId = this.workingSound.getSoundId();
            File file = new File(this.getApplicationContext().getFilesDir() + soundId + ".wav");
            final boolean delete = file.delete();

        }
        recordedNewSound = false;
        this.workingSound.setWaveId(waveUUID);
        db.updateSound(this.workingSound);
        this.currentSoundBoard = db.getSoundBoardById(this.currentSoundBoardId);
        this.soundList = this.currentSoundBoard.getSoundList();
    }

    public void playSound(String uuid) {
        recorder.startPlaying(uuid);
    }

    public void editSoundBoard() {

        dialogEditSoundBoard = new Dialog(this);
        dialogEditSoundBoard.setContentView(R.layout.soundboard_edit);
        dialogEditSoundBoard.setTitle("Edit board");
        dialogEditSoundBoard.setCancelable(true);

        ImageView   photoIcon = (ImageView) dialogEditSoundBoard.findViewById(R.id.photoChooseIcon);
        photoIcon.setImageResource(R.drawable.ic_photo_camera_white_48dp);
        photoIcon.setOnClickListener(this.editPhotoListener);

        EditText nameBox = (EditText) dialogEditSoundBoard.findViewById(R.id.textfieldcreatesoundboardname);
        nameBox.setText(this.currentSoundBoard.getSoundBoardName());

        Button buttonOk = (Button) dialogEditSoundBoard.findViewById(R.id.saveeditSoundBoardButton);
        buttonOk.setOnClickListener(saveEditSoundBoardListener);

        Button buttonCancel = (Button) dialogEditSoundBoard.findViewById(R.id.canceleditSoundBoardButton);
        buttonCancel.setOnClickListener(cancelbuttonEditSoundBoardListener);

        Button buttonDelete = (Button) dialogEditSoundBoard.findViewById(R.id.deleteSoundBoardButton);
        buttonDelete.setOnClickListener(confirmDeleteSoundBoardListener);

        dialogEditSoundBoard.show();

    }


    //The Listener for the ok edit sound_create board dialog
    public OnClickListener saveEditSoundBoardListener = new OnClickListener() {

        public void onClick(View arg0) {

            EditText nameBox = (EditText) dialogEditSoundBoard.findViewById(R.id.textfieldcreatesoundboardname);
            String newname = nameBox.getText().toString();
            SoundBoardType newSoundBoard =  currentSoundBoard;
            // this.currentSoundBoard.setSoundBoardPhotoLocation(filepath);
            //   db.updateSoundboard(this.currentSoundBoard);
            newSoundBoard.setSoundBoardName(newname);
            newSoundBoard.setSoundBoardPhotoLocation(filepathTempPhoto);
            filepathTempPhoto = "";
            db.updateSoundboard(newSoundBoard);
            dialogEditSoundBoard.dismiss();
            showToastMessage("board Updated");
            setUpSoundboard();
        }
    };
    //The Listener for the cancel edit sound_create board dialog
    private OnClickListener cancelbuttonEditSoundBoardListener = new OnClickListener() {

        public void onClick(View arg0) {
            //dismiss dialog
            dialogEditSoundBoard.dismiss();
        }
    };
    private OnClickListener confirmDeleteSoundBoardListener = new OnClickListener() {

        public void onClick(View arg0) {
            confirmDeleteAlert();
        }
    };
    private OnClickListener cancelButtonDeleteSoundBoardListener = new OnClickListener() {

        public void onClick(View arg0) {
            //dismiss dialog
            dialogDeleteSoundBoard.dismiss();
        }
    };
    public void confirmDeleteAlert() {

        dialogDeleteSoundBoard = new Dialog(this);
        dialogDeleteSoundBoard.setContentView(R.layout.soundboard_delete);
        dialogDeleteSoundBoard.setTitle("Delete board & all bytes?");
        dialogDeleteSoundBoard.setCancelable(true);

        Button buttonCancel = (Button) dialogDeleteSoundBoard.findViewById(R.id.cancelSoundBoardDeleteButton);
        buttonCancel.setOnClickListener(cancelButtonDeleteSoundBoardListener);

        Button buttonDelete = (Button) dialogDeleteSoundBoard.findViewById(R.id.deleteSoundBoardButton);
        buttonDelete.setOnClickListener(deleteButtonSoundBoardListener);

        dialogDeleteSoundBoard.show();
    }

    private OnClickListener deleteButtonSoundBoardListener = new OnClickListener() {

        public void onClick(View arg0) {
            dialogDeleteSoundBoard.dismiss();
            util.deleteSoundFilesInSoundBoard(currentSoundBoard);

       //     soundBoardNames.remove(currentWorkingSoundBoard);
      //      soundBoardListAdapter.notifyDataSetChanged();
            //dismiss dialog
            dialogEditSoundBoard.dismiss();
            db.deleteSoundboard(currentSoundBoard.getSoundBoardId());
            finish();
        //    soundBoardList = db.getAllSoundboards();
        }
    };


    public void showToastMessage(CharSequence message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

}
