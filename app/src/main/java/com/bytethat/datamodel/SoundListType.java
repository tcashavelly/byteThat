/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytethat.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TCASHAVELLY
 */
public class SoundListType implements Serializable{
    
     List<SoundType> soundListType;

    public SoundListType(){
        soundListType = new ArrayList<SoundType>();
    }
     
     public List<SoundType> getSoundList() {
        if (soundListType == null) {
            soundListType = new ArrayList<SoundType>();
        }
        return this.soundListType;
    }

    public void setSoundListType(List<SoundType> soundListType) {
        this.soundListType = soundListType;
    }
     
     
    
}