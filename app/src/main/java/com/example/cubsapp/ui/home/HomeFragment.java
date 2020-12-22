package com.example.cubsapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cubsapp.R;
import com.example.cubsapp.ui.news.NewsFragment;
import com.example.cubsapp.ui.news.RssFeedListAdapter;
import com.example.cubsapp.ui.roster.Player;
import com.example.cubsapp.ui.roster.RosterFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private HistoryAdapter historyAdapter;
    private List<HistoryHelper> historyList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = root.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        historyList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(getActivity(), historyList);
        mRecyclerView.setAdapter(historyAdapter);

        new ParseHtml().execute();

        return root;
    }

    // class used to get the 'On this day in history' facts
    private class ParseHtml extends AsyncTask<Void, Void, Boolean> {
        String urlLink = "https://www.nationalpastime.com/site/index.php?action=baseball_team_search&baseball_team=Chicago+Cubs";

        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                Document document = Jsoup.connect(urlLink).get();
                Elements elements = document.select("tr");
                for (Element element: elements) {
                    if(element.hasAttr("valign")) {
                        Elements children = element.getElementsByAttribute("width");
                        List<String> stringList = children.eachText();
                        String date = stringList.get(0);
                        String description = stringList.get(1);
                        System.out.println(date + "\n");
                        System.out.println(description + "\n");
                        HistoryHelper historyHelper = new HistoryHelper(date, description);
                        historyList.add(historyHelper);
                    }
                }

                return true;
            } catch(IOException e){
                Log.d("ParseHtml","Error parsing html");
                System.out.println(e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Fill RecyclerView
                historyAdapter = new HistoryAdapter(getContext(), historyList);
                mRecyclerView.setAdapter(historyAdapter);
            } else {
                Log.d("Home fragment","ParseHtml not successful");
            }
        }
    }

    // class used to store fact information
    public class HistoryHelper{
        public String date;
        public String description;

        public HistoryHelper(String date, String description){
            this.date = date;
            this.description = description;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    // adapter used to display HistoryHelper objects into the RecyclerView
    protected class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.FeedModelViewHolder2> {

        private List<HistoryHelper> historyHelpers;
        private Context context;

        public class FeedModelViewHolder2 extends RecyclerView.ViewHolder {
            private View feedView;

            public FeedModelViewHolder2(View v) {
                super(v);
                feedView = v;
            }
        }

        public HistoryAdapter(Context context, List<HistoryHelper> historyHelpers) {
            this.context = context;
            this.historyHelpers = historyHelpers;
        }

        @NonNull
        @Override
        public FeedModelViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_layout, parent, false);
            FeedModelViewHolder2 holder = new FeedModelViewHolder2(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull FeedModelViewHolder2 holder, int position) {
            final HistoryHelper historyHelper = historyHelpers.get(position);

            ((TextView)holder.feedView.findViewById(R.id.dateText)).setText(historyHelper.getDate());
            ((TextView)holder.feedView.findViewById(R.id.descriptionText)).setText(historyHelper.getDescription());
        }

        @Override
        public int getItemCount() {
            return historyHelpers.size();
        }
    }
}