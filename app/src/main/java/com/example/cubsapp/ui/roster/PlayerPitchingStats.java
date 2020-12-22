package com.example.cubsapp.ui.roster;

import com.google.gson.annotations.SerializedName;

public class PlayerPitchingStats {
    @SerializedName("w")
    private String wins;

    @SerializedName("l")
    private String losses;

    @SerializedName("era")
    private String era;

    @SerializedName("g")
    private String games;

    @SerializedName("gs")
    private String gamesStarted;

    @SerializedName("sv")
    private String saves;

    @SerializedName("ip")
    private String innings;

    @SerializedName("so")
    private String strikeouts;

    @SerializedName("bb")
    private String walks;

    @SerializedName("whip")
    private String whip;

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getLosses() {
        return losses;
    }

    public void setLosses(String losses) {
        this.losses = losses;
    }

    public String getEra() {
        return era;
    }

    public void setEra(String era) {
        this.era = era;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public String getGamesStarted() {
        return gamesStarted;
    }

    public void setGamesStarted(String gamesStarted) {
        this.gamesStarted = gamesStarted;
    }

    public String getSaves() {
        return saves;
    }

    public void setSaves(String saves) {
        this.saves = saves;
    }

    public String getInnings() {
        return innings;
    }

    public void setInnings(String innings) {
        this.innings = innings;
    }

    public String getStrikeouts() {
        return strikeouts;
    }

    public void setStrikeouts(String strikeouts) {
        this.strikeouts = strikeouts;
    }

    public String getWalks() {
        return walks;
    }

    public void setWalks(String walks) {
        this.walks = walks;
    }

    public String getWhip() {
        return whip;
    }

    public void setWhip(String whip) {
        this.whip = whip;
    }

    public PlayerPitchingStats(boolean empty){
        this.wins = "0";
        this.losses = "0";
        this.era = "0";
        this.games = "0";
        this.gamesStarted = "0";
        this.saves = "0";
        this.innings = "0";
        this.strikeouts = "0";
        this.walks = "0";
        this.whip = "0";
    }
}
