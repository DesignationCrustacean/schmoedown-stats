package com.crustacean.designation.schmoedownstats.model;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Humz on 29/06/2017.
 */
@Parcel
public class Team implements Serializable
{
    private String name, imageName;
    private List<String> currentMembers, pastMembers, currentBelts, pastBelts;
    private Integer wins, starWarsWins, starWarsLosses, starWarsKnockouts, losses, kos, rank;
    private boolean active;

    public Integer getStarWarsWins()
    {
        return starWarsWins;
    }

    public void setStarWarsWins(Integer starWarsWins)
    {
        this.starWarsWins = starWarsWins;
    }

    public Integer getStarWarsLosses()
    {
        return starWarsLosses;
    }

    public void setStarWarsLosses(Integer starWarsLosses)
    {
        this.starWarsLosses = starWarsLosses;
    }

    public Integer getStarWarsKnockouts()
    {
        return starWarsKnockouts;
    }

    public void setStarWarsKnockouts(Integer starWarsKnockouts)
    {
        this.starWarsKnockouts = starWarsKnockouts;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public boolean isCelebrity()
    {
        return celebrity;
    }

    public void setCelebrity(boolean celebrity)
    {
        this.celebrity = celebrity;
    }

    public boolean isStarWars()
    {
        return starWars;
    }

    public void setStarWars(boolean starWars)
    {
        this.starWars = starWars;
    }

    private boolean celebrity;
    private boolean starWars;

    public Team()
    {

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getImageName()
    {
        return imageName;
    }

    public void setImageName(String imageName)
    {
        this.imageName = imageName;
    }

    public List<String> getCurrentMembers()
    {
        return currentMembers;
    }

    public void setCurrentMembers(List<String> currentMembers)
    {
        this.currentMembers = currentMembers;
    }

    public List<String> getPastMembers()
    {
        return pastMembers;
    }

    public void setPastMembers(List<String> pastMembers)
    {
        this.pastMembers = pastMembers;
    }

    public List<String> getCurrentBelts()
    {
        return currentBelts;
    }

    public void setCurrentBelts(List<String> currentBelts)
    {
        this.currentBelts = currentBelts;
    }

    public List<String> getPastBelts()
    {
        return pastBelts;
    }

    public void setPastBelts(List<String> pastBelts)
    {
        this.pastBelts = pastBelts;
    }

    public Integer getWins()
    {
        return wins;
    }

    public void setWins(Integer wins)
    {
        this.wins = wins;
    }

    public Integer getLosses()
    {
        return losses;
    }

    public void setLosses(Integer losses)
    {
        this.losses = losses;
    }

    public Integer getKos()
    {
        return kos;
    }

    public void setKos(Integer kos)
    {
        this.kos = kos;
    }

    public Integer getRank()
    {
        return rank;
    }

    public void setRank(Integer rank)
    {
        this.rank = rank;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

}
