package com.example.cubsapp.ui.roster;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CareerHittingAPI {
    @GET("named.sport_career_hitting.bam?league_list_id='mlb'&game_type='R'&sport_career_hitting.col_in=g,ab,r,h,d,t,hr,rbi,bb,so,sb,cs,avg,obp,slg,ops&")
    Call<CareerHittingHelp> loadCareerHitting(@Query("player_id") String player_id);
}
