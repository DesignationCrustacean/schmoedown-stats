package com.crustacean.designation.schmoedownstats.activity;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crustacean.designation.schmoedownstats.R;
import com.crustacean.designation.schmoedownstats.adapter.SearchAdapter;
import com.crustacean.designation.schmoedownstats.data.CompetitorContract;
import com.crustacean.designation.schmoedownstats.data.CompetitorsDbHelper;
import com.crustacean.designation.schmoedownstats.data.TeamContract;
import com.crustacean.designation.schmoedownstats.data.TeamDbHelper;
import com.crustacean.designation.schmoedownstats.model.Competitor;
import com.crustacean.designation.schmoedownstats.model.Team;
import com.crustacean.designation.schmoedownstats.util.NetworkUtils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bundle>
{

    private static final int DATA_LOADER = 21;
    public static final String COMMA = ",";
    public static final String COMPETITORS = "competitors";
    public static final String TEAMS = "teams";
    public static final String ALL_COMPETITORS = "all_competitors";
    public static final String MEMBER = "member";
    public static final String BELT = "belt";
    public static final String FACTION = "faction";
    public static final String GEEK_COMPETITORS = "geek_competitors";
    public static final String STAR_WARS_COMPETITORS = "star_wars_competitors";
    public static final String STAR_WARS_TEAMS = "star_wars_teams";
    public static final String CELEBRITY_COMPETITORS = "celebrity_competitors";
    public static final String CELEBRITY_TEAMS = "celebrity_teams";
    public static final String MINOR_VERSION = "minor_version";
    public static final String PREFS = "PREFS";
    public static final String COMPETITION = "competition";
    public static final String WINS = "wins";
    public static final String LOSSES = "losses";
    public static final String RANK = "rank";
    public static final String ACTIVE = "active";
    public static final String STAR_WARS_WINS = "star_wars_wins";
    public static final String STAR_WARS_LOSSES = "star_wars_losses";
    public static final String STAR_WARS_KNOCKOUTS = "star_wars_knockouts";
    public static final String IMAGE_NAME = "image_name";
    public static final String EMPTY_STRING = "";
    private ImageView mSchmoedownButton, mInnerGeekdomButton, mCelebrityButton, mStarWarsButton;
    private ProgressBar mProgressBar;
    private Bundle mBundle;
    private ConstraintLayout mLoadingLayout;
    private boolean dataLoaded;
    private Integer remoteMajorVersion;
    private Integer remoteMinorVersion;
    private static final Integer localMajorVersion = 1;
    private boolean newMajorVersion;
    private RecyclerView searchResultsListView;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();
        setupListeners();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (dataLoaded)
                {
                    if (mBundle != null)
                    {
                        if (mBundle.containsKey(ALL_COMPETITORS) && mBundle.containsKey(TEAMS))
                        {
                            ArrayList<Object> mCompetitorsAndTeams = new ArrayList<>();

                            mCompetitorsAndTeams.addAll(mBundle.getParcelableArrayList(ALL_COMPETITORS));
                            mCompetitorsAndTeams.addAll(mBundle.getParcelableArrayList(TEAMS));

                            if (mCompetitorsAndTeams.isEmpty())
                            {
                                new GetCompetitorsAndTeamsFromDbTask(false, null).execute();
                            }
                            else
                            {
                                launchRandomCompetitor(mCompetitorsAndTeams);
                            }
                        }
                    }
                    else
                    {
                        new GetCompetitorsAndTeamsFromDbTask(false, null).execute();
                    }
                }
            }
        });

        getSupportLoaderManager().initLoader(DATA_LOADER, null, this);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        searchResultsListView.setVisibility(View.GONE);
        searchResultsListView.setAdapter(null);
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed()
    {
        if (!searchView.isIconified())
        {
            invalidateOptionsMenu();

        }
        else
        {
            super.onBackPressed();
        }
    }

    private void setupViews()
    {
        mSchmoedownButton = (ImageView) findViewById(R.id.bt_schmoedown);
        mSchmoedownButton.setContentDescription(getString(R.string.mts));
        mSchmoedownButton.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.schmoedownGreen), PorterDuff.Mode.MULTIPLY);
        mInnerGeekdomButton = (ImageView) findViewById(R.id.bt_innergeekdom);
        mInnerGeekdomButton.setContentDescription(getString(R.string.innergeekdom_championship));
        mInnerGeekdomButton.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        mCelebrityButton = (ImageView) findViewById(R.id.bt_celeb);
        mCelebrityButton.setContentDescription(getString(R.string.celebrity_schmoedown));
        mCelebrityButton.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.celebSchmoeBlue), PorterDuff.Mode.MULTIPLY);
        mStarWarsButton = (ImageView) findViewById(R.id.bt_star_wars);
        mStarWarsButton.setContentDescription(getString(R.string.star_wars));
        mStarWarsButton.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.starWarsYellow), PorterDuff.Mode.MULTIPLY);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
        mProgressBar.setIndeterminate(true);
        mLoadingLayout = (ConstraintLayout) findViewById(R.id.loading_layout);
        mLoadingLayout.setVisibility(View.INVISIBLE);
        searchResultsListView = (RecyclerView) findViewById(R.id.lv_search);
    }

    private void setupListeners()
    {
        View.OnClickListener buttonOnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                Class clas;

                if (dataLoaded)
                {
                    if (v.getId() == mSchmoedownButton.getId())
                    {
                        clas = SchmoedownActivity.class;
                        Intent intent = new Intent(getApplicationContext(), clas);
                        if (mBundle != null)
                        {
                            intent.putExtra(COMPETITORS, mBundle.getParcelableArrayList(COMPETITORS));
                            intent.putExtra(TEAMS, mBundle.getParcelableArrayList(TEAMS));
                        }
                        intent.putExtra(COMPETITION, getString(R.string.key_schmoedown));
                        startActivity(intent);

                    }
                    else if (v.getId() == mInnerGeekdomButton.getId())
                    {
                        clas = InnergeekdomActivity.class;
                        Intent intent = new Intent(getApplicationContext(), clas);
                        if (mBundle != null)
                        {
                            intent.putExtra(COMPETITORS, mBundle.getParcelableArrayList(GEEK_COMPETITORS));
                        }
                        intent.putExtra(COMPETITION, getString(R.string.key_innergeekdom));
                        startActivity(intent);
                    }
                    else if (v.getId() == mCelebrityButton.getId())
                    {
                        clas = SchmoedownActivity.class;
                        Intent intent = new Intent(getApplicationContext(), clas);
                        if (mBundle != null)
                        {
                            intent.putExtra(CELEBRITY_COMPETITORS, mBundle.getParcelableArrayList(CELEBRITY_COMPETITORS));
                            intent.putExtra(CELEBRITY_TEAMS, mBundle.getParcelableArrayList(CELEBRITY_TEAMS));
                        }
                        intent.putExtra(COMPETITION, getString(R.string.key_celebrity));
                        startActivity(intent);
                    }
                    else if (v.getId() == mStarWarsButton.getId())
                    {
                        clas = SchmoedownActivity.class;
                        Intent intent = new Intent(getApplicationContext(), clas);
                        if (mBundle != null)
                        {
                            intent.putExtra(STAR_WARS_COMPETITORS, mBundle.getParcelableArrayList(STAR_WARS_COMPETITORS));
                            intent.putExtra(STAR_WARS_TEAMS, mBundle.getParcelableArrayList(STAR_WARS_TEAMS));
                        }
                        intent.putExtra(COMPETITION, getString(R.string.key_star_wars));
                        startActivity(intent);
                    }
                }
            }
        };

        View.OnTouchListener onTouchListener = new View.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                if (dataLoaded)
                {
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.transparent);

                    if (v.getId() == mSchmoedownButton.getId())
                    {
                        color = ContextCompat.getColor(getApplicationContext(), R.color.schmoedownGreen);
                    }
                    else if (v.getId() == mInnerGeekdomButton.getId())
                    {
                        color = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
                    }
                    else if (v.getId() == mCelebrityButton.getId())
                    {
                        color = ContextCompat.getColor(getApplicationContext(), R.color.celebSchmoeBlue);
                    }
                    else if (v.getId() == mStarWarsButton.getId())
                    {
                        color = ContextCompat.getColor(getApplicationContext(), R.color.starWarsYellow);
                    }

                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                        {
                            ImageView view = (ImageView) v;
                            view.getDrawable().clearColorFilter();
                            if (v.getId() == mSchmoedownButton.getId() || v.getId() == mStarWarsButton.getId())
                            {
                                view.getDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.lightGrey), PorterDuff.Mode.OVERLAY);
                            }
                            view.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                        {
                            ImageView view = (ImageView) v;
                            view.getDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                            view.invalidate();
                            break;
                        }
                    }
                }
                return false;
            }
        };

        mSchmoedownButton.setOnClickListener(buttonOnClickListener);
        mSchmoedownButton.setOnTouchListener(onTouchListener);
        mInnerGeekdomButton.setOnClickListener(buttonOnClickListener);
        mInnerGeekdomButton.setOnTouchListener(onTouchListener);
        mCelebrityButton.setOnClickListener(buttonOnClickListener);
        mCelebrityButton.setOnTouchListener(onTouchListener);
        mStarWarsButton.setOnClickListener(buttonOnClickListener);
        mStarWarsButton.setOnTouchListener(onTouchListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (dataLoaded && newText.length() > 1)
                {
                    if (mBundle != null)
                    {
                        if (mBundle.containsKey(COMPETITORS) && mBundle.containsKey(TEAMS))
                        {
                            ArrayList<Object> mCompetitorsAndTeams = new ArrayList<>();
                            mCompetitorsAndTeams.addAll(mBundle.getParcelableArrayList(ALL_COMPETITORS));
                            mCompetitorsAndTeams.addAll(mBundle.getParcelableArrayList(TEAMS));

                            if (mCompetitorsAndTeams.isEmpty())
                            {
                                new GetCompetitorsAndTeamsFromDbTask(true, newText).execute();
                            }
                            else
                            {
                                performSearch(mCompetitorsAndTeams, newText);
                            }
                        }
                    }
                    else
                    {
                        new GetCompetitorsAndTeamsFromDbTask(true, newText).execute();
                    }
                }
                if (newText.length() < 2)
                {
                    searchResultsListView.setVisibility(View.GONE);
                    searchResultsListView.setAdapter(null);
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_search)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Bundle> onCreateLoader(int id, Bundle args)
    {
        return new AsyncTaskLoader<Bundle>(this)
        {
            final Bundle bundle = new Bundle();

            @Override
            protected void onStartLoading()
            {
                forceLoad();
                mLoadingLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public Bundle loadInBackground()
            {
                try
                {
                    StringBuilder builder = new StringBuilder();
                    String rawJson;
                    InputStream inputStream;
                    if (NetworkUtils.isOnline(getApplicationContext()))
                    {
                        String remoteSchmoeDbVersion = NetworkUtils.getRemoteSchmoeDbVersion();

                        String[] split = remoteSchmoeDbVersion.split("\\.");

                        remoteMajorVersion = Integer.valueOf(split[0]);
                        remoteMinorVersion = Integer.valueOf(split[1]);

                        //if remote major is larger than local major
                        //dialog to update
                        //else continue
                        //if remote minor is larger than local minor
                        //download
                        //else
                        //do nothing

                        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
                        int localMinorVersion = prefs.getInt(MINOR_VERSION, 0);

                        Log.d(MainActivity.class.toString(), "Current Local Major: " + localMajorVersion);
                        Log.d(MainActivity.class.toString(), "Current Local Minor: " + localMinorVersion);

                        Log.d(MainActivity.class.toString(), "Current Remote Major: " + remoteMajorVersion);
                        Log.d(MainActivity.class.toString(), "Current Remote Minor: " + remoteMinorVersion);

                        if (remoteMajorVersion > localMajorVersion)
                        {
                            newMajorVersion = true;
                            return null;
                        }
                        if (remoteMinorVersion.equals(localMinorVersion))
                        {
                            final Cursor competitorCursor = getCompetitorsFromDb();

                            final Cursor teamCursor = getTeamsFromDb();

                            if (competitorCursor.getCount() > 0 && teamCursor.getCount() > 0)
                            {
                                return null;
                            }
                        }
                        else if (remoteMinorVersion > localMinorVersion)
                        {
                            //get db (continue)
                        }


                        try
                        {
                            rawJson = NetworkUtils.getRemoteSchmoeDb();
                        }
                        catch (IOException e)
                        {
                            //TODO: ERROR
                            inputStream = getResources().openRawResource(R.raw.schmoedb);
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                            String line;
                            while ((line = reader.readLine()) != null)
                            {
                                builder.append(line);
                            }

                            rawJson = builder.toString();
                        }
                    }
                    else
                    {
                        //TODO: HANDLE NOT ONLINE
                        return null;
                    }

                    JSONObject data = new JSONObject(rawJson);

                    ArrayList competitorList = getCompetitorData(data);
                    ArrayList teamList = getTeamData(data);

                    populateCompetitorsDatabase(competitorList);
                    Competitor comp = new Competitor();
                    ArrayList<Competitor> rankedCompetitors = new ArrayList<>();
                    ArrayList<Competitor> geekCompetitors = new ArrayList<>();
                    ArrayList<Competitor> starWarsCompetitors = new ArrayList<>();
                    ArrayList<Competitor> celebrityCompetitors = new ArrayList<>();

                    for (int i = 0; i < 10; i++)
                    {
                        rankedCompetitors.add(comp);
                        geekCompetitors.add(comp);
                    }
                    for (Competitor competitor : (ArrayList<Competitor>) competitorList)
                    {
                        if (competitor.getRank() > 0 && competitor.getRank() <= 10)
                        {
                            rankedCompetitors.set(competitor.getRank() - 1, competitor);
                        }
                        if (competitor.getGeekRank() > 0 && competitor.getGeekRank() <= 10)
                        {
                            geekCompetitors.add(competitor);
                        }
                        if (competitor.getStarWars())
                        {
                            starWarsCompetitors.add(competitor);
                        }
                        if (competitor.isCelebrity())
                        {
                            celebrityCompetitors.add(competitor);
                        }
                    }

                    populateTeamsDatabase(teamList);
                    Team tm = new Team();
                    ArrayList<Team> rankedTeams = new ArrayList<>();
                    ArrayList<Team> celebrityTeams = new ArrayList<>();
                    ArrayList<Team> starWarsTeams = new ArrayList<>();

                    for (int i = 0; i < 10; i++)
                    {
                        rankedTeams.add(tm);
                    }
                    for (Team team : (ArrayList<Team>) teamList)
                    {
                        if (team.getRank() > 0 && team.getRank() <= 10)
                        {
                            rankedTeams.set(team.getRank() - 1, team);
                        }
                        if (team.isCelebrity())
                        {
                            celebrityTeams.add(team);
                        }
                        if (team.isStarWars())
                        {
                            starWarsTeams.add(team);
                        }
                    }
                    bundle.putParcelableArrayList(COMPETITORS, (ArrayList) rankedCompetitors);
                    bundle.putParcelableArrayList(ALL_COMPETITORS, competitorList);
                    bundle.putParcelableArrayList(TEAMS, (ArrayList) rankedTeams);
                    bundle.putParcelableArrayList(GEEK_COMPETITORS, (ArrayList) geekCompetitors);
                    bundle.putParcelableArrayList(STAR_WARS_COMPETITORS, (ArrayList) starWarsCompetitors);
                    bundle.putParcelableArrayList(STAR_WARS_TEAMS, (ArrayList) starWarsTeams);
                    bundle.putParcelableArrayList(CELEBRITY_COMPETITORS, (ArrayList) celebrityCompetitors);
                    bundle.putParcelableArrayList(CELEBRITY_TEAMS, (ArrayList) celebrityTeams);

                    if (!competitorList.isEmpty() && !teamList.isEmpty())
                    {
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
                        editor.putInt(MINOR_VERSION, remoteMinorVersion);

                        editor.apply();

                        Log.d(MainActivity.class.toString(), "New Local Major: " + localMajorVersion);
                        Log.d(MainActivity.class.toString(), "New Local Minor: " + remoteMinorVersion);
                    }

                }
                catch (IOException | JSONException e)
                {
                    Log.e(getClass().toString(), "Unable to retrieve database data. " + e.getMessage());
                    //TODO:HANDLE
                }
                return bundle;
            }

            private ArrayList<Competitor> getCompetitorData(JSONObject data) throws JSONException
            {
                JSONArray competitorJsonArray = data.getJSONArray(getString(R.string.key_competitor));

                ArrayList<Competitor> competitors = new ArrayList<>();

                for (int i = 0; i < competitorJsonArray.length(); i++)
                {
                    JSONObject competitorJsonObject = competitorJsonArray.getJSONObject(i);

                    JSONArray currentBeltsJsonArray = competitorJsonObject.getJSONArray("current_belts");
                    JSONArray pastBeltsJsonArray = competitorJsonObject.getJSONArray("past_belts");
                    JSONArray currentTeamsJsonArray = competitorJsonObject.getJSONArray("current_teams");
                    JSONArray pastTeamsJsonArray = competitorJsonObject.getJSONArray("past_teams");
                    JSONArray currentFactionsJsonArray = competitorJsonObject.getJSONArray("current_factions");
                    JSONArray pastFactionsJsonArray = competitorJsonObject.getJSONArray("past_factions");

                    //current belts
                    List<String> currentBelts = new ArrayList<>();
                    for (int j = 0; j < currentBeltsJsonArray.length(); j++)
                    {
                        JSONObject jsonObject = currentBeltsJsonArray.getJSONObject(j);
                        currentBelts.add(jsonObject.getString(BELT));
                    }

                    //past belts
                    List<String> pastBelts = new ArrayList<>();
                    for (int j = 0; j < pastBeltsJsonArray.length(); j++)
                    {
                        JSONObject jsonObject = pastBeltsJsonArray.getJSONObject(j);
                        pastBelts.add(jsonObject.getString(BELT));
                    }

                    //current teams
                    List<String> currentTeams = new ArrayList<>();
                    for (int j = 0; j < currentTeamsJsonArray.length(); j++)
                    {
                        JSONObject jsonObject = currentTeamsJsonArray.getJSONObject(j);
                        currentTeams.add(jsonObject.getString(getString(R.string.key_team)));
                    }

                    //past teams
                    List<String> pastTeams = new ArrayList<>();
                    for (int j = 0; j < pastTeamsJsonArray.length(); j++)
                    {
                        JSONObject jsonObject = pastTeamsJsonArray.getJSONObject(j);
                        pastTeams.add(jsonObject.getString(getString(R.string.key_team)));
                    }

                    //current factions
                    List<String> currentFactions = new ArrayList<>();
                    for (int j = 0; j < currentFactionsJsonArray.length(); j++)
                    {
                        JSONObject jsonObject = currentFactionsJsonArray.getJSONObject(j);
                        currentFactions.add(jsonObject.getString(FACTION));
                    }

                    //past factions
                    List<String> pastFactions = new ArrayList<>();
                    for (int j = 0; j < pastFactionsJsonArray.length(); j++)
                    {
                        JSONObject jsonObject = pastFactionsJsonArray.getJSONObject(j);
                        pastFactions.add(jsonObject.getString(FACTION));
                    }

                    Competitor competitor = new Competitor();

                    competitor.setFirstName(competitorJsonObject.getString("first_name"));
                    competitor.setSurname(competitorJsonObject.getString("surname"));
                    competitor.setNickname(competitorJsonObject.getString("nickname"));
                    competitor.setWins(competitorJsonObject.getInt(WINS));
                    competitor.setLosses(competitorJsonObject.getInt(LOSSES));
                    competitor.setKos(competitorJsonObject.getInt("ko"));
                    competitor.setRank(competitorJsonObject.getInt(RANK));
                    competitor.setCurrentBelts(currentBelts);
                    competitor.setPastBelts(pastBelts);
                    competitor.setCurrentTeams(currentTeams);
                    competitor.setPastTeams(pastTeams);
                    competitor.setCurrentFactions(currentFactions);
                    competitor.setPastFactions(pastFactions);
                    competitor.setActive(competitorJsonObject.getBoolean(ACTIVE));
                    competitor.setInnergeekdom(competitorJsonObject.getBoolean(getString(R.string.key_innergeekdom)));
                    competitor.setGeekWins(competitorJsonObject.getInt("geek_wins"));
                    competitor.setGeekLosses(competitorJsonObject.getInt("geek_losses"));
                    competitor.setGeekKnockouts(competitorJsonObject.getInt("geek_knockouts"));
                    competitor.setGeekRank(competitorJsonObject.getInt("geek_rank"));
                    competitor.setStarWars(competitorJsonObject.getBoolean(getString(R.string.key_star_wars)));
                    competitor.setStarWarsWins(competitorJsonObject.getInt(STAR_WARS_WINS));
                    competitor.setStarWarsLosses(competitorJsonObject.getInt(STAR_WARS_LOSSES));
                    competitor.setStarWarsKnockouts(competitorJsonObject.getInt(STAR_WARS_KNOCKOUTS));
                    competitor.setCelebrity(competitorJsonObject.getBoolean(getString(R.string.key_celebrity)));
                    competitor.setImageName(competitorJsonObject.getString(IMAGE_NAME));

                    Log.i(EMPTY_STRING, EMPTY_STRING);

                    competitors.add(competitor);

                    //add to db
                }
                return competitors;
            }

            private ArrayList<Team> getTeamData(JSONObject data) throws JSONException
            {
                JSONArray teamJsonArray = data.getJSONArray(getString(R.string.key_team));

                ArrayList<Team> teams = new ArrayList<>();

                for (int i = 0; i < teamJsonArray.length(); i++)
                {
                    JSONObject teamJsonObject = teamJsonArray.getJSONObject(i);

                    JSONArray currentMembersJsonArray = teamJsonObject.getJSONArray("current_members");
                    JSONArray pastMembersJsonArray = teamJsonObject.getJSONArray("past_members");
                    JSONArray currentBeltsJsonArray = teamJsonObject.getJSONArray("current_belts");
                    JSONArray pastBeltsJsonArray = teamJsonObject.getJSONArray("past_belts");

                    //current members
                    List<String> currentMembers = new ArrayList<>();
                    for (int j = 0; j < currentMembersJsonArray.length(); j++)
                    {
                        JSONObject jsonObject = currentMembersJsonArray.getJSONObject(j);
                        currentMembers.add(jsonObject.getString(MEMBER));
                    }

                    //past members
                    List<String> pastMembers = new ArrayList<>();
                    for (int j = 0; j < pastMembersJsonArray.length(); j++)
                    {
                        JSONObject jsonObject = pastMembersJsonArray.getJSONObject(j);
                        pastMembers.add(jsonObject.getString(MEMBER));
                    }

                    //current belts
                    List<String> currentBelts = new ArrayList<>();
                    for (int j = 0; j < currentBeltsJsonArray.length(); j++)
                    {
                        JSONObject jsonObject = currentBeltsJsonArray.getJSONObject(j);
                        currentBelts.add(jsonObject.getString(BELT));
                    }

                    //past belts
                    List<String> pastBelts = new ArrayList<>();
                    for (int j = 0; j < pastBeltsJsonArray.length(); j++)
                    {
                        JSONObject jsonObject = pastBeltsJsonArray.getJSONObject(j);
                        pastBelts.add(jsonObject.getString(BELT));
                    }

                    Team team = new Team();

                    team.setName(teamJsonObject.getString("name"));
                    team.setWins(teamJsonObject.getInt(WINS));
                    team.setLosses(teamJsonObject.getInt(LOSSES));
                    team.setKos(teamJsonObject.getInt("kos"));
                    team.setRank(teamJsonObject.getInt(RANK));
                    team.setCurrentMembers(currentMembers);
                    team.setPastMembers(pastMembers);
                    team.setCurrentBelts(currentBelts);
                    team.setPastBelts(pastBelts);
                    team.setActive(teamJsonObject.getBoolean(ACTIVE));
                    team.setStarWars(teamJsonObject.getBoolean(getString(R.string.key_star_wars)));
                    team.setStarWarsWins(teamJsonObject.getInt(STAR_WARS_WINS));
                    team.setStarWarsLosses(teamJsonObject.getInt(STAR_WARS_LOSSES));
                    team.setStarWarsKnockouts(teamJsonObject.getInt(STAR_WARS_KNOCKOUTS));
                    team.setCelebrity(teamJsonObject.getBoolean(getString(R.string.key_celebrity)));

                    team.setImageName(teamJsonObject.getString(IMAGE_NAME));

                    teams.add(team);
                }

                return teams;
            }

            private boolean populateCompetitorsDatabase(ArrayList<Competitor> competitors)
            {
                CompetitorsDbHelper competitorsDbHelper = new CompetitorsDbHelper(getApplicationContext());
                competitorsDbHelper.getWritableDatabase().delete(CompetitorContract.CompetitorsEntry.TABLE_NAME, null, null);

                for (final Competitor competitor : competitors)
                {
                    String currentBeltsString = listToString(competitor.getCurrentBelts());

                    String pastBeltsString = listToString(competitor.getPastBelts());
                    String currentTeamsString = listToString(competitor.getCurrentTeams());
                    String pastTeamsString = listToString(competitor.getPastTeams());
                    String currentFactionsString = listToString(competitor.getCurrentFactions());
                    String pastFactionsString = listToString(competitor.getPastFactions());

                    ContentValues cv = new ContentValues();

                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_FIRST_NAME, competitor.getFirstName());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_SURNAME, competitor.getSurname());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_NICKNAME, competitor.getNickname());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_WINS, competitor.getWins());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_LOSSES, competitor.getLosses());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_KOS, competitor.getKos());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_RANK, competitor.getRank());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_CURRENT_BELTS, currentBeltsString);
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_PAST_BELTS, pastBeltsString);
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_CURRENT_TEAMS, currentTeamsString);
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_PAST_TEAMS, pastTeamsString);
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_CURRENT_FACTIONS, currentFactionsString);
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_PAST_FACTIONS, pastFactionsString);
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_INNERGEEKDOM, competitor.getInnergeekdom());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_GEEK_WINS, competitor.getGeekWins());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_GEEK_LOSSES, competitor.getGeekLosses());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_GEEK_KNOCKOUTS, competitor.getGeekKnockouts());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_GEEK_RANK, competitor.getGeekRank());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_STARWARS, competitor.getStarWars());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_STARWARS_WINS, competitor.getStarWarsWins());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_STARWARS_LOSSES, competitor.getStarWarsLosses());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_STARWARS_KNOCKOUTS, competitor.getStarWarsKnockouts());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_CELEBRITY, competitor.isCelebrity());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_ACTIVE, competitor.getActive());
                    cv.put(CompetitorContract.CompetitorsEntry.COLUMN_IMAGE_NAME, competitor.getImageName());

                    Uri uri = CompetitorContract.CompetitorsEntry.CONTENT_URI;

                    getContentResolver().insert(CompetitorContract.CompetitorsEntry.CONTENT_URI, cv);

                    getContentResolver().notifyChange(uri, null);
                }
                return true;
            }

            private boolean populateTeamsDatabase(ArrayList<Team> teams)
            {
                TeamDbHelper teamsDbHelper = new TeamDbHelper(getApplicationContext());
                teamsDbHelper.getWritableDatabase().delete(TeamContract.TeamsEntry.TABLE_NAME, null, null);

                for (final Team team : teams)
                {
                    String currentBeltsString = listToString(team.getCurrentBelts());

                    String pastBeltsString = listToString(team.getPastBelts());
                    String currentMembersString = listToString(team.getCurrentMembers());
                    String pastMembersString = listToString(team.getPastMembers());

                    ContentValues cv = new ContentValues();

                    cv.put(TeamContract.TeamsEntry.COLUMN_NAME, team.getName());
                    cv.put(TeamContract.TeamsEntry.COLUMN_WINS, team.getWins());
                    cv.put(TeamContract.TeamsEntry.COLUMN_LOSSES, team.getLosses());
                    cv.put(TeamContract.TeamsEntry.COLUMN_KOS, team.getKos());
                    cv.put(TeamContract.TeamsEntry.COLUMN_RANK, team.getRank());
                    cv.put(TeamContract.TeamsEntry.COLUMN_CURRENT_BELTS, currentBeltsString);
                    cv.put(TeamContract.TeamsEntry.COLUMN_PAST_BELTS, pastBeltsString);
                    cv.put(TeamContract.TeamsEntry.COLUMN_CURRENT_MEMBERS, currentMembersString);
                    cv.put(TeamContract.TeamsEntry.COLUMN_PAST_MEMBERS, pastMembersString);
                    cv.put(TeamContract.TeamsEntry.COLUMN_ACTIVE, team.getActive());
                    cv.put(TeamContract.TeamsEntry.COLUMN_IMAGE_NAME, team.getImageName());
                    cv.put(TeamContract.TeamsEntry.COLUMN_STARWARS, team.isStarWars());
                    cv.put(TeamContract.TeamsEntry.COLUMN_STARWARS_WINS, team.getStarWarsWins());
                    cv.put(TeamContract.TeamsEntry.COLUMN_STARWARS_LOSSES, team.getStarWarsLosses());
                    cv.put(TeamContract.TeamsEntry.COLUMN_STARWARS_KNOCKOUTS, team.getStarWarsKnockouts());
                    cv.put(TeamContract.TeamsEntry.COLUMN_CELEBRITY, team.isCelebrity());

                    Uri uri = TeamContract.TeamsEntry.CONTENT_URI;

                    getContentResolver().insert(TeamContract.TeamsEntry.CONTENT_URI, cv);

                    getContentResolver().notifyChange(uri, null);
                }
                return true;
            }
        };
    }

    private Cursor getTeamsFromDb()
    {
        return getContentResolver().query(
                TeamContract.TeamsEntry.CONTENT_URI,
                null,
                null,
                null,
                TeamContract.TeamsEntry.COLUMN_RANK);
    }

    private Cursor getCompetitorsFromDb()
    {
        return getContentResolver().query(
                CompetitorContract.CompetitorsEntry.CONTENT_URI,
                null,
                null,
                null,
                CompetitorContract.CompetitorsEntry.COLUMN_RANK);
    }

    @Override
    public void onLoadFinished(Loader<Bundle> loader, Bundle data)
    {
        dataLoaded = true;
        mBundle = data;
        mLoadingLayout.setVisibility(View.INVISIBLE);
        getSupportLoaderManager().destroyLoader(DATA_LOADER);

        if (newMajorVersion)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.new_db_version_message), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Bundle> loader)
    {

    }

    private String listToString(List<String> list)
    {
        String stringList = EMPTY_STRING;
        for (String item : list)
        {
            if (stringList.equals(EMPTY_STRING))
            {
                stringList = stringList + item;
            }
            else
            {
                stringList = stringList + COMMA + item;
            }
        }
        return stringList;
    }

    public class GetCompetitorsAndTeamsFromDbTask extends AsyncTask<Void, Void, ArrayList<Object>>
    {
        final Boolean forSearch;
        final String searchQuery;

        GetCompetitorsAndTeamsFromDbTask(Boolean forSearch, String searchQuery)
        {
            this.forSearch = forSearch;
            this.searchQuery = searchQuery;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Object> doInBackground(Void... params)
        {

            ArrayList<Object> competitorsAndTeams = new ArrayList<>();

            Cursor competitorCursor = getApplicationContext().getContentResolver().query(
                    CompetitorContract.CompetitorsEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    CompetitorContract.CompetitorsEntry.COLUMN_RANK);


            for (int i = 0; i < competitorCursor.getCount(); i++)
            {
                if (!competitorCursor.moveToPosition(i))
                {
                    continue;
                }

                int wins = competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_WINS));
                int losses = competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_LOSSES));

                Competitor competitor = new Competitor();
                competitor.setFirstName(competitorCursor.getString(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_FIRST_NAME)));
                competitor.setSurname(competitorCursor.getString(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_SURNAME)));
                competitor.setNickname(competitorCursor.getString(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_NICKNAME)));
                competitor.setRank(competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_RANK)));
                competitor.setWins(wins);
                competitor.setLosses(losses);
                competitor.setKos(competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_KOS)));
                competitor.setCurrentBelts(Arrays.asList(competitorCursor.getString(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_CURRENT_BELTS)).split(COMMA)));
                competitor.setPastBelts(Arrays.asList(competitorCursor.getString(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_PAST_BELTS)).split(COMMA)));
                competitor.setCurrentTeams(Arrays.asList(competitorCursor.getString(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_CURRENT_TEAMS)).split(COMMA)));
                competitor.setPastTeams(Arrays.asList(competitorCursor.getString(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_PAST_TEAMS)).split(COMMA)));
                competitor.setCurrentFactions(Arrays.asList(competitorCursor.getString(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_CURRENT_FACTIONS)).split(COMMA)));
                competitor.setPastFactions(Arrays.asList(competitorCursor.getString(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_PAST_FACTIONS)).split(COMMA)));
                competitor.setActive(competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_ACTIVE)) != 0);
                competitor.setInnergeekdom(competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_INNERGEEKDOM)) != 0);
                competitor.setGeekRank(competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_GEEK_RANK)));
                competitor.setGeekWins(competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_GEEK_WINS)));
                competitor.setGeekLosses(competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_GEEK_LOSSES)));
                competitor.setGeekKnockouts(competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_GEEK_KNOCKOUTS)));
                competitor.setImageName(competitorCursor.getString(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_IMAGE_NAME)));
                competitor.setStarWarsWins(competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_STARWARS_WINS)));
                competitor.setStarWarsLosses(competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_STARWARS_LOSSES)));
                competitor.setStarWarsKnockouts(competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_STARWARS_KNOCKOUTS)));
                competitor.setStarWars(competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_STARWARS)) != 0);
                competitor.setCelebrity(competitorCursor.getInt(competitorCursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_CELEBRITY)) != 0);

                competitorsAndTeams.add(competitor);
            }

            Cursor teamCursor = getApplicationContext().getContentResolver().query(
                    TeamContract.TeamsEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    TeamContract.TeamsEntry.COLUMN_RANK);


            for (int j = 0; j < teamCursor.getCount(); j++)
            {

                if (!teamCursor.moveToPosition(j))
                {
                    continue;
                }

                Team team = new Team();
                team.setName(teamCursor.getString(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_NAME)));
                team.setRank(teamCursor.getInt(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_RANK)));
                team.setWins(teamCursor.getInt(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_WINS)));
                team.setLosses(teamCursor.getInt(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_LOSSES)));
                team.setKos(teamCursor.getInt(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_KOS)));
                team.setCurrentMembers(Arrays.asList(teamCursor.getString(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_CURRENT_MEMBERS)).split(COMMA)));
                team.setPastMembers(Arrays.asList(teamCursor.getString(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_PAST_MEMBERS)).split(COMMA)));
                team.setCurrentBelts(Arrays.asList(teamCursor.getString(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_CURRENT_BELTS)).split(COMMA)));
                team.setPastBelts(Arrays.asList(teamCursor.getString(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_PAST_BELTS)).split(COMMA)));
                team.setImageName(teamCursor.getString(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_IMAGE_NAME)));
                team.setActive(teamCursor.getInt(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_ACTIVE)) != 0);
                team.setStarWars(teamCursor.getInt(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_STARWARS)) != 0);
                team.setStarWarsWins(teamCursor.getInt(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_STARWARS_WINS)));
                team.setStarWarsLosses(teamCursor.getInt(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_STARWARS_LOSSES)));
                team.setStarWarsKnockouts(teamCursor.getInt(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_STARWARS_KNOCKOUTS)));
                team.setCelebrity(teamCursor.getInt(teamCursor.getColumnIndex(TeamContract.TeamsEntry.COLUMN_CELEBRITY)) != 0);

                competitorsAndTeams.add(team);
            }
            teamCursor.close();
            competitorCursor.close();
            return competitorsAndTeams;
        }

        @Override
        protected void onPostExecute(ArrayList<Object> competitorsAndTeams)
        {
            super.onPostExecute(competitorsAndTeams);
            if (forSearch && searchQuery.length() > 1)
            {
                performSearch(competitorsAndTeams, searchQuery);
            }
            else
            {
                launchRandomCompetitor(competitorsAndTeams);
            }
        }
    }

    private void performSearch(ArrayList<Object> competitorsAndTeams, String searchQuery)
    {
        SearchAdapter mSearchAdapter;

        ArrayList<Object> results = new ArrayList<>();

        for (Object object : competitorsAndTeams)
        {
            if (object instanceof Competitor)
            {
                Competitor competitor = (Competitor) object;
                if (StringUtils.containsIgnoreCase(competitor.getNickname(), searchQuery))
                {
                    results.add(object);
                }
            }
            else if (object instanceof Team)
            {
                Team team = (Team) object;
                if (StringUtils.containsIgnoreCase(team.getName(), searchQuery))
                {
                    results.add(object);
                }
            }
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        searchResultsListView.setLayoutManager(mLayoutManager);
        searchResultsListView.setHasFixedSize(true);
        searchResultsListView.setVisibility(View.VISIBLE);

        mSearchAdapter = new SearchAdapter((ArrayList) results);
        searchResultsListView.setAdapter(mSearchAdapter);
    }

    private void launchRandomCompetitor(ArrayList<Object> competitorsAndTeams)
    {
        Collections.shuffle(competitorsAndTeams);
        Random random = new Random();
        int index = random.nextInt(competitorsAndTeams.size());
        Object object = competitorsAndTeams.get(index);

        Intent intent = null;

        if (object instanceof Competitor)
        {
            Competitor competitor = (Competitor) object;
            boolean isSchmoedown = competitor.getWins() + competitor.getLosses() > 0;
            List<Class> classList = new ArrayList<>();

            if (isSchmoedown)
            {
                classList.add(CompetitorDetailActivity.class);
            }
            if (competitor.getInnergeekdom())
            {
                classList.add(InnergeekdomDetailActivity.class);
            }
            if (competitor.getStarWars())
            {
                classList.add(StarWarsDetailActivity.class);
            }
            intent = new Intent(this, getRandomClass(classList));
            intent.putExtra(getString(R.string.key_competitor), Parcels.wrap(object));
        }
        else if (object instanceof Team)
        {
            intent = new Intent(this, TeamDetailActivity.class);
            intent.putExtra(getString(R.string.key_team), Parcels.wrap(object));
        }

        if (intent != null)
        {
            startActivity(intent);
        }
    }

    private Class<?> getRandomClass(List<Class> classList)
    {
        Collections.shuffle(classList);
        return classList.get(0);
    }
}
