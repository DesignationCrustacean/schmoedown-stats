package com.crustacean.designation.schmoedownstats.model;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Humz on 29/06/2017.
 */
@Parcel
public class Competitor implements Serializable
{
    private String firstName, surname, nickname, imageName;
    private Integer wins, losses, kos, rank, geekWins, geekLosses, geekKnockouts, geekRank, starWarsWins, starWarsLosses, starWarsKnockouts;
    private List<String> currentBelts, pastBelts, currentTeams, pastTeams, currentFactions, pastFactions;
    private Boolean active, innergeekdom, starWars, celebrity;

    public Competitor()
    {

    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getImageName()
    {
        return imageName;
    }

    public void setImageName(String imageName)
    {
        this.imageName = imageName;
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

    public Integer getGeekWins()
    {
        return geekWins;
    }

    public void setGeekWins(Integer geekWins)
    {
        this.geekWins = geekWins;
    }

    public Integer getGeekLosses()
    {
        return geekLosses;
    }

    public void setGeekLosses(Integer geekLosses)
    {
        this.geekLosses = geekLosses;
    }

    public Integer getGeekKnockouts()
    {
        return geekKnockouts;
    }

    public void setGeekKnockouts(Integer geekKnockouts)
    {
        this.geekKnockouts = geekKnockouts;
    }

    public Integer getGeekRank()
    {
        return geekRank;
    }

    public void setGeekRank(Integer geekRank)
    {
        this.geekRank = geekRank;
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

    public List<String> getCurrentTeams()
    {
        return currentTeams;
    }

    public void setCurrentTeams(List<String> currentTeams)
    {
        this.currentTeams = currentTeams;
    }

    public List<String> getPastTeams()
    {
        return pastTeams;
    }

    public void setPastTeams(List<String> pastTeams)
    {
        this.pastTeams = pastTeams;
    }

    public List<String> getCurrentFactions()
    {
        return currentFactions;
    }

    public void setCurrentFactions(List<String> currentFactions)
    {
        this.currentFactions = currentFactions;
    }

    public List<String> getPastFactions()
    {
        return pastFactions;
    }

    public void setPastFactions(List<String> pastFactions)
    {
        this.pastFactions = pastFactions;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    public Boolean getInnergeekdom()
    {
        return innergeekdom;
    }

    public void setInnergeekdom(Boolean innergeekdom)
    {
        this.innergeekdom = innergeekdom;
    }

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

    public Boolean getStarWars()
    {
        return starWars;
    }

    public void setStarWars(Boolean starWars)
    {
        this.starWars = starWars;
    }

    public Boolean isCelebrity()
    {
        return celebrity;
    }

    public void setCelebrity(Boolean celebrity)
    {
        this.celebrity = celebrity;
    }

}
