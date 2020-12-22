package com.example.cubsapp.ui.roster;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CareerPitchingAPI {
    @GET("named.sport_career_pitching.bam?league_list_id='mlb'&game_type='R'&sport_career_pitching.col_in=w,l,era,g,gs,sv,ip,so,bb,whip&")
    Call<CareerPitchingHelp> loadCareerPitching(@Query("player_id") String player_id);
}
