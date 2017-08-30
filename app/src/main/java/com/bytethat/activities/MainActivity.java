package com.bytethat.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.bytethat.R;
import com.bytethat.database.Utilities;
import com.bytethat.database.DatabaseHelper;
import com.bytethat.datamodel.SoundBoardListType;
import com.bytethat.datamodel.SoundBoardType;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * @author Tom
 *         <p>
 *         Copyright 2017 Thomas Cashavelly
 *         All Rights Reserved
 */

public class MainActivity extends Activity {

    DatabaseHelper db;
    private AdView mAdView;
    static Utilities util = null;
    SoundBoardListType soundBoardList = null;
    ArrayList<String> soundBoardNames = null;
    ArrayAdapter<String> soundBoardListAdapter = null;
    String newSoundBoardName = null;
    EditText sbName = null;
    Dialog dialogDeleteAll = null;
    Dialog dialogCreateSoundBoard = null;
    private InterstitialAd mInterstital;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        mInterstital = new InterstitialAd(this);
        mInterstital.setAdUnitId("ca-app-pub-3057749752423169/5037232130");
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstital.loadAd(adRequest);
        db = new DatabaseHelper(getApplicationContext());
        util = new Utilities(this.getApplicationContext());

        super.onCreate(savedInstanceState);

        setSoundBoardLayout();
    }

    //sets the layout of the list of soundboards
    public void setSoundBoardLayout() {

        soundBoardList = new SoundBoardListType();
        soundBoardList = db.getAllSoundboards();
        soundBoardNames = new ArrayList();

        for (int i = 0; i < soundBoardList.getSoundBoardList().size(); i++) {
            soundBoardNames.add(soundBoardList.getSoundBoardList().get(i).getSoundBoardName());
        }

        soundBoardNames.add("+");
        setContentView(R.layout.main_grid);
        GridView gridView = (GridView) findViewById(R.id.soundboardgrid);

        soundBoardListAdapter = new ArrayAdapter<String>(this, R.layout.main_sbrowtext, soundBoardNames);
        gridView.setAdapter(soundBoardListAdapter);
        gridView.setOnItemClickListener(openSoundBoardListener);
        gridView.setOnItemLongClickListener(editSoundBoardListener);
        gridView.setBackgroundColor(Color.BLACK);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.getSelector().setAlpha(100);
       // if(mInterstital.isLoaded()){
            mInterstital.show();
      //  }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Dialog to create a new Sound Board
     * 
     */
    //Get to the screen to enter the name of a new soundboard

    public void createNewSoundBoard() {

        dialogCreateSoundBoard = new Dialog(this);
        dialogCreateSoundBoard.setContentView(R.layout.soundboard_create);
        dialogCreateSoundBoard.setTitle("Create board");
        dialogCreateSoundBoard.setCancelable(true);


        sbName = (EditText) dialogCreateSoundBoard.findViewById(R.id.textfieldcreatesoundboardname);
        sbName.setHint("Name");
        Button createOkButton = (Button) dialogCreateSoundBoard.findViewById(R.id.createSoundBoardButton);
        createOkButton.setOnClickListener(createSoundBoardListener);

        Button createCancelButton = (Button) dialogCreateSoundBoard.findViewById(R.id.cancelSoundBoardButton);
        createCancelButton.setOnClickListener(cancelButtonCreateSoundBoardListener);

        dialogCreateSoundBoard.show();
    }

    /*
     * The listener to create a new soundboard of the OK button
     */
    private OnClickListener createSoundBoardListener = new OnClickListener() {

        public void onClick(View arg0) {

            boolean exists = false;
            newSoundBoardName = sbName.getText().toString();

            if (newSoundBoardName.contentEquals("")) {
                showToastMessage("Name that board!");
            } else {
                if (util.checkForNameInSoundBoardList(newSoundBoardName, soundBoardList)) {
                    exists = true;
                }
                SoundBoardType createdSoundBoard = util.createSoundBoard(newSoundBoardName);
                createdSoundBoard.setSoundBoardPhotoLocation("none");
                soundBoardList.getSoundBoardList().add(0, createdSoundBoard);
                db.createSoundboard(createdSoundBoard);
                soundBoardNames.add(0, newSoundBoardName);
                dialogCreateSoundBoard.dismiss();
                setSoundBoardLayout();
                showToastMessage("board Created");

                if (exists && createdSoundBoard.getSoundBoardPhotoLocation().contentEquals("none")) {
                    showToastMessage("Duplicate board Name");
                } else if (exists) {
                    showToastMessage("Duplicate board Name");
                }

            }
        }
    };

    /*
     * Cancel Button Listener
     */
    private OnClickListener cancelButtonCreateSoundBoardListener = new OnClickListener() {

        public void onClick(View arg0) {
            dialogCreateSoundBoard.dismiss();
        }
    };
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Open Sound Board
     */
    private OnItemClickListener openSoundBoardListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            int numOfNames = soundBoardNames.size();


            if (arg2 == numOfNames - 1) {
                createNewSoundBoard();
            } else {
                //Reload sounds
                setSoundLayout(arg2);
            }
        }
    };
    /*
     * Edit Sound Board
     */
    private OnItemLongClickListener editSoundBoardListener = new OnItemLongClickListener() {

        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            if (arg2 != soundBoardNames.size() - 1) {

            } else {
                confirmDeleteAllAlert();
            }
            return true;
        }
    };

    public void confirmDeleteAllAlert() {

        dialogDeleteAll = new Dialog(this);
        dialogDeleteAll.setContentView(R.layout.soundboard_deleteall);
        dialogDeleteAll.setTitle("Delete Everything, Are you Sure? Seriously? ");
        dialogDeleteAll.setCancelable(true);

        Button buttonCancel = (Button) dialogDeleteAll.findViewById(R.id.cancelAllDeleteButton);
        buttonCancel.setOnClickListener(cancelButtonDeleteAllListener);
        Button buttonDelete = (Button) dialogDeleteAll.findViewById(R.id.deleteAllSoundBoardButton);
        buttonDelete.setOnClickListener(deleteEverythingListener);

        dialogDeleteAll.show();
    }

    private OnClickListener deleteEverythingListener = new OnClickListener() {

        public void onClick(View arg0) {
            dialogDeleteAll.dismiss();
            util.deleteAllSounds();
            soundBoardNames = new ArrayList<String>();
            soundBoardNames.add("+");
            soundBoardListAdapter.notifyDataSetChanged();
            db.resetDB();
            soundBoardList = db.getAllSoundboards();
            setSoundBoardLayout();
            showToastMessage("All boards & bytes Deleted");
        }
    };

    private OnClickListener cancelButtonDeleteAllListener = new OnClickListener() {

        public void onClick(View arg0) {
            //dismiss dialog
            dialogDeleteAll.dismiss();
        }
    };



    public void showToastMessage(CharSequence message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //sets the layout of the list of sounds
    public void setSoundLayout(int sb) {

        //call to new activity

        Intent i = new Intent(getApplicationContext(), SoundListActivity.class);
        System.out.println(getApplicationContext().getFilesDir().toString());
        i.putExtra("SoundboardId", soundBoardList.getSoundBoardList().get(sb).getSoundBoardId());
        i.putExtra("CurrentSB", sb);
        i.putExtra("Util", this.util);
        startActivityForResult(i, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setSoundBoardLayout();

    }
}
