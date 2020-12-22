package com.example.cubsapp.ui.roster;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cubsapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RosterFragment extends Fragment{

    private RosterAdapter rosterAdapter;

    private ArrayList<Player> rosterList;
    private ListView listView;
    static final String BASE_URL = "http://lookup-service-prod.mlb.com/json/";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_roster, container, false);

        listView = (ListView) root.findViewById(R.id.lvRoster);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemClicked(adapterView, view, i, l);
            }
        });

        new queue().startQuery();

        return root;
    }

    // function for displaying individual player data
    public void itemClicked(AdapterView<?> parent, View view, int position, long id){
        // gets clickedPlayer information
        Player clickedPlayer = rosterList.get(position);

        // if a player is a pitcher get pitching stats
        if(clickedPlayer.getPosition_txt().equals("P")){
            System.out.println("Pitcher clicked");
            new pitchingQueue().startQuery(clickedPlayer.getPlayer_id(), clickedPlayer.getName_display_first_last());
            System.out.println(clickedPlayer.getPlayer_id());
        }
        // else player is a hitter, get hitting stats
        else{
            System.out.println("Hitter clicked");
            new hittingQueue().startQuery(clickedPlayer.getPlayer_id(), clickedPlayer.getName_display_first_last());
        }
    }

    // pitching queue class for performing queue to receive player pitching stats
    private class pitchingQueue implements Callback<CareerPitchingHelp>{
        private String playerName;
        private String playerId;

        public void startQuery(String playerId, String playerName) {
            this.playerName = playerName;
            this.playerId = playerId;

            Log.d("RosterFragment", "pitchingQueue");
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            CareerPitchingAPI careerPitchingAPI = retrofit.create(CareerPitchingAPI.class);
            Call<CareerPitchingHelp> call = careerPitchingAPI.loadCareerPitching(playerId);
            call.enqueue(this);
        }

        @Override
        public void onResponse(Call<CareerPitchingHelp> call, Response<CareerPitchingHelp> response) {
            if (response.isSuccessful()) {
                Log.d("RosterFragment", "CareerPitching success");
                System.out.println(response.body());

                // if totalSize = 0, this means the player has no stats at the major league level
                if(response.body().getSport_career_pitching().getQueryResults().getTotalSize().equals("0")){
                    PlayerPitchingStats playerPitchingStats = new PlayerPitchingStats(true);
                    createPitchingStats(playerPitchingStats, playerName, playerId);
                }
                else {
                    PlayerPitchingStats playerPitchingStats = response.body().getSport_career_pitching().getQueryResults().getRow();
                    createPitchingStats(playerPitchingStats, playerName, playerId);
                }
            }
        }

        @Override
        public void onFailure(Call<CareerPitchingHelp> call, Throwable t) {
            Log.d("RosterFragment", "CareerPitching Failure");
            t.printStackTrace();
        }
    }

    // hitting queue class for performing queue to receive player hitting stats
    private class hittingQueue implements Callback<CareerHittingHelp>{
        private String playerName;
        private String playerId;

        public void startQuery(String playerId, String playerName) {
            this.playerName = playerName;
            this.playerId = playerId;

            Log.d("RosterFragment", "hittingQueue");
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            CareerHittingAPI careerHittingAPI = retrofit.create(CareerHittingAPI.class);
            Call<CareerHittingHelp> call = careerHittingAPI.loadCareerHitting(playerId);
            call.enqueue(this);
        }

        @Override
        public void onResponse(Call<CareerHittingHelp> call, Response<CareerHittingHelp> response) {
            if (response.isSuccessful()) {
                Log.d("RosterFragment", "CareerHitting success");

                // if totalSize = 0, this means the player has no stats at the major league level
                if(response.body().getSport_career_hitting().getQueryResults().getTotalSize().equals("0")){
                    PlayerHittingStats playerHittingStats = new PlayerHittingStats(true);
                    createHittingStats(playerHittingStats, playerName, playerId);
                }
                else {
                    PlayerHittingStats playerHittingStats = response.body().getSport_career_hitting().getQueryResults().getRow();
                    createHittingStats(playerHittingStats, playerName, playerId);
                }
            }
        }

        @Override
        public void onFailure(Call<CareerHittingHelp> call, Throwable t) {
            Log.d("RosterFragment", "CareerHitting Failure");
            t.printStackTrace();
        }
    }

    // queue class for receiving all players on roster
    private class queue implements Callback<RosterItem>{
        public void startQuery() {
            Log.d("RosterFragment", "startQuery");

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            RosterAPI rosterAPI = retrofit.create(RosterAPI.class);
            Call<RosterItem> call = rosterAPI.loadAllRosterItems();
            call.enqueue(this);
        }

        @Override
        public void onResponse(Call<RosterItem> call, Response<RosterItem> response) {
            if (response.isSuccessful()) {
                Log.d("RosterFragment", "All players success");
                rosterList = new ArrayList<Player>(response.body().getRoster_40().getQueryResults().getRow());
                rosterAdapter = new RosterAdapter(getActivity(), rosterList);
                listView.setAdapter(rosterAdapter);
            }
        }

        @Override
        public void onFailure(Call<RosterItem> call, Throwable t) {
            Log.d("RosterFragment", "Failure");
            t.printStackTrace();
        }
    }

    // Class to retrieve image from a URL
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error downloading image", e.getMessage());
                try {
                    mIcon11 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.default_player);
                    mIcon11 = Bitmap.createScaledBitmap(mIcon11, 213, 320, true);
                } catch (NullPointerException ee){
                    Log.e("Error finding resource", ee.getMessage());
                }
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    // Class to create pop-up for a hitter's statistics
    public void createHittingStats(PlayerHittingStats playerHittingStats, String playerName, String playerId){
        // create table layout
        TableLayout tableLayout = new TableLayout(getActivity());
        tableLayout.setStretchAllColumns(true);
        tableLayout.setOrientation(LinearLayout.VERTICAL);

        TableRow.LayoutParams llp = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(2, 2, 2, 2);

        // get image to display for player
        String imageUrl = "https://securea.mlb.com/mlb/images/players/head_shot/" + playerId + ".jpg";
        ImageView imageView = new ImageView(getActivity());
        new DownloadImageTask(imageView).execute(imageUrl);

        // TextView for player name
        final TextView name = new TextView(getActivity());
        name.setText(playerName);
        name.setTextColor(Color.BLACK);
        name.setTypeface(null, Typeface.BOLD);
        name.setPadding(6,0,0,0);

        // create row for column field cells
        TableRow row2 = new TableRow(getActivity());
        row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        row2.setPadding(2,2,2,2);

        LinearLayout cell1 = new LinearLayout(getActivity());
        cell1.setBackgroundColor(Color.BLUE);
        cell1.setLayoutParams(llp);
        cell1.setGravity(Gravity.CENTER);
        final TextView year = new TextView(getActivity());
        year.setText("Year");
        year.setTextColor(Color.WHITE);
        cell1.addView(year);
        row2.addView(cell1);

        LinearLayout cell2 = new LinearLayout(getActivity());
        cell2.setBackgroundColor(Color.BLUE);
        cell2.setLayoutParams(llp);
        cell2.setGravity(Gravity.CENTER);
        final TextView ab = new TextView(getActivity());
        ab.setText("AB");
        ab.setTextColor(Color.WHITE);
        cell2.addView(ab);
        row2.addView(cell2);

        LinearLayout cell3 = new LinearLayout(getActivity());
        cell3.setBackgroundColor(Color.BLUE);
        cell3.setLayoutParams(llp);
        cell3.setGravity(Gravity.CENTER);
        final TextView r = new TextView(getActivity());
        r.setText("R");
        r.setTextColor(Color.WHITE);
        cell3.addView(r);
        row2.addView(cell3);

        LinearLayout cell4 = new LinearLayout(getActivity());
        cell4.setBackgroundColor(Color.BLUE);
        cell4.setLayoutParams(llp);
        cell4.setGravity(Gravity.CENTER);
        final TextView h = new TextView(getActivity());
        h.setText("H");
        h.setTextColor(Color.WHITE);
        cell4.addView(h);
        row2.addView(cell4);

        LinearLayout cell5 = new LinearLayout(getActivity());
        cell5.setBackgroundColor(Color.BLUE);
        cell5.setLayoutParams(llp);
        cell5.setGravity(Gravity.CENTER);
        final TextView hr = new TextView(getActivity());
        hr.setText("HR");
        hr.setTextColor(Color.WHITE);
        cell5.addView(hr);
        row2.addView(cell5);

        LinearLayout cell6 = new LinearLayout(getActivity());
        cell6.setBackgroundColor(Color.BLUE);
        cell6.setLayoutParams(llp);
        cell6.setGravity(Gravity.CENTER);
        final TextView rbi = new TextView(getActivity());
        rbi.setText("RBI");
        rbi.setTextColor(Color.WHITE);
        cell6.addView(rbi);
        row2.addView(cell6);

        LinearLayout cell7 = new LinearLayout(getActivity());
        cell7.setBackgroundColor(Color.BLUE);
        cell7.setLayoutParams(llp);
        cell7.setGravity(Gravity.CENTER);
        final TextView sb = new TextView(getActivity());
        sb.setText("SB");
        sb.setTextColor(Color.WHITE);
        cell7.addView(sb);
        row2.addView(cell7);

        LinearLayout cell8 = new LinearLayout(getActivity());
        cell8.setBackgroundColor(Color.BLUE);
        cell8.setLayoutParams(llp);
        cell8.setGravity(Gravity.CENTER);
        final TextView avg = new TextView(getActivity());
        avg.setText("AVG");
        avg.setTextColor(Color.WHITE);
        cell8.addView(avg);
        row2.addView(cell8);

        LinearLayout cell9 = new LinearLayout(getActivity());
        cell9.setBackgroundColor(Color.BLUE);
        cell9.setLayoutParams(llp);
        cell9.setGravity(Gravity.CENTER);
        final TextView obp = new TextView(getActivity());
        obp.setText("OBP");
        obp.setTextColor(Color.WHITE);
        cell9.addView(obp);
        row2.addView(cell9);

        LinearLayout cell10 = new LinearLayout(getActivity());
        cell10.setBackgroundColor(Color.BLUE);
        cell10.setLayoutParams(llp);
        cell10.setGravity(Gravity.CENTER);
        final TextView ops = new TextView(getActivity());
        ops.setText("OPS");
        ops.setTextColor(Color.WHITE);
        cell10.addView(ops);
        row2.addView(cell10);

        // create row for Career stats
        TableRow row3 = new TableRow(getActivity());
        row3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        row3.setPadding(2,0,0,2);

        LinearLayout cellYear = new LinearLayout(getActivity());
        cellYear.setBackgroundColor(Color.BLUE);
        cellYear.setLayoutParams(llp);
        cellYear.setGravity(Gravity.CENTER);
        final TextView career = new TextView(getActivity());
        career.setText("Career");
        career.setTextColor(Color.WHITE);
        cellYear.addView(career);
        row3.addView(cellYear);

        LinearLayout cellAb = new LinearLayout(getActivity());
        cellAb.setBackgroundColor(Color.BLUE);
        cellAb.setLayoutParams(llp);
        cellAb.setGravity(Gravity.CENTER);
        final TextView tvAb = new TextView(getActivity());
        tvAb.setText(playerHittingStats.getAtBats());
        tvAb.setTextColor(Color.WHITE);
        cellAb.addView(tvAb);
        row3.addView(cellAb);


        LinearLayout cellR = new LinearLayout(getActivity());
        cellR.setBackgroundColor(Color.BLUE);
        cellR.setLayoutParams(llp);
        cellR.setGravity(Gravity.CENTER);
        final TextView tvR = new TextView(getActivity());
        tvR.setText(playerHittingStats.getRuns());
        tvR.setTextColor(Color.WHITE);
        cellR.addView(tvR);
        row3.addView(cellR);


        LinearLayout cellH = new LinearLayout(getActivity());
        cellH.setBackgroundColor(Color.BLUE);
        cellH.setLayoutParams(llp);
        cellH.setGravity(Gravity.CENTER);
        final TextView tvH = new TextView(getActivity());
        tvH.setText(playerHittingStats.getHits());
        tvH.setTextColor(Color.WHITE);
        cellH.addView(tvH);
        row3.addView(cellH);


        LinearLayout cellHr = new LinearLayout(getActivity());
        cellHr.setBackgroundColor(Color.BLUE);
        cellHr.setLayoutParams(llp);
        cellHr.setGravity(Gravity.CENTER);
        final TextView tvHr = new TextView(getActivity());
        tvHr.setText(playerHittingStats.getHomeruns());
        tvHr.setTextColor(Color.WHITE);
        cellHr.addView(tvHr);
        row3.addView(cellHr);


        LinearLayout cellRbi = new LinearLayout(getActivity());
        cellRbi.setBackgroundColor(Color.BLUE);
        cellRbi.setLayoutParams(llp);
        cellRbi.setGravity(Gravity.CENTER);
        final TextView tvRbi = new TextView(getActivity());
        tvRbi.setText(playerHittingStats.getRbis());
        tvRbi.setTextColor(Color.WHITE);
        cellRbi.addView(tvRbi);
        row3.addView(cellRbi);


        LinearLayout cellSb = new LinearLayout(getActivity());
        cellSb.setBackgroundColor(Color.BLUE);
        cellSb.setLayoutParams(llp);
        cellSb.setGravity(Gravity.CENTER);
        final TextView tvSb = new TextView(getActivity());
        tvSb.setText(playerHittingStats.getStolenBases());
        tvSb.setTextColor(Color.WHITE);
        cellSb.addView(tvSb);
        row3.addView(cellSb);


        LinearLayout cellAvg = new LinearLayout(getActivity());
        cellAvg.setBackgroundColor(Color.BLUE);
        cellAvg.setLayoutParams(llp);
        cellAvg.setGravity(Gravity.CENTER);
        final TextView tvAvg = new TextView(getActivity());
        tvAvg.setText(playerHittingStats.getAverage());
        tvAvg.setTextColor(Color.WHITE);
        cellAvg.addView(tvAvg);
        row3.addView(cellAvg);


        LinearLayout cellObp = new LinearLayout(getActivity());
        cellObp.setBackgroundColor(Color.BLUE);
        cellObp.setLayoutParams(llp);
        cellObp.setGravity(Gravity.CENTER);
        final TextView tvObp = new TextView(getActivity());
        tvObp.setText(playerHittingStats.getOnBase());
        tvObp.setTextColor(Color.WHITE);
        cellObp.addView(tvObp);
        row3.addView(cellObp);


        LinearLayout cellOps = new LinearLayout(getActivity());
        cellOps.setBackgroundColor(Color.BLUE);
        cellOps.setLayoutParams(llp);
        cellOps.setGravity(Gravity.CENTER);
        final TextView tvOps = new TextView(getActivity());
        tvOps.setText(playerHittingStats.getOps());
        tvOps.setTextColor(Color.WHITE);
        cellOps.addView(tvOps);
        row3.addView(cellOps);

        TableRow emptyRow = new TableRow(getActivity());
        LinearLayout cellEmpty = new LinearLayout(getActivity());
        cellEmpty.setBackgroundColor(Color.WHITE);
        cellEmpty.setLayoutParams(llp);
        cellEmpty.setGravity(Gravity.CENTER);
        emptyRow.addView(cellEmpty);

        tableLayout.addView(emptyRow);
        tableLayout.addView(imageView);
        tableLayout.addView(name);
        tableLayout.addView(row2);
        tableLayout.addView(row3);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(tableLayout)
                .create();
        dialog.show();
    }

    // Class to create popout for a Pitcher's stats
    public void createPitchingStats(PlayerPitchingStats playerPitchingStats, String playerName, String playerId){
        // create table layout
        TableLayout tableLayout = new TableLayout(getActivity());
        tableLayout.setStretchAllColumns(true);
        tableLayout.setOrientation(LinearLayout.VERTICAL);

        TableRow.LayoutParams llp = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(2, 2, 2, 2);

        // get image to display for player
        String imageUrl = "https://securea.mlb.com/mlb/images/players/head_shot/" + playerId + ".jpg";
        ImageView imageView = new ImageView(getActivity());
        new DownloadImageTask(imageView).execute(imageUrl);

        // TextView for player name
        final TextView name = new TextView(getActivity());
        name.setText(playerName);
        name.setTextColor(Color.BLACK);
        name.setTypeface(null, Typeface.BOLD);
        name.setPadding(6,0,0,0);

        // create row for column field cells
        TableRow row2 = new TableRow(getActivity());
        row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        row2.setPadding(2,1,2,1);

        LinearLayout cell1 = new LinearLayout(getActivity());
        cell1.setBackgroundColor(Color.BLUE);
        cell1.setLayoutParams(llp);
        cell1.setGravity(Gravity.CENTER);
        final TextView year = new TextView(getActivity());
        year.setText("Year");
        year.setTextColor(Color.WHITE);
        cell1.addView(year);
        row2.addView(cell1);

        LinearLayout cell2 = new LinearLayout(getActivity());
        cell2.setBackgroundColor(Color.BLUE);
        cell2.setLayoutParams(llp);
        cell2.setGravity(Gravity.CENTER);
        final TextView wins = new TextView(getActivity());
        wins.setText("W");
        wins.setTextColor(Color.WHITE);
        cell2.addView(wins);
        row2.addView(cell2);

        LinearLayout cell3 = new LinearLayout(getActivity());
        cell3.setBackgroundColor(Color.BLUE);
        cell3.setLayoutParams(llp);
        cell3.setGravity(Gravity.CENTER);
        final TextView losses = new TextView(getActivity());
        losses.setText("L");
        losses.setTextColor(Color.WHITE);
        cell3.addView(losses);
        row2.addView(cell3);

        LinearLayout cell4 = new LinearLayout(getActivity());
        cell4.setBackgroundColor(Color.BLUE);
        cell4.setLayoutParams(llp);
        cell4.setGravity(Gravity.CENTER);
        final TextView era = new TextView(getActivity());
        era.setText("ERA");
        era.setTextColor(Color.WHITE);
        cell4.addView(era);
        row2.addView(cell4);

        LinearLayout cell5 = new LinearLayout(getActivity());
        cell5.setBackgroundColor(Color.BLUE);
        cell5.setLayoutParams(llp);
        cell5.setGravity(Gravity.CENTER);
        final TextView games = new TextView(getActivity());
        games.setText("G");
        games.setTextColor(Color.WHITE);
        cell5.addView(games);
        row2.addView(cell5);

        LinearLayout cell6 = new LinearLayout(getActivity());
        cell6.setBackgroundColor(Color.BLUE);
        cell6.setLayoutParams(llp);
        cell6.setGravity(Gravity.CENTER);
        final TextView gamesStarted = new TextView(getActivity());
        gamesStarted.setText("GS");
        gamesStarted.setTextColor(Color.WHITE);
        cell6.addView(gamesStarted);
        row2.addView(cell6);

        LinearLayout cell7 = new LinearLayout(getActivity());
        cell7.setBackgroundColor(Color.BLUE);
        cell7.setLayoutParams(llp);
        cell7.setGravity(Gravity.CENTER);
        final TextView saves = new TextView(getActivity());
        saves.setText("SV");
        saves.setTextColor(Color.WHITE);
        cell7.addView(saves);
        row2.addView(cell7);

        LinearLayout cell8 = new LinearLayout(getActivity());
        cell8.setBackgroundColor(Color.BLUE);
        cell8.setLayoutParams(llp);
        cell8.setGravity(Gravity.CENTER);
        final TextView innings = new TextView(getActivity());
        innings.setText("IP");
        innings.setTextColor(Color.WHITE);
        cell8.addView(innings);
        row2.addView(cell8);

        LinearLayout cell9 = new LinearLayout(getActivity());
        cell9.setBackgroundColor(Color.BLUE);
        cell9.setLayoutParams(llp);
        cell9.setGravity(Gravity.CENTER);
        final TextView strikeouts = new TextView(getActivity());
        strikeouts.setTextColor(Color.WHITE);
        strikeouts.setText("SO");
        cell9.addView(strikeouts);
        row2.addView(cell9);

        LinearLayout cell10 = new LinearLayout(getActivity());
        cell10.setBackgroundColor(Color.BLUE);
        cell10.setLayoutParams(llp);
        cell10.setGravity(Gravity.CENTER);
        final TextView walks = new TextView(getActivity());
        walks.setText("BB");
        walks.setTextColor(Color.WHITE);
        cell10.addView(walks);
        row2.addView(cell10);

        LinearLayout cell11 = new LinearLayout(getActivity());
        cell11.setBackgroundColor(Color.BLUE);
        cell11.setLayoutParams(llp);
        cell11.setGravity(Gravity.CENTER);
        final TextView whip = new TextView(getActivity());
        whip.setText("WHIP");
        whip.setTextColor(Color.WHITE);
        cell11.addView(whip);
        row2.addView(cell11);

        // create row for career stat cells
        TableRow row3 = new TableRow(getActivity());
        row3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        row3.setPadding(2,1,2,1);

        LinearLayout careerCell = new LinearLayout(getActivity());
        careerCell.setBackgroundColor(Color.BLUE);
        careerCell.setLayoutParams(llp);
        careerCell.setGravity(Gravity.CENTER);
        final TextView career = new TextView(getActivity());
        career.setText("Career");
        career.setTextColor(Color.WHITE);
        careerCell.addView(career);
        row3.addView(careerCell);

        LinearLayout winsCell = new LinearLayout(getActivity());
        winsCell.setBackgroundColor(Color.BLUE);
        winsCell.setLayoutParams(llp);
        winsCell.setGravity(Gravity.CENTER);
        final TextView w = new TextView(getActivity());
        w.setText(playerPitchingStats.getWins());
        w.setTextColor(Color.WHITE);
        winsCell.addView(w);
        row3.addView(winsCell);

        LinearLayout lossCell = new LinearLayout(getActivity());
        lossCell.setBackgroundColor(Color.BLUE);
        lossCell.setLayoutParams(llp);
        lossCell.setGravity(Gravity.CENTER);
        final TextView l = new TextView(getActivity());
        l.setTextColor(Color.WHITE);
        l.setText(playerPitchingStats.getLosses());
        lossCell.addView(l);
        row3.addView(lossCell);

        LinearLayout eraCell = new LinearLayout(getActivity());
        eraCell.setBackgroundColor(Color.BLUE);
        eraCell.setLayoutParams(llp);
        eraCell.setGravity(Gravity.CENTER);
        final TextView _era = new TextView(getActivity());
        _era.setText(playerPitchingStats.getEra());
        _era.setTextColor(Color.WHITE);
        eraCell.addView(_era);
        row3.addView(eraCell);

        LinearLayout gamesCell = new LinearLayout(getActivity());
        gamesCell.setBackgroundColor(Color.BLUE);
        gamesCell.setLayoutParams(llp);
        gamesCell.setGravity(Gravity.CENTER);
        final TextView g = new TextView(getActivity());
        g.setTextColor(Color.WHITE);
        g.setText(playerPitchingStats.getGames());
        gamesCell.addView(g);
        row3.addView(gamesCell);

        LinearLayout gamesStartedCell = new LinearLayout(getActivity());
        gamesStartedCell.setBackgroundColor(Color.BLUE);
        gamesStartedCell.setLayoutParams(llp);
        gamesStartedCell.setGravity(Gravity.CENTER);
        final TextView gs = new TextView(getActivity());
        gs.setText(playerPitchingStats.getGamesStarted());
        gs.setTextColor(Color.WHITE);
        gamesStartedCell.addView(gs);
        row3.addView(gamesStartedCell);

        LinearLayout savesCell = new LinearLayout(getActivity());
        savesCell.setBackgroundColor(Color.BLUE);
        savesCell.setLayoutParams(llp);
        savesCell.setGravity(Gravity.CENTER);
        final TextView sv = new TextView(getActivity());
        sv.setTextColor(Color.WHITE);
        sv.setText(playerPitchingStats.getSaves());
        savesCell.addView(sv);
        row3.addView(savesCell);

        LinearLayout ipCell = new LinearLayout(getActivity());
        ipCell.setBackgroundColor(Color.BLUE);
        ipCell.setLayoutParams(llp);
        ipCell.setGravity(Gravity.CENTER);
        final TextView ip = new TextView(getActivity());
        ip.setText(playerPitchingStats.getInnings());
        ip.setTextColor(Color.WHITE);
        ipCell.addView(ip);
        row3.addView(ipCell);

        LinearLayout soCell = new LinearLayout(getActivity());
        soCell.setBackgroundColor(Color.BLUE);
        soCell.setLayoutParams(llp);
        soCell.setGravity(Gravity.CENTER);
        final TextView so = new TextView(getActivity());
        so.setTextColor(Color.WHITE);
        so.setText(playerPitchingStats.getStrikeouts());
        soCell.addView(so);
        row3.addView(soCell);

        LinearLayout bbCell = new LinearLayout(getActivity());
        bbCell.setBackgroundColor(Color.BLUE);
        bbCell.setLayoutParams(llp);
        bbCell.setGravity(Gravity.CENTER);
        final TextView bb = new TextView(getActivity());
        bb.setTextColor(Color.WHITE);
        bb.setText(playerPitchingStats.getWalks());
        bbCell.addView(bb);
        row3.addView(bbCell);

        LinearLayout whipCell = new LinearLayout(getActivity());
        whipCell.setBackgroundColor(Color.BLUE);
        whipCell.setLayoutParams(llp);
        whipCell.setGravity(Gravity.CENTER);
        final TextView whp = new TextView(getActivity());
        whp.setText(playerPitchingStats.getWhip());
        whp.setTextColor(Color.WHITE);
        whipCell.addView(whp);
        row3.addView(whipCell);

        TableRow emptyRow = new TableRow(getActivity());
        LinearLayout cellEmpty = new LinearLayout(getActivity());
        cellEmpty.setBackgroundColor(Color.WHITE);
        cellEmpty.setLayoutParams(llp);
        cellEmpty.setGravity(Gravity.CENTER);
        emptyRow.addView(cellEmpty);

        tableLayout.addView(emptyRow);
        tableLayout.addView(imageView);
        tableLayout.addView(name);
        tableLayout.addView(row2);
        tableLayout.addView(row3);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(tableLayout)
                .create();
        dialog.show();
    }

    protected static final String noJersey = "N/A";

    // Adapter used to insert Players into the ListView
    protected class RosterAdapter extends  ArrayAdapter<Player>{

        public RosterAdapter(Context context, ArrayList<Player> players) {
            super(context, 0 , players);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            // Get the data item for this position
            Player player = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.roster_item_layout, parent, false);
            }
            // Lookup view for data population
            TextView tvPlayerName = convertView.findViewById(R.id.tvPlayerName);
            TextView tvPlayerPosition = convertView.findViewById(R.id.tvPlayerPosition);
            TextView tvPlayerNumber = convertView.findViewById(R.id.tvPlayerNumber);
            TextView tvPlayerBatsThrows = convertView.findViewById(R.id.tvPlayerBatsThrows);

            String imageUrl = "https://securea.mlb.com/mlb/images/players/head_shot/" + player.getPlayer_id() + ".jpg";
            ImageView imageView = convertView.findViewById(R.id.ivPlayer);
            new DownloadImageTask(imageView).execute(imageUrl);

            tvPlayerName.setText(player.getName_display_first_last());
            tvPlayerName.setTextColor(Color.BLACK);
            tvPlayerPosition.setText(player.getPosition_txt());

            if(player.getJersey_number().isEmpty()){
                tvPlayerNumber.setText(String.format("Number: %s", noJersey));
            }
            else{tvPlayerNumber.setText(String.format("Number: %s", player.getJersey_number()));}

            tvPlayerBatsThrows.setText(String.format("Bats/Throws: %s/%s", player.getBats(), player.getThrw()));

            // Return the completed view to render on screen

            return convertView;
        }
    }
}