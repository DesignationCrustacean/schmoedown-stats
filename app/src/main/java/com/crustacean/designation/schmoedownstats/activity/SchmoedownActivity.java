package com.crustacean.designation.schmoedownstats.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.crustacean.designation.schmoedownstats.R;
import com.crustacean.designation.schmoedownstats.adapter.SchmoedownAdapter;
import com.crustacean.designation.schmoedownstats.adapter.SchmoedownTeamAdapter;
import com.crustacean.designation.schmoedownstats.data.CompetitorContract;
import com.crustacean.designation.schmoedownstats.data.TeamContract;
import com.crustacean.designation.schmoedownstats.model.Competitor;
import com.crustacean.designation.schmoedownstats.model.Team;

import java.util.ArrayList;
import java.util.Arrays;

public class SchmoedownActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String COMPETITORS = "competitors";
    private static final String CELEBRITY_COMPETITORS = "celebrity_competitors";
    private static final String CELEBRITY_TEAMS = "celebrity_teams";
    private static final String TEAMS = "teams";
    private static final String STAR_WARS_COMPETITORS = "star_wars_competitors";
    private static final String STAR_WARS_TEAMS = "star_wars_teams";
    private static final String SCROLL_POSITION = "scroll_position";
    private static final String SINGLES = "singles";
    public static final String COMPETITION = "competition";
    public static final String COMMA = ",";
    private ArrayList<Competitor> mCompetitors = new ArrayList<>();
    private ArrayList<Team> mTeams = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private LinearLayoutManager mLayoutManager;
    private boolean touched;
    private boolean singles = true;
    private int scrollPosition;
    private String mCompetition;
    SchmoedownTeamAdapter mSchmoedownTeamAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schmoedown);
        ConstraintLayout mRootLayout;

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recyclerView);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
        mRootLayout = (ConstraintLayout) findViewById(R.id.cl_root);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        Intent intent = getIntent();

        if (intent.hasExtra(COMPETITION))
        {
            mCompetition = intent.getStringExtra(COMPETITION);
        }

        if (mCompetition.equals(getString(R.string.key_celebrity)))
        {
            getSupportActionBar().setTitle(getString(R.string.celeb_schmoedown));
            mRootLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.celebSchmoeBlue));
        }
        else if (mCompetition.equals(getString(R.string.key_schmoedown)))
        {
            getSupportActionBar().setTitle(getString(R.string.schmoedown));
            mRootLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.schmoedownGreen));
        }
        else if (mCompetition.equals(getString(R.string.key_star_wars)))
        {
            getSupportActionBar().setTitle(getString(R.string.star_wars));
            mRootLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.starWarsYellow));
        }

        if (intent.hasExtra(COMPETITORS) && intent.hasExtra(TEAMS)
                || intent.hasExtra(CELEBRITY_COMPETITORS) && intent.hasExtra(CELEBRITY_TEAMS)
                || intent.hasExtra(STAR_WARS_COMPETITORS) && intent.hasExtra(STAR_WARS_TEAMS))
        {
            if (mCompetition.equals(getString(R.string.key_celebrity)))
            {
                mCompetitors = (ArrayList) intent.getParcelableArrayListExtra(CELEBRITY_COMPETITORS);
                mTeams = (ArrayList) intent.getParcelableArrayListExtra(CELEBRITY_TEAMS);
            }
            else if (mCompetition.equals(getString(R.string.key_schmoedown)))
            {
                mCompetitors = (ArrayList) intent.getParcelableArrayListExtra(COMPETITORS);
                mTeams = (ArrayList) intent.getParcelableArrayListExtra(TEAMS);
            }
            else if (mCompetition.equals(getString(R.string.key_star_wars)))
            {
                mCompetitors = (ArrayList) intent.getParcelableArrayListExtra(STAR_WARS_COMPETITORS);
                mTeams = (ArrayList) intent.getParcelableArrayListExtra(STAR_WARS_TEAMS);
            }
        }
        else
        {
            if (savedInstanceState != null)
            {
                if (savedInstanceState.containsKey(COMPETITORS) && savedInstanceState.containsKey(TEAMS))
                {
                    mCompetitors = (ArrayList) savedInstanceState.getParcelableArrayList(COMPETITORS);
                    mTeams = (ArrayList) savedInstanceState.getParcelableArrayList(TEAMS);
                }
                else if (savedInstanceState.containsKey(CELEBRITY_COMPETITORS) && savedInstanceState.containsKey(CELEBRITY_TEAMS))
                {
                    mCompetitors = (ArrayList) savedInstanceState.getParcelableArrayList(CELEBRITY_COMPETITORS);
                    mTeams = (ArrayList) savedInstanceState.getParcelableArrayList(CELEBRITY_TEAMS);
                }
                else if (savedInstanceState.containsKey(STAR_WARS_COMPETITORS) && savedInstanceState.containsKey(STAR_WARS_TEAMS))
                {
                    mCompetitors = (ArrayList) savedInstanceState.getParcelableArrayList(STAR_WARS_COMPETITORS);
                    mTeams = (ArrayList) savedInstanceState.getParcelableArrayList(STAR_WARS_TEAMS);
                }
                else
                {
                    new GetCompetitorsFromDbTask().execute();
                    new GetTeamsFromDbTask().execute();
                }
                if (savedInstanceState.containsKey(SCROLL_POSITION) && savedInstanceState.getInt(SCROLL_POSITION) > 0)
                {
                    scrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
                }
                if (savedInstanceState.containsKey(SINGLES))
                {
                    singles = savedInstanceState.getBoolean(SINGLES);
                }
            }
            else
            {
                new GetTeamsFromDbTask().execute();
                new GetCompetitorsFromDbTask().execute();
            }
        }

        if (savedInstanceState != null)
        {
            if (savedInstanceState.containsKey(SCROLL_POSITION) && savedInstanceState.getInt(SCROLL_POSITION) > 0)
            {
                scrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
            }
            if (savedInstanceState.containsKey(SINGLES))
            {
                singles = savedInstanceState.getBoolean(SINGLES);
            }
        }
        populate(singles);
    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (!mCompetitors.isEmpty())
        {
            outState.putParcelableArrayList(COMPETITORS, (ArrayList) mCompetitors);
        }
        if (!mTeams.isEmpty())
        {
            outState.putParcelableArrayList(TEAMS, (ArrayList) mTeams);
        }
        outState.putBoolean(SINGLES, singles);
        outState.putInt(SCROLL_POSITION, mLayoutManager.findFirstCompletelyVisibleItemPosition());
    }

    @Override
    public void onClick(View v)
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_schmoedown, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array, R.layout.white_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnTouchListener(new AdapterView.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                touched = true;
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (touched)
                {
                    if (position == 0)
                    {
                        singles = true;
                    }
                    else if (position == 1)
                    {
                        singles = false;
                    }
                    if (mRecyclerView.getAdapter() != null)
                    {
                        populate(singles);
                    }
                }
                else
                {
                    if (singles)
                    {
                        spinner.setSelection(0);
                    }
                    else
                    {
                        spinner.setSelection(1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        touched = false;
        return true;
    }

    private void populate(boolean singles)
    {

        if (singles)
        {
            SchmoedownAdapter mSchmoedownAdapter = new SchmoedownAdapter((ArrayList) mCompetitors, mCompetition);
            mRecyclerView.setAdapter(mSchmoedownAdapter);
        }
        else
        {
            mSchmoedownTeamAdapter = new SchmoedownTeamAdapter((ArrayList) mTeams, mCompetition);
            mRecyclerView.setAdapter(mSchmoedownTeamAdapter);

        }
        mRecyclerView.getLayoutManager().scrollToPosition(scrollPosition);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private class GetCompetitorsFromDbTask extends AsyncTask<Context, Void, ArrayList<Competitor>>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Competitor> doInBackground(Context... params)
        {
            Cursor cursor = getApplicationContext().getContentResolver().query(
                    CompetitorContract.CompetitorsEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    CompetitorContract.CompetitorsEntry.COLUMN_RANK);

            ArrayList<Competitor> competitors = new ArrayList<>();

            for (int i = 0; i < cursor.getCount(); i++)
            {
                if (!cursor.moveToPosition(i))
                {
                    continue;
                }

                final int rank = cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_RANK));

                if (mCompetition.equals(getString(R.string.key_schmoedown)) && rank < 1)
                {
                    continue;
                }

                boolean isCelebrity = cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_CELEBRITY)) != 0;

                if (mCompetition.equals(getString(R.string.key_celebrity)) && !isCelebrity)
                {
                    continue;
                }

                boolean isStarWars = cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_STARWARS)) != 0;

                if (mCompetition.equals(getString(R.string.key_star_wars)) && !isStarWars)
                {
                    continue;
                }

                Competitor competitor = new Competitor();
                competitor.setFirstName(cursor.getString(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_FIRST_NAME)));
                competitor.setSurname(cursor.getString(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_SURNAME)));
                competitor.setNickname(cursor.getString(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_NICKNAME)));
                competitor.setRank(rank);
                competitor.setWins(cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_WINS)));
                competitor.setLosses(cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_LOSSES)));
                competitor.setKos(cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_KOS)));
                competitor.setCurrentBelts(Arrays.asList(cursor.getString(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_CURRENT_BELTS)).split(COMMA)));
                competitor.setPastBelts(Arrays.asList(cursor.getString(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_PAST_BELTS)).split(COMMA)));
                competitor.setCurrentTeams(Arrays.asList(cursor.getString(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_CURRENT_TEAMS)).split(COMMA)));
                competitor.setPastTeams(Arrays.asList(cursor.getString(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_PAST_TEAMS)).split(COMMA)));
                competitor.setCurrentFactions(Arrays.asList(cursor.getString(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_CURRENT_FACTIONS)).split(COMMA)));
                competitor.setPastFactions(Arrays.asList(cursor.getString(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_PAST_FACTIONS)).split(COMMA)));
                competitor.setActive(cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_ACTIVE)) != 0);
                competitor.setInnergeekdom(cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_INNERGEEKDOM)) != 0);
                competitor.setGeekRank(cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_GEEK_RANK)));
                competitor.setGeekWins(cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_GEEK_WINS)));
                competitor.setGeekLosses(cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_GEEK_LOSSES)));
                competitor.setGeekKnockouts(cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_GEEK_KNOCKOUTS)));
                competitor.setImageName(cursor.getString(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_IMAGE_NAME)));
                competitor.setStarWarsWins(cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_STARWARS_WINS)));
                competitor.setStarWarsLosses(cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_STARWARS_LOSSES)));
                competitor.setStarWarsKnockouts(cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_STARWARS_KNOCKOUTS)));
                competitor.setStarWars(cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_STARWARS)) != 0);
                competitor.setCelebrity(isCelebrity);

                competitors.add(competitor);
            }
            cursor.close();
            return competitors;
        }

        @Override
        protected void onPostExecute(ArrayList<Competitor> competitors)
        {
            super.onPostExecute(competitors);
            mCompetitors = competitors;
            mProgressBar.setVisibility(View.INVISIBLE);
            populate(singles);

        }
    }

    private class GetTeamsFromDbTask extends AsyncTask<Context, Void, ArrayList<Team>>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Team> doInBackground(Context... params)
        {
            Cursor cursor = getApplicationContext().getContentResolver().query(
                    TeamContract.TeamsEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    TeamContract.TeamsEntry.COLUMN_RANK);

            ArrayList<Team> teams = new ArrayList<>();

            for (int i = 0; i < cursor.getCount(); i++)
            {

                if (!cursor.moveToPosition(i))
                {
                    continue;
                }

                final int rank = cursor.getInt(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_RANK));

                if (mCompetition.equals(getString(R.string.key_schmoedown)) && rank < 1)
                {
                    continue;
                }

                boolean isCelebrity = cursor.getInt(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_CELEBRITY)) != 0;

                if (mCompetition.equals(getString(R.string.key_celebrity)) && !isCelebrity)
                {
                    continue;
                }

                boolean isStarWars = cursor.getInt(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_STARWARS)) != 0;

                if (mCompetition.equals(getString(R.string.key_star_wars)) && !isStarWars)
                {
                    continue;
                }

                Team team = new Team();
                team.setName(cursor.getString(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_NAME)));
                team.setRank(cursor.getInt(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_RANK)));
                team.setWins(cursor.getInt(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_WINS)));
                team.setLosses(cursor.getInt(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_LOSSES)));
                team.setKos(cursor.getInt(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_KOS)));
                team.setCurrentMembers(Arrays.asList(cursor.getString(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_CURRENT_MEMBERS)).split(COMMA)));
                team.setPastMembers(Arrays.asList(cursor.getString(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_PAST_MEMBERS)).split(COMMA)));
                team.setCurrentBelts(Arrays.asList(cursor.getString(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_CURRENT_BELTS)).split(COMMA)));
                team.setPastBelts(Arrays.asList(cursor.getString(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_PAST_BELTS)).split(COMMA)));
                team.setImageName(cursor.getString(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_IMAGE_NAME)));
                team.setActive(cursor.getInt(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_ACTIVE)) != 0);
                team.setStarWars(isStarWars);
                team.setStarWarsWins(cursor.getInt(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_STARWARS_WINS)));
                team.setStarWarsLosses(cursor.getInt(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_STARWARS_LOSSES)));
                team.setStarWarsKnockouts(cursor.getInt(cursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_STARWARS_KNOCKOUTS)));
                team.setCelebrity(isCelebrity);

                teams.add(team);
            }
            cursor.close();
            return teams;
        }

        @Override
        protected void onPostExecute(ArrayList<Team> teams)
        {
            super.onPostExecute(teams);
            mTeams = teams;
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}

