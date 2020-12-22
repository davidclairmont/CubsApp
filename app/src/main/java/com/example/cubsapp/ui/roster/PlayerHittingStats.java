package com.example.cubsapp.ui.roster;

import com.google.gson.annotations.SerializedName;

public class PlayerHittingStats {
    @SerializedName("g")
    private String games;

    @SerializedName("ab")
    private String atBats;

    @SerializedName("r")
    private String runs;

    @SerializedName("h")
    private String hits;

    @SerializedName("d")
    private String doubles;

    @SerializedName("t")
    private String triples;

    @SerializedName("hr")
    private String homeruns;

    @SerializedName("rbi")
    private String rbis;

    @SerializedName("bb")
    private String walks;

    @SerializedName("so")
    private String strikeouts;

    @SerializedName("sb")
    private String stolenBases;

    @SerializedName("cs")
    private String caughtStealing;

    @SerializedName("avg")
    private String average;

    @SerializedName("obp")
    private String onBase;

    @SerializedName("slg")
    private String slugging;

    @SerializedName("ops")
    private String ops;

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public String getAtBats() {
        return atBats;
    }

    public void setAtBats(String atBats) {
        this.atBats = atBats;
    }

    public String getRuns() {
        return runs;
    }

    public void setRuns(String runs) {
        this.runs = runs;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getDoubles() {
        return doubles;
    }

    public void setDoubles(String doubles) {
        this.doubles = doubles;
    }

    public String getTriples() {
        return triples;
    }

    public void setTriples(String triples) {
        this.triples = triples;
    }

    public String getHomeruns() {
        return homeruns;
    }

    public void setHomeruns(String homeruns) {
        this.homeruns = homeruns;
    }

    public String getRbis() {
        return rbis;
    }

    public void setRbis(String rbis) {
        this.rbis = rbis;
    }

    public String getWalks() {
        return walks;
    }

    public void setWalks(String walks) {
        this.walks = walks;
    }

    public String getStrikeouts() {
        return strikeouts;
    }

    public void setStrikeouts(String strikeouts) {
        this.strikeouts = strikeouts;
    }

    public String getStolenBases() {
        return stolenBases;
    }

    public void setStolenBases(String stolenBases) {
        this.stolenBases = stolenBases;
    }

    public String getCaughtStealing() {
        return caughtStealing;
    }

    public void setCaughtStealing(String caughtStealing) {
        this.caughtStealing = caughtStealing;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getOnBase() {
        return onBase;
    }

    public void setOnBase(String onBase) {
        this.onBase = onBase;
    }

    public String getSlugging() {
        return slugging;
    }

    public void setSlugging(String slugging) {
        this.slugging = slugging;
    }

    public String getOps() {
        return ops;
    }

    public void setOps(String ops) {
        this.ops = ops;
    }

    public PlayerHittingStats(boolean empty){
        this.games = "0";
        this.atBats = "0";
        this.runs = "0";
        this.hits = "0";
        this.doubles = "0";
        this.triples = "0";
        this.homeruns = "0";
        this.rbis = "0";
        this.walks = "0";
        this.strikeouts = "0";
        this.stolenBases = "0";
        this.caughtStealing = "0";
        this.average = "0";
        this.onBase = "0";
        this.slugging = "0";
        this.ops = "0";
    }

}
