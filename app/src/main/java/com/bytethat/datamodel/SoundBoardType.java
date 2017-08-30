/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytethat.datamodel;

import java.io.Serializable;

/**
 *
 * @author TCASHAVELLY
 */
public class SoundBoardType implements Serializable {

    private String soundBoardName;
    private String soundBoardId;
    private String soundBoardPhotoLocation;
    private SoundListType soundList;

    public SoundBoardType(String soundBoardName) {
        this.soundBoardName = soundBoardName;
    }
    
    public SoundBoardType(){
        
    }

    public String getSoundBoardPhotoLocation() {
        return soundBoardPhotoLocation;
    }

    public void setSoundBoardPhotoLocation(String soundBoardPhotoLocation) {
        this.soundBoardPhotoLocation = soundBoardPhotoLocation;
    }

    public SoundListType getSoundList() {
        return soundList;
    }

    public void setSoundList(SoundListType soundList) {
        this.soundList = soundList;
    }
    
    public String getSoundBoardId() {
        return soundBoardId;
    }

    public void setSoundBoardId(String soundBoardId) {
        this.soundBoardId = soundBoardId;
    }

    public String getSoundBoardName() {
        return soundBoardName;
    }

    public void setSoundBoardName(String soundBoardName) {
        this.soundBoardName = soundBoardName;
    }
}