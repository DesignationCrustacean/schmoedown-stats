package com.crustacean.designation.schmoedownstats.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.crustacean.designation.schmoedownstats.data.TeamContract.TeamsEntry;


/**
 * Created by Humz on 05/07/2017.
 */
public class TeamDbHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "Teams.db";
    private static final int DATABASE_VERSION = 1;

    public TeamDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_TEAMS_TABLE = "CREATE TABLE " + TeamsEntry.TABLE_NAME +
                " (" +
                TeamsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TeamsEntry.COLUMN_NAME + " TEXT, " +
                TeamsEntry.COLUMN_WINS + " INTEGER NOT NULL, " +
                TeamsEntry.COLUMN_LOSSES + " INTEGER NOT NULL, " +
                TeamsEntry.COLUMN_KOS + " INTEGER NOT NULL, " +
                TeamsEntry.COLUMN_RANK + " INTEGER NOT NULL, " +
                TeamsEntry.COLUMN_CURRENT_BELTS + " TEXT, " +
                TeamsEntry.COLUMN_PAST_BELTS + " TEXT, " +
                TeamsEntry.COLUMN_CURRENT_MEMBERS + " TEXT, " +
                TeamsEntry.COLUMN_PAST_MEMBERS + " TEXT, " +
                TeamsEntry.COLUMN_ACTIVE + " BOOLEAN NOT NULL, " +
                TeamsEntry.COLUMN_STARWARS + " BOOLEAN NOT NULL, " +
                TeamsEntry.COLUMN_STARWARS_WINS + " INTEGER NOT NULL, " +
                TeamsEntry.COLUMN_STARWARS_LOSSES + " INTEGER NOT NULL, " +
                TeamsEntry.COLUMN_STARWARS_KNOCKOUTS + " INTEGER NOT NULL, " +
                TeamsEntry.COLUMN_CELEBRITY + " BOOLEAN NOT NULL, " +
                TeamsEntry.COLUMN_IMAGE_NAME + " TEXT NOT NULL " +
                "); ";

        db.execSQL(SQL_CREATE_TEAMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TeamsEntry.TABLE_NAME);
        onCreate(db);
    }
}
