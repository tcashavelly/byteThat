/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytethat.adaptors;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import com.bytethat.database.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tom
 *
 * Copyright 2017 Thomas Cashavelly
 * All Rights Reserved
 */
public class Recorder extends Activity {

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    String mFileName = null;
    Utilities util = new Utilities();
    static Context context = null;

    public Recorder() {
        setFileName();
    }

    public Recorder(Context context) {
        this.context = context;
    }

    public void setFileName() {
     //    mFileName = this.context.getFilesDir();
    //    mFileName =  "/data/data/com.bytethat/files/";
    }

    public String startRecording() {
        String uuid = util.generateUUID();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(this.context.getFilesDir() + uuid + ".wav");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IllegalStateException ex) {
            Logger.getLogger(Recorder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Recorder.class.getName()).log(Level.SEVERE, null, ex);
        }

        mRecorder.start();
        return uuid;
    }

    public void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public void startPlaying(String uuid) {
        FileInputStream fis = null;

        try {
            mPlayer = new MediaPlayer();
            File file = new File(this.context.getFilesDir() + uuid + ".wav");
            fis = new FileInputStream(file);
            try {
                mPlayer.setDataSource(fis.getFD());
                mPlayer.prepare();
                mPlayer.start();
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();

                    };
                });

            } catch (IOException ex) {
                Logger.getLogger(Recorder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Recorder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(Recorder.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(Recorder.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(Recorder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }


}
