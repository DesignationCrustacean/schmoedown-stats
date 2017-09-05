package com.crustacean.designation.schmoedownstats.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crustacean.designation.schmoedownstats.data.TeamContract.TeamsEntry;

/**
 * Created by Humz on 06/07/2017.
 */

public class TeamContentProvider extends ContentProvider
{
    public static final String UNKNOWN_URI = "Unknown uri: ";
    private TeamDbHelper dbHelper;

    private static final int TEAMS = 200;
    private static final int TEAMS_WITH_NAME = 202;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher()
    {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(TeamContract.AUTHORITY, TeamContract.PATH_TEAMS, TEAMS);
        matcher.addURI(TeamContract.AUTHORITY, TeamContract.PATH_TEAMS + "/*", TEAMS_WITH_NAME);
        return matcher;
    }

    @Override
    public boolean onCreate()
    {
        dbHelper = new TeamDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);

        Cursor cursor;
        switch (match)
        {
            case TEAMS:
                cursor = db.query(
                        TeamsEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case TEAMS_WITH_NAME:

                String id = uri.getPathSegments().get(1);

                cursor = db.query(
                        TeamsEntry.TABLE_NAME,
                        null,
                        TeamsEntry.COLUMN_NAME + "=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        Uri returnUri = null;

        switch (match)
        {
            case TEAMS:
                Long id = db.insert(TeamsEntry.TABLE_NAME, null, values);
                if(id > 0)
                {
                    returnUri = ContentUris.withAppendedId(TeamsEntry.CONTENT_URI, id);
                }
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        return 0;
    }
}
