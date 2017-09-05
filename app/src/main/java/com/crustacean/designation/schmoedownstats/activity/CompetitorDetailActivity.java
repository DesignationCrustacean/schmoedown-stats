package com.crustacean.designation.schmoedownstats.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.crustacean.designation.schmoedownstats.R;
import com.crustacean.designation.schmoedownstats.model.Competitor;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

public class CompetitorDetailActivity extends AppCompatActivity
{
    public static final String EMPTY_STRING = "";
    public static final String SPACE = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_detail);

        ImageView competitorImage;
        TextView name, nickname, currentBelts, pastBelts, currentTeams, pastTeams, rank, wins, losses, kos, competition;

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(getString(R.string.key_competitor)))
        {
            Competitor competitor = Parcels.unwrap(intent.getParcelableExtra(getString(R.string.key_competitor)));
            competitorImage = (ImageView) findViewById(R.id.iv_competitor_image);
            currentBelts = (TextView) findViewById(R.id.tv_current_belts);
            pastBelts = (TextView) findViewById(R.id.tv_past_belts);
            currentTeams = (TextView) findViewById(R.id.tv_current_teams);
            pastTeams = (TextView) findViewById(R.id.tv_past_teams);
            name = (TextView) findViewById(R.id.tv_name);
            nickname = (TextView) findViewById(R.id.tv_nickname);
            rank = (TextView) findViewById(R.id.tv_rank);
            wins = (TextView) findViewById(R.id.tv_wins);
            losses = (TextView) findViewById(R.id.tv_losses);
            kos = (TextView) findViewById(R.id.tv_kos);
            competition = (TextView) findViewById(R.id.tv_competition);

            if (competitor.isCelebrity())
            {
                competition.setText(getText(R.string.celebrity_schmoedown));
                competition.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.celebSchmoeBlue));
            }

            String url = getString(R.string.img_base_url) + competitor.getImageName();

            Picasso.with(getApplicationContext()).load(url).placeholder(R.color.transparent).error(R.mipmap.mts).into(competitorImage);

            String fullName = competitor.getFirstName() + SPACE + competitor.getSurname();

            competitorImage.setContentDescription(fullName);

            name.setText(fullName);
            nickname.setText(competitor.getNickname());

            currentBelts.setText(populateComplexItem(competitor.getCurrentBelts()));
            pastBelts.setText(populateComplexItem(competitor.getPastBelts()));
            currentTeams.setText(populateComplexItem(competitor.getCurrentTeams()));
            pastTeams.setText(populateComplexItem(competitor.getPastTeams()));

            Integer ranking = competitor.getRank();
            if (ranking > 0)
            {
                rank.setText(String.valueOf(ranking));
            }
            else
            {
                rank.setText(getString(R.string.not_applicable));
            }

            wins.setText(String.valueOf(competitor.getWins()));
            losses.setText(String.valueOf(competitor.getLosses()));
            kos.setText(String.valueOf(competitor.getKos()));

            getSupportActionBar().setTitle(competitor.getNickname());
        }
    }

    private String populateComplexItem(List<String> list)
    {
        String displayString = getString(R.string.none);
        if (!list.isEmpty() && list.get(0).length() > 0)
        {
            displayString = EMPTY_STRING;
            for (String item : list)
            {
                if (item.equals(getString(R.string.belt_innergeekdom)) || item.equals(getString(R.string.belt_starwars)))
                {
                    continue;
                }
                displayString += item + ", ";
            }
            if (displayString.isEmpty() || displayString.equals(EMPTY_STRING))
            {
                displayString = getString(R.string.none);
            }
            else
            {
                displayString = displayString.substring(0, displayString.length() - 2);
            }
        }
        return displayString;
    }
}
