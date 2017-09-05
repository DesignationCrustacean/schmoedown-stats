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

import com.crustacean.designation.schmoedownstats.data.CompetitorContract.*;

/**
 * Created by Humz on 06/07/2017.
 */
public class CompetitorContentProvider extends ContentProvider
{
    public static final String UNKNOWN_URI = "Unknown uri: ";
    private CompetitorsDbHelper dbHelper;

    private static final int COMPETITORS = 100;
    private static final int COMPETITORS_WITH_NAME = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher()
    {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(CompetitorContract.AUTHORITY, CompetitorContract.PATH_COMPETITORS, COMPETITORS);
        matcher.addURI(CompetitorContract.AUTHORITY, CompetitorContract.PATH_COMPETITORS + "/*", COMPETITORS_WITH_NAME);
        return matcher;
    }

    @Override
    public boolean onCreate()
    {
        dbHelper = new CompetitorsDbHelper(getContext());
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
            case COMPETITORS:
                cursor = db.query(
                        CompetitorsEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case COMPETITORS_WITH_NAME:

                String id = uri.getPathSegments().get(1);

                cursor = db.query(
                        CompetitorsEntry.TABLE_NAME,
                        null,
                        CompetitorsEntry.COLUMN_NICKNAME + "=?",
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
            case COMPETITORS:
                Long id = db.insert(CompetitorsEntry.TABLE_NAME, null, values);
                if (id > 0)
                {
                    returnUri = ContentUris.withAppendedId(CompetitorsEntry.CONTENT_URI, id);
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
