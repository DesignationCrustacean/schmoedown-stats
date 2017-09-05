package com.crustacean.designation.schmoedownstats.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.crustacean.designation.schmoedownstats.data.CompetitorContract.*;


/**
 * Created by Humz on 05/07/2017.
 */
public class CompetitorsDbHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "Competitors.db";
    private static final int DATABASE_VERSION = 1;

    public CompetitorsDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_COMPETITORS_TABLE = "CREATE TABLE " + CompetitorsEntry.TABLE_NAME +
                " (" +
                CompetitorsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CompetitorsEntry.COLUMN_FIRST_NAME + " TEXT, " +
                CompetitorsEntry.COLUMN_SURNAME + " TEXT, " +
                CompetitorsEntry.COLUMN_NICKNAME + " TEXT UNIQUE, " +
                CompetitorsEntry.COLUMN_WINS + " INTEGER NOT NULL, " +
                CompetitorsEntry.COLUMN_LOSSES + " INTEGER NOT NULL, " +
                CompetitorsEntry.COLUMN_KOS + " INTEGER NOT NULL, " +
                CompetitorsEntry.COLUMN_RANK + " INTEGER NOT NULL, " +
                CompetitorsEntry.COLUMN_CURRENT_BELTS + " TEXT, " +
                CompetitorsEntry.COLUMN_PAST_BELTS + " TEXT, " +
                CompetitorsEntry.COLUMN_CURRENT_TEAMS + " TEXT, " +
                CompetitorsEntry.COLUMN_PAST_TEAMS + " TEXT, " +
                CompetitorsEntry.COLUMN_CURRENT_FACTIONS + " TEXT, " +
                CompetitorsEntry.COLUMN_PAST_FACTIONS + " TEXT, " +
                CompetitorsEntry.COLUMN_INNERGEEKDOM + " BOOLEAN NOT NULL, " +
                CompetitorsEntry.COLUMN_GEEK_WINS + " INTEGER NOT NULL, " +
                CompetitorsEntry.COLUMN_GEEK_LOSSES + " INTEGER NOT NULL, " +
                CompetitorsEntry.COLUMN_GEEK_KNOCKOUTS + " INTEGER NOT NULL, " +
                CompetitorsEntry.COLUMN_GEEK_RANK + " INTEGER NOT NULL, " +
                CompetitorsEntry.COLUMN_STARWARS + " BOOLEAN NOT NULL, " +
                CompetitorsEntry.COLUMN_STARWARS_WINS + " INTEGER NOT NULL, " +
                CompetitorsEntry.COLUMN_STARWARS_LOSSES + " INTEGER NOT NULL, " +
                CompetitorsEntry.COLUMN_STARWARS_KNOCKOUTS + " INTEGER NOT NULL, " +
                CompetitorsEntry.COLUMN_CELEBRITY + " BOOLEAN NOT NULL, " +
                CompetitorsEntry.COLUMN_ACTIVE + " BOOLEAN NOT NULL, " +
                CompetitorsEntry.COLUMN_IMAGE_NAME + " TEXT NOT NULL " +
                "); ";

        db.execSQL(SQL_CREATE_COMPETITORS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + CompetitorsEntry.TABLE_NAME);
        onCreate(db);
    }

}
