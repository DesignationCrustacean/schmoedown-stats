package com.crustacean.designation.schmoedownstats.model;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Humz on 29/06/2017.
 */
@Parcel
class Faction implements Serializable
{
    private String name, imageName;
    private List<String> currentMembers, pastMembers, teams;
    private Boolean active;

    Faction()
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

    public List<String> getTeams()
    {
        return teams;
    }

    public void setTeams(List<String> teams)
    {
        this.teams = teams;
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
