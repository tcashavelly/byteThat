/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytethat.datamodel;

import java.io.Serializable;

/**
 * @author TCASHAVELLY
 */
public class SoundType implements Serializable {

    private String soundName;
    private String soundId;
    private String soundboardId;
    private String waveId;

    public SoundType(String soundName) {
        this.soundName = soundName;
    }

    public SoundType() {

    }

    public String getSoundId() {

        return soundId;
    }

    public void setSoundId(String soundId) {

        this.soundId = soundId;
    }

    public String getSoundboardId() {
        return soundboardId;
    }

    public void setSoundboardId(String soundboardId) {

        this.soundboardId = soundboardId;
    }

    public String getSoundName() {

        return soundName;
    }

    public void setSoundName(String soundName) {

        this.soundName = soundName;
    }

    public String getWaveId() {
        return waveId;
    }

    public void setWaveId(String waveId) {
        this.waveId = waveId;
    }
}