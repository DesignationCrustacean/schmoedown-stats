package com.crustacean.designation.schmoedownstats.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crustacean.designation.schmoedownstats.R;
import com.crustacean.designation.schmoedownstats.activity.CompetitorDetailActivity;
import com.crustacean.designation.schmoedownstats.activity.InnergeekdomDetailActivity;
import com.crustacean.designation.schmoedownstats.activity.StarWarsDetailActivity;
import com.crustacean.designation.schmoedownstats.activity.TeamDetailActivity;
import com.crustacean.designation.schmoedownstats.model.Competitor;
import com.crustacean.designation.schmoedownstats.model.Team;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Humz on 02/07/2017.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultHolder>
{
    String competitorString;
    private Context mContext;
    private final ArrayList<Object> mCompetitorsTeams;

    public SearchAdapter(ArrayList<Object> competitorTeamList)
    {
        this.mCompetitorsTeams = competitorTeamList;
    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        mContext = parent.getContext();
        competitorString = mContext.getString(R.string.key_competitor);
        int listItemLayoutId = R.layout.list_item_search;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(listItemLayoutId, parent, false);

        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position)
    {
        Object object = mCompetitorsTeams.get(position);

        if(object instanceof Competitor)
        {
            Competitor competitor = (Competitor) object;
            holder.name.setText(competitor.getNickname());
        }
        else if(object instanceof Team)
        {
            Team team = (Team) object;
            holder.name.setText(team.getName());
        }
        if(position%2 == 0)
        {
            holder.layout.setBackgroundResource(R.drawable.light_list_item_selector);
        }
        else
        {
            holder.layout.setBackgroundResource(R.drawable.dark_list_item_selector);
        }
    }

    @Override
    public int getItemCount()
    {
        if(mCompetitorsTeams != null && !mCompetitorsTeams.isEmpty())
        {
            return mCompetitorsTeams.size();
        }
        return 0;
    }

    public class ResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        final TextView name;
        final LinearLayout layout;

        public ResultHolder(View itemView)
        {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_search_name);
            layout = (LinearLayout) itemView.findViewById(R.id.ll_search);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            Intent intent = null;
            Object object = mCompetitorsTeams.get(getAdapterPosition());
            if(object instanceof Competitor)
            {
                Competitor competitor = (Competitor) object;
                ArrayList<CharSequence> charSequenceArrayList = new ArrayList<>();
                if(competitor.getWins()+competitor.getLosses()>0)
                {
                    charSequenceArrayList.add(mContext.getString(R.string.schmoedown));
                    intent = new Intent(mContext, CompetitorDetailActivity.class);
                }
                if(competitor.getInnergeekdom())
                {
                    charSequenceArrayList.add(mContext.getString(R.string.innergeekdom));
                    intent = new Intent(mContext, InnergeekdomDetailActivity.class);
                }
                if(competitor.getStarWars())
                {
                    charSequenceArrayList.add(mContext.getString(R.string.star_wars));
                    intent = new Intent(mContext, StarWarsDetailActivity.class);
                }
                if(charSequenceArrayList.size() > 1)
                {
                    createDialog(charSequenceArrayList.toArray(new CharSequence[charSequenceArrayList.size()]), competitor);
                }
                else
                {
                    intent.putExtra(competitorString, Parcels.wrap(object));
                    mContext.startActivity(intent);
                }
            }
            else if(object instanceof  Team)
            {
                intent = new Intent(mContext, TeamDetailActivity.class);
                intent.putExtra(mContext.getString(R.string.key_team), Parcels.wrap(object));
                mContext.startActivity(intent);
            }

        }

        private void createDialog(final CharSequence[] competitionList, final Competitor competitor)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(mContext.getString(R.string.which_profile));
            builder.setItems(competitionList, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Intent intent = null;

                    CharSequence charSequence = competitionList[item];

                    if(charSequence.equals(mContext.getString(R.string.schmoedown)))
                    {
                        intent = new Intent(mContext, CompetitorDetailActivity.class);
                    }
                    else if(charSequence.equals(mContext.getString(R.string.innergeekdom)))
                    {
                        intent = new Intent(mContext, InnergeekdomDetailActivity.class);
                    }
                    else if(charSequence.equals(mContext.getString(R.string.star_wars)))
                    {
                        intent = new Intent(mContext, StarWarsDetailActivity.class);
                    }

                    intent.putExtra(competitorString, Parcels.wrap(competitor));
                    mContext.startActivity(intent);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
