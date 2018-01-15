package com.bytethat;

import android.content.Context;

import com.bytethat.database.Utilities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilitiesUnitTest {

    String name = "Tom";

    @Test
    public void addition_isCorrect() throws Exception {



        assertEquals(4, 2 + 2);
    }

    @Test
    public void uuidCheck(){

        Utilities util = new Utilities();
        assertNull(util.getContext());
    }

    @Test
    public void createSoundType(){
        Utilities util = new Utilities();
        assertNotNull(util.create(name));
  //      assertEquals(SoundType, util.create(name));

    }
}