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
public class SoundBoardListType implements Serializable{
    
     List<SoundBoardType> soundBoardListType;
     
     public List<SoundBoardType> getSoundBoardList() {
        if (soundBoardListType == null) {
            soundBoardListType = new ArrayList<SoundBoardType>();
        }
        return this.soundBoardListType;
    }
    
}