package com.example.cubsapp.ui.roster;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player {
    @SerializedName("position_txt") private String position_txt;
    private String weight;
    private String name_display_first_last;
    private String height_inches;
    private String jersey_number;
    private String bats;
    private String height_feet;

    @SerializedName("throws")
    private String thrw;
    private String player_id;

    public String getPosition_txt(){
        return position_txt;
    }

    public void setPosition_txt(String position_txt) {
        this.position_txt = position_txt;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getName_display_first_last() {
        return name_display_first_last;
    }

    public void setName_display_first_last(String name_display_first_last) {
        this.name_display_first_last = name_display_first_last;
    }

    public String getHeight_feet() {
        return height_feet;
    }

    public void setHeight_feet(String height_feet) {
        this.height_feet = height_feet;
    }

    public String getHeight_inches() {
        return height_inches;
    }

    public void setHeight_inches(String height_inches) {
        this.height_inches = height_inches;
    }

    public String getJersey_number() {
        return jersey_number;
    }

    public void setJersey_number(String jersey_number) {
        this.jersey_number = jersey_number;
    }

    public String getBats() {
        return bats;
    }

    public void setBats(String bats) {
        this.bats = bats;
    }

    public String getThrw() {
        return thrw;
    }

    public void setThrw(String thrw) {
        this.thrw = thrw;
    }

    public String getPlayer_id() { return player_id; }

    public void setPlayer_id(String player_id) { this.player_id = player_id; }
}
