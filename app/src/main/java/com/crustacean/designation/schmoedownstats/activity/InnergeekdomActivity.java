package com.crustacean.designation.schmoedownstats.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.crustacean.designation.schmoedownstats.R;
import com.crustacean.designation.schmoedownstats.adapter.SchmoedownAdapter;
import com.crustacean.designation.schmoedownstats.data.CompetitorContract;
import com.crustacean.designation.schmoedownstats.model.Competitor;

import java.util.ArrayList;
import java.util.Arrays;

public class InnergeekdomActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String COMPETITORS = "geek_competitors";
    private static final String SCROLL_POSITION = "scroll_position";
    public static final String COMMA = ",";
    private ArrayList<Competitor> mCompetitors = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private LinearLayoutManager mLayoutManager;
    private int scrollPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innergeekdom);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recyclerView);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        getSupportActionBar().setTitle(getString(R.string.innergeekdom));
        Intent intent = getIntent();

        if (intent.hasExtra(COMPETITORS))
        {
            mCompetitors = (ArrayList) intent.getParcelableArrayListExtra(COMPETITORS);
        }
        else
        {
            if (savedInstanceState != null)
            {
                if (savedInstanceState.containsKey(COMPETITORS))
                {
                    mCompetitors = (ArrayList) savedInstanceState.getParcelableArrayList(COMPETITORS);
                }
                else
                {
                    new GetCompetitorsFromDbTask().execute();
                }
                if (savedInstanceState.containsKey(SCROLL_POSITION) && savedInstanceState.getInt(SCROLL_POSITION) > 0)
                {
                    scrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
                }
            }
            else
            {
                new GetCompetitorsFromDbTask().execute();
            }
        }

        if (savedInstanceState != null)
        {
            if (savedInstanceState.containsKey(SCROLL_POSITION) && savedInstanceState.getInt(SCROLL_POSITION) > 0)
            {
                scrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
            }
        }
        populate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (!mCompetitors.isEmpty())
        {
            outState.putParcelableArrayList(COMPETITORS, (ArrayList) mCompetitors);
        }
        outState.putInt(SCROLL_POSITION, mLayoutManager.findFirstCompletelyVisibleItemPosition());
    }

    @Override
    public void onClick(View v)
    {

    }


    private void populate()
    {
        SchmoedownAdapter mSchmoedownAdapter = new SchmoedownAdapter((ArrayList) mCompetitors, getString(R.string.key_innergeekdom));
        mRecyclerView.setAdapter(mSchmoedownAdapter);
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
                    CompetitorContract.CompetitorsEntry.COLUMN_GEEK_RANK);

            ArrayList<Competitor> competitors = new ArrayList<>();

            for (int i = 0; i < cursor.getCount(); i++)
            {

                if (!cursor.moveToPosition(i))
                {
                    continue;
                }

                final int rank = cursor.getInt(cursor.getColumnIndex(CompetitorContract.CompetitorsEntry.COLUMN_GEEK_RANK));

                if (rank < 1)
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
            populate();
        }
    }
}

