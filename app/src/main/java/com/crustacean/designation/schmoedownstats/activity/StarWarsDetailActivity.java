package com.crustacean.designation.schmoedownstats.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.crustacean.designation.schmoedownstats.R;
import com.crustacean.designation.schmoedownstats.model.Competitor;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

public class StarWarsDetailActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_wars_detail);

        ImageView competitorImage;
        TextView name, nickname, currentBelts, pastBelts, rank, wins, losses, kos;

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(getString(R.string.key_competitor)))
        {
            Competitor competitor = Parcels.unwrap(intent.getParcelableExtra(getString(R.string.key_competitor)));
            competitorImage = (ImageView) findViewById(R.id.iv_competitor_image);
            currentBelts = (TextView) findViewById(R.id.tv_current_belts);
            pastBelts = (TextView) findViewById(R.id.tv_past_belts);
            name = (TextView) findViewById(R.id.tv_name);
            nickname = (TextView) findViewById(R.id.tv_nickname);
            //rank = (TextView) findViewById(R.id.tv_rank);
            wins = (TextView) findViewById(R.id.tv_wins);
            losses = (TextView) findViewById(R.id.tv_losses);
            kos = (TextView) findViewById(R.id.tv_kos);

            Picasso.with(getApplicationContext()).load(getString(R.string.img_base_url) + competitor.getImageName()).placeholder(R.color.transparent).error(R.mipmap.mts).into(competitorImage);

            String fullName = competitor.getFirstName() + " " + competitor.getSurname();
            competitorImage.setContentDescription(fullName);
            name.setText(fullName);
            nickname.setText(competitor.getNickname());

            currentBelts.setText(populateComplexItem(competitor.getCurrentBelts()));
            pastBelts.setText(populateComplexItem(competitor.getPastBelts()));

            wins.setText(String.valueOf(competitor.getStarWarsWins()));
            losses.setText(String.valueOf(competitor.getStarWarsLosses()));
            kos.setText(String.valueOf(competitor.getStarWarsKnockouts()));

            getSupportActionBar().setTitle(competitor.getNickname());
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
                if (item.equals(getString(R.string.belt_singles)) || item.equals(getString(R.string.belt_innergeekdom)))
                {
                    continue;
                }
                displayString += item + ", ";
            }
            if (displayString.isEmpty() || displayString.equals(""))
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
