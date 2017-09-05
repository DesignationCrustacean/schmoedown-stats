package com.crustacean.designation.schmoedownstats.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Humz on 16/06/2017.
 */
public class TeamContract
{
    static final String AUTHORITY = "com.crustacean.designation.schmoedownstats.team";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    static final String PATH_TEAMS = "teams";

    public static final class TeamsEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEAMS).build();

        public static final String TABLE_NAME = "teams";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_WINS = "wins";
        public static final String COLUMN_LOSSES = "losses";
        public static final String COLUMN_KOS = "kos";
        public static final String COLUMN_RANK = "rank";
        public static final String COLUMN_CURRENT_BELTS = "current_belts";
        public static final String COLUMN_PAST_BELTS = "past_belts";
        public static final String COLUMN_CURRENT_MEMBERS = "current_members";
        public static final String COLUMN_PAST_MEMBERS = "past_members";
        public static final String COLUMN_ACTIVE = "active";
        public static final String COLUMN_IMAGE_NAME = "image_name";
        public static final String COLUMN_STARWARS = "star_wars";
        public static final String COLUMN_STARWARS_WINS = "star_wars_wins";
        public static final String COLUMN_STARWARS_LOSSES = "star_wars_losses";
        public static final String COLUMN_STARWARS_KNOCKOUTS = "star_wars_knockouts";
        public static final String COLUMN_CELEBRITY = "celebrity";

    }
}
