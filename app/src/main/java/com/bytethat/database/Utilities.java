/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytethat.database;

import android.content.Context;

import com.bytethat.datamodel.SoundBoardListType;
import com.bytethat.datamodel.SoundBoardType;
import com.bytethat.datamodel.SoundListType;
import com.bytethat.datamodel.SoundType;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Tom
 *
 * Copyright 2017 Thomas Cashavelly
 * All Rights Reserved
 */
public class Utilities implements Serializable {

    static Context context = null;

    public Utilities(){

    }

    public Utilities(Context context) {

        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String generateUUID() {
        UUID id = UUID.randomUUID();
        String uuidValue = id.toString();
        return uuidValue;
    }

    public void deleteAllSounds() {
        String[] fileList = this.context.fileList();
        List<String> files = Arrays.asList(fileList);

        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).toString().contains(".wav")) {
                String fileToDelete = files.get(i).toString();
           //     File file = new File("/data/data/com.android/files/" + fileToDelete);
                File file = new File(this.getContext().getFilesDir() + fileToDelete);
                final boolean delete = file.delete();
            }
        }
    }

    public void deleteSoundFilesInSoundBoard(SoundBoardType soundBoard) {
        SoundListType soundList = soundBoard.getSoundList();

        if (soundList != null) {
            for (SoundType sound : soundList.getSoundList()) {
                String uuid = sound.getSoundId();
            //    File file = new File("/data/data/com.bytethat/files/" + uuid + ".wav");
                File file = new File(this.getContext().getFilesDir() + uuid + ".wav");
                final boolean delete = file.delete();
            }
        }

    }

    // create a new soundboard
    public SoundBoardType createSoundBoard(String newSoundBoardName) {

        SoundBoardType newSoundBoard = new SoundBoardType(newSoundBoardName);

        Utilities util = new Utilities();
        newSoundBoard.setSoundBoardId(util.generateUUID());

        SoundListType newSoundList = new SoundListType();
        List<SoundType> newList = new ArrayList<SoundType>();
        SoundType create = createStart("byteThat");

        newList.add(create);
        newSoundList.setSoundListType(newList);
        newSoundBoard.setSoundList(newSoundList);

        return newSoundBoard;
    }

    //check for existing name of a soundboard in the list
    public boolean checkForNameInSoundBoardList(String name, SoundBoardListType soundBoardList) {

        for (SoundBoardType nSoundBoard : soundBoardList.getSoundBoardList()) {
            if (nSoundBoard.getSoundBoardName().contentEquals(name)) {
                return true;
            }
        }
        return false;
    }

    public SoundType create(String newSoundName) {
        SoundType newSound = new SoundType(newSoundName);
        return newSound;
    }

    public SoundType createStart(String newSoundName) {
        SoundType newSound = new SoundType(newSoundName);
        Utilities util = new Utilities();
        newSound.setSoundId(util.generateUUID());

        return newSound;
    }

}
