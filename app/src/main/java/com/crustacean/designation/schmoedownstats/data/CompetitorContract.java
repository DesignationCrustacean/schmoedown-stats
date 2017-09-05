package com.crustacean.designation.schmoedownstats.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Humz on 16/06/2017.
 */
public class CompetitorContract
{
    static final String AUTHORITY = "com.crustacean.designation.schmoedownstats.competitor";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    static final String PATH_COMPETITORS = "competitors";

    public static final class CompetitorsEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMPETITORS).build();

        public static final String TABLE_NAME = "competitors";

        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_NICKNAME = "nickname";
        public static final String COLUMN_WINS = "wins";
        public static final String COLUMN_LOSSES = "losses";
        public static final String COLUMN_KOS = "kos";
        public static final String COLUMN_RANK = "rank";
        public static final String COLUMN_CURRENT_BELTS = "current_belts";
        public static final String COLUMN_PAST_BELTS = "past_belts";
        public static final String COLUMN_CURRENT_TEAMS = "current_teams";
        public static final String COLUMN_PAST_TEAMS = "past_teams";
        public static final String COLUMN_CURRENT_FACTIONS = "current_factions";
        public static final String COLUMN_PAST_FACTIONS = "past_factions";
        public static final String COLUMN_INNERGEEKDOM = "innergeekdom";
        public static final String COLUMN_GEEK_RANK = "geek_rank";
        public static final String COLUMN_GEEK_WINS = "geek_wins";
        public static final String COLUMN_GEEK_LOSSES = "geek_losses";
        public static final String COLUMN_GEEK_KNOCKOUTS = "geek_knockouts";
        public static final String COLUMN_STARWARS = "star_wars";
        public static final String COLUMN_STARWARS_WINS = "star_wars_wins";
        public static final String COLUMN_STARWARS_LOSSES = "star_wars_losses";
        public static final String COLUMN_STARWARS_KNOCKOUTS = "star_wars_knockouts";
        public static final String COLUMN_CELEBRITY = "celebrity";
        public static final String COLUMN_ACTIVE = "active";
        public static final String COLUMN_IMAGE_NAME = "image_name";
    }
}
