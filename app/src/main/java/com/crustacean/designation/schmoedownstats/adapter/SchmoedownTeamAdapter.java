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
import com.crustacean.designation.schmoedownstats.activity.TeamDetailActivity;
import com.crustacean.designation.schmoedownstats.model.Team;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Humz on 02/07/2017.
 */
public class SchmoedownTeamAdapter extends RecyclerView.Adapter<SchmoedownTeamAdapter.ShmoeHolder>
{
    private Context mContext;
    private final ArrayList<Team> mTeams;
    private final String mCompetition;

    public SchmoedownTeamAdapter(ArrayList<Team> teamsList, String mCompetition)
    {
        this.mTeams = teamsList;
        this.mCompetition = mCompetition;
    }

    @Override
    public ShmoeHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        mContext = parent.getContext();
        int listItemLayoutId = R.layout.list_item_schmoedown;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(listItemLayoutId, parent, false);

        return new ShmoeHolder(view);
    }

    @Override
    public void onBindViewHolder(ShmoeHolder holder, int position)
    {
        Team team = mTeams.get(position);

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

        Picasso.with(mContext).load(mContext.getString(R.string.img_base_url) + team.getImageName() + mContext.getString(R.string.icon)).placeholder(R.color.darkerGrey).into(holder.teamImage);
        holder.teamImage.setContentDescription(team.getName());

        holder.nickname.setText(team.getName());
        holder.wins.setText(String.valueOf(team.getWins()));
        holder.losses.setText(String.valueOf(team.getLosses()));
        if (mCompetition.equals(mContext.getString(R.string.key_team)))
        {
            holder.rank.setText(String.valueOf(team.getRank()));

            if (team.getCurrentBelts() != null && team.getCurrentBelts().contains(mContext.getString(R.string.belt_team)))
            {
                holder.beltBarLayout.setVisibility(View.VISIBLE);
            }
        }
        if (mCompetition.equals(mContext.getString(R.string.key_star_wars)))
        {
            if (team.getCurrentBelts() != null && team.getCurrentBelts().contains(mContext.getString(R.string.belt_starwars_team)))
            {
                holder.beltBarLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        if (mTeams != null && !mTeams.isEmpty())
        {
            return mTeams.size();
        }
        return 0;
    }

    public class ShmoeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        final ImageView teamImage;
        final TextView nickname, wins, losses, rank;
        final LinearLayout recordLayout, layout;
        final FrameLayout beltBarLayout;

        public ShmoeHolder(View itemView)
        {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.list_item_layout);
            teamImage = (ImageView) itemView.findViewById(R.id.iv_image);
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
            Intent intent = new Intent(mContext, TeamDetailActivity.class);
            intent.putExtra(mContext.getString(R.string.key_team), Parcels.wrap(mTeams.get(getAdapterPosition())));

            mContext.startActivity(intent);
        }
    }
}
