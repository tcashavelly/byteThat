package com.bytethat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bytethat.datamodel.SoundBoardListType;
import com.bytethat.datamodel.SoundBoardType;
import com.bytethat.datamodel.SoundListType;
import com.bytethat.datamodel.SoundType;
import android.util.Log;

/**
 * Created by TCASHAVELLY on 2/28/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "bytemedb";

    // Table Names
    private static final String TABLE_SOUNDBOARD = "soundboard";
    private static final String TABLE_SOUND = "sound";

    // Common column names
    private static final String KEY_ID = "id";

    // Soundboard Table - column names
    private static final String KEY_SOUNDBOARD_NAME = "soundboard_name";
    private static final String KEY_PHOTO_LOCATION = "photo_location";

    // Sound Table - column names
    private static final String KEY_SOUND_NAME = "sound_name";
    private static final String KEY_SOUNDBOARD_ID = "soundboard_id";
    private static final String KEY_WAVE_ID = "wave_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_SOUNDBOARD);
        db.execSQL(CREATE_TABLE_SOUND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //todo need to update for next release

        // on upgrade drop older tables
        //  db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOUNDBOARD);
        //  db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOUND);

        // create new tables
        onCreate(db);
    }

    // Table Create Statements
    // Soundboard table create statement
    private static final String CREATE_TABLE_SOUNDBOARD = "CREATE TABLE "
            + TABLE_SOUNDBOARD + "(" + KEY_ID + " TEXT PRIMARY KEY," + KEY_SOUNDBOARD_NAME
            + " TEXT," + KEY_PHOTO_LOCATION + " TEXT" + ")";

    private static final String CREATE_TABLE_SOUND = "CREATE TABLE "
            + TABLE_SOUND + "(" + KEY_ID + " TEXT PRIMARY KEY," + KEY_SOUND_NAME
            + " TEXT," + KEY_SOUNDBOARD_ID + " TEXT," + KEY_WAVE_ID + " TEXT" + ")";

    /*
 * Creating a soundboard
 */
    public long createSoundboard(SoundBoardType soundboard) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, soundboard.getSoundBoardId());
        values.put(KEY_SOUNDBOARD_NAME, soundboard.getSoundBoardName());
        values.put(KEY_PHOTO_LOCATION, soundboard.getSoundBoardPhotoLocation());

        long insert = db.insert(TABLE_SOUNDBOARD, null, values);
        return insert;

    }

    public long createSound(SoundType sound) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, sound.getSoundId());
        values.put(KEY_SOUNDBOARD_ID, sound.getSoundboardId());
        values.put(KEY_SOUND_NAME, sound.getSoundName());
        values.put(KEY_WAVE_ID, sound.getWaveId());

        long insert = db.insert(TABLE_SOUND, null, values);
        return insert;
    }

    /*
 * getting all soundboards
 * */
    public SoundBoardListType getAllSoundboards() {
        SoundBoardListType soundboardList = new SoundBoardListType();
        String selectQuery = "SELECT * FROM " + TABLE_SOUNDBOARD;

        Log.i(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                SoundBoardType sb = new SoundBoardType();
                sb.setSoundBoardId(c.getString(c.getColumnIndex(KEY_ID)));
                sb.setSoundBoardName(c.getString(c.getColumnIndex(KEY_SOUNDBOARD_NAME)));
                sb.setSoundBoardPhotoLocation(c.getString(c.getColumnIndex(KEY_PHOTO_LOCATION)));
                soundboardList.getSoundBoardList().add(0, sb);

            } while (c.moveToNext());
        }

        return soundboardList;
    }


    /*
 * get soundboard by id
 * */
    public SoundBoardType getSoundBoardById(String soundBoardId) {
        SoundBoardType soundboard = new SoundBoardType();
        String selectQuery = "SELECT * FROM " + TABLE_SOUNDBOARD + " WHERE " + KEY_ID + " = '" + soundBoardId + "'";

        Log.i(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        soundboard.setSoundBoardId(c.getString(c.getColumnIndex(KEY_ID)));
        soundboard.setSoundBoardName(c.getString(c.getColumnIndex(KEY_SOUNDBOARD_NAME)));
        soundboard.setSoundBoardPhotoLocation(c.getString(c.getColumnIndex(KEY_PHOTO_LOCATION)));

        return soundboard;
    }

    /*
 * getting all Sounds under soundboard
 * */
    public SoundListType getSoundsBySoundboardId(String soundBoardId) {
        SQLiteDatabase db = this.getReadableDatabase();
        SoundListType soundList = new SoundListType();

        String selectQuery = "SELECT * FROM " + TABLE_SOUND + " WHERE " + KEY_SOUNDBOARD_ID + " = '" + soundBoardId + "'";
        Log.i(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                SoundType sound = new SoundType();
                sound.setSoundId(c.getString(c.getColumnIndex(KEY_ID)));
                sound.setSoundboardId(c.getString(c.getColumnIndex(KEY_SOUNDBOARD_ID)));
                sound.setSoundName(c.getString(c.getColumnIndex(KEY_SOUND_NAME)));
                sound.setWaveId(c.getString(c.getColumnIndex(KEY_WAVE_ID)));
                soundList.getSoundList().add(0, sound);
            } while (c.moveToNext());
        }

        return soundList;
    }

    /*
* getting all soundboards
* */
    public boolean soundboardExists() {
        SoundBoardListType soundboardList = new SoundBoardListType();
        String selectQuery = "SELECT * FROM " + TABLE_SOUNDBOARD;
        boolean value;
        Log.i(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() > 0){
            value = true;
        }else{
            value = false;
        }
        return value;
    }

    /*
 * Updating a soundboard
 */
    public int updateSoundboard(SoundBoardType soundboard) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if(soundboard.getSoundBoardName() != null){
            values.put(KEY_SOUNDBOARD_NAME, soundboard.getSoundBoardName());
        }
        if(soundboard.getSoundBoardPhotoLocation() != null){
            values.put(KEY_PHOTO_LOCATION, soundboard.getSoundBoardPhotoLocation());
        }


        // updating row
        return db.update(TABLE_SOUNDBOARD, values, KEY_ID + " = ?",
                new String[]{String.valueOf(soundboard.getSoundBoardId())});
    }

    /*
* Updating a sound
*/
    public int updateSound(SoundType sound) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SOUND_NAME, sound.getSoundName());
        values.put(KEY_WAVE_ID, sound.getWaveId());

        // updating row
        return db.update(TABLE_SOUND, values, KEY_ID + " = ?",
                new String[]{String.valueOf(sound.getSoundId())});
    }

    /*
 * Deleting a soundboard
 */
    public void deleteSoundboard(String soundboardId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SOUNDBOARD, KEY_ID + " = ?",
                new String[]{String.valueOf(soundboardId)});
    }

    /*
* Deleting a sound
*/
    public void deleteSound(String soundId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SOUND, KEY_ID + " = ?",
                new String[]{String.valueOf(soundId)});
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public void resetDB() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOUNDBOARD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOUND);
        db.execSQL(CREATE_TABLE_SOUNDBOARD);
        db.execSQL(CREATE_TABLE_SOUND);
    }

}
