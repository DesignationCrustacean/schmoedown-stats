package com.crustacean.designation.schmoedownstats.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.crustacean.designation.schmoedownstats.R;
import com.crustacean.designation.schmoedownstats.model.Team;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

public class TeamDetailActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        ImageView teamImage;
        TextView name, currentBelts, pastBelts, currentMembers, pastMembers, rank, wins, losses, kos, competition;

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(getString(R.string.key_team)))
        {
            Team team = Parcels.unwrap(intent.getParcelableExtra(getString(R.string.key_team)));
            teamImage = (ImageView) findViewById(R.id.iv_team_image);
            currentBelts = (TextView) findViewById(R.id.tv_current_belts);
            pastBelts = (TextView) findViewById(R.id.tv_past_belts);
            currentMembers = (TextView) findViewById(R.id.tv_current_members);
            pastMembers = (TextView) findViewById(R.id.tv_past_members);
            name = (TextView) findViewById(R.id.tv_name);
            rank = (TextView) findViewById(R.id.tv_rank);
            wins = (TextView) findViewById(R.id.tv_wins);
            losses = (TextView) findViewById(R.id.tv_losses);
            kos = (TextView) findViewById(R.id.tv_kos);
            competition = (TextView) findViewById(R.id.tv_competition);

            Picasso.with(getApplicationContext()).load(getString(R.string.img_base_url) + team.getImageName()).placeholder(R.color.transparent).error(R.mipmap.mts).into(teamImage);
            teamImage.setContentDescription(team.getName());
            name.setText(team.getName());

            currentBelts.setText(populateComplexItem(team.getCurrentBelts()));
            pastBelts.setText(populateComplexItem(team.getPastBelts()));
            currentMembers.setText(populateComplexItem(team.getCurrentMembers()));
            pastMembers.setText(populateComplexItem(team.getPastMembers()));

            if (team.isCelebrity())
            {
                competition.setText(getString(R.string.celebrity_schmoedown));
                competition.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.celebSchmoeBlue));
            }

            Integer ranking = team.getRank();
            if (ranking > 0)
            {
                rank.setText(String.valueOf(ranking));
            }
            else
            {
                rank.setText(getString(R.string.not_applicable));
            }

            wins.setText(String.valueOf(team.getWins()));
            losses.setText(String.valueOf(team.getLosses()));
            kos.setText(String.valueOf(team.getKos()));

            getSupportActionBar().setTitle(team.getName());
        }
    }

    private String populateComplexItem(List<String> list)
    {
        String displayString = getString(R.string.none);
        if (!list.isEmpty() && list.get(0).length() > 0)
        {
            displayString = "";
            for (String item : list)
            {
                displayString += item + ", ";
            }
            if (!displayString.isEmpty())
            {
                displayString = displayString.substring(0, displayString.length() - 2);
            }
        }
        return displayString;
    }
}
