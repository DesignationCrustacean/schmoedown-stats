package com.crustacean.designation.schmoedownstats.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crustacean.designation.schmoedownstats.R;
import com.crustacean.designation.schmoedownstats.activity.CompetitorDetailActivity;
import com.crustacean.designation.schmoedownstats.activity.InnergeekdomDetailActivity;
import com.crustacean.designation.schmoedownstats.activity.StarWarsDetailActivity;
import com.crustacean.designation.schmoedownstats.model.Competitor;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Humz on 02/07/2017.
 */
public class SchmoedownAdapter extends RecyclerView.Adapter<SchmoedownAdapter.ShmoeHolder>
{
    private Context mContext;
    private final ArrayList<Competitor> mCompetitors;
    private final String competition;
    private String schmoedown;
    private String celebrity;
    private String innergeekdom;
    private String starWars;

    public SchmoedownAdapter(ArrayList<Competitor> competitorList, String competition)
    {
        this.mCompetitors = competitorList;
        this.competition = competition;
    }

    @Override
    public ShmoeHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        mContext = parent.getContext();
        schmoedown = mContext.getString(R.string.key_schmoedown);
        celebrity = mContext.getString(R.string.key_celebrity);
        innergeekdom = mContext.getString(R.string.key_innergeekdom);
        starWars = mContext.getString(R.string.key_star_wars);
        int listItemLayoutId = R.layout.list_item_schmoedown;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(listItemLayoutId, parent, false);

        return new ShmoeHolder(view);
    }

    @Override
    public void onBindViewHolder(ShmoeHolder holder, int position)
    {
        Competitor competitor = mCompetitors.get(position);

        if (position % 2 == 0)
        {
            Drawable background = holder.recordLayout.getBackground();
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(ContextCompat.getColor(mContext, R.color.defaultBackground));
            holder.layout.setBackgroundResource(R.drawable.light_list_item_selector);
        }
        else
        {
            Drawable background = holder.recordLayout.getBackground();
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(Color.WHITE);

            holder.layout.setBackgroundResource(R.drawable.dark_list_item_selector);
        }

        Picasso.with(mContext).load(mContext.getString(R.string.img_base_url) + competitor.getImageName() + mContext.getString(R.string.icon)).placeholder(R.color.darkerGrey).into(holder.competitorImage);
        String fullName = competitor.getFirstName() + " " + competitor.getSurname();
        holder.competitorImage.setContentDescription(fullName);

        holder.nickname.setText(competitor.getNickname());
        String beltName = null;
        if (competition.equals(schmoedown) || competition.equals(celebrity))
        {
            holder.wins.setText(String.valueOf(competitor.getWins()));
            holder.losses.setText(String.valueOf(competitor.getLosses()));
            if (competition.equals(schmoedown))
            {
                holder.rank.setText(String.valueOf(competitor.getRank()));
                beltName = mContext.getString(R.string.belt_singles);
            }
        }
        else if (competition.equals(innergeekdom))
        {
            holder.wins.setText(String.valueOf(competitor.getGeekWins()));
            holder.losses.setText(String.valueOf(competitor.getGeekLosses()));
            holder.rank.setText(String.valueOf(competitor.getGeekRank()));
            beltName = mContext.getString(R.string.belt_innergeekdom);
        }
        else if (competition.equals(starWars))
        {
            holder.wins.setText(String.valueOf(competitor.getStarWarsWins()));
            holder.losses.setText(String.valueOf(competitor.getStarWarsLosses()));
            beltName = mContext.getString(R.string.belt_starwars);
        }

        if (competitor.getCurrentBelts() != null && competitor.getCurrentBelts().contains(beltName))
        {
            holder.beltBarLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount()
    {
        if (mCompetitors != null && !mCompetitors.isEmpty())
        {
            return mCompetitors.size();
        }
        return 0;
    }

    public class ShmoeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        final ImageView competitorImage;
        final TextView nickname, wins, losses, rank;
        final LinearLayout layout, recordLayout;
        final FrameLayout beltBarLayout;

        public ShmoeHolder(View itemView)
        {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.list_item_layout);
            competitorImage = (ImageView) itemView.findViewById(R.id.iv_image);
            nickname = (TextView) itemView.findViewById(R.id.tv_name);
            wins = (TextView) itemView.findViewById(R.id.tv_wins);
            losses = (TextView) itemView.findViewById(R.id.tv_losses);
            rank = (TextView) itemView.findViewById(R.id.tv_rank);
            recordLayout = (LinearLayout) itemView.findViewById(R.id.layout_records);
            beltBarLayout = (FrameLayout) itemView.findViewById(R.id.layout_belt_bar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            Intent intent = null;
            if (competition.equals(schmoedown))
            {
                intent = new Intent(mContext, CompetitorDetailActivity.class);
            }
            else if (competition.equals(innergeekdom))
            {
                intent = new Intent(mContext, InnergeekdomDetailActivity.class);
            }
            else if (competition.equals(celebrity))
            {
                intent = new Intent(mContext, CompetitorDetailActivity.class);
            }
            else if (competition.equals(starWars))
            {
                intent = new Intent(mContext, StarWarsDetailActivity.class);
            }
            intent.putExtra(mContext.getString(R.string.key_competitor), Parcels.wrap(mCompetitors.get(getAdapterPosition())));

            mContext.startActivity(intent);
        }
    }
}
