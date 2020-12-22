package com.example.cubsapp.ui.roster;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// performs query to receive a roster item
public interface RosterAPI {
    @GET("named.roster_40.bam?team_id=112&roster_40.col_in=position_txt,weight,name_display_first_last,height_inches,jersey_number,bats,height_feet,throws,player_id")
    Call <RosterItem> loadAllRosterItems();
}
