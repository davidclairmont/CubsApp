package com.example.cubsapp.ui.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cubsapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;

    private List<RssFeedModel> mFeedModelList;
    private RssFeedListAdapter rssFeedListAdapter;

    private List<String> imageList;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("NewsFragment", "onCreateView");
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        mSwipeLayout = (SwipeRefreshLayout)  root.findViewById(R.id.swipeRefreshLayout);

        TextView tvFeedTitle = (TextView) root.findViewById(R.id.titleText);
        TextView tvFeedAuthor = (TextView) root.findViewById(R.id.creatorText);
        TextView tvFeedDate = (TextView) root.findViewById(R.id.dateText);

        //recycler
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // define an adapter
        mFeedModelList = new ArrayList<>();
        rssFeedListAdapter = new RssFeedListAdapter(getContext() ,mFeedModelList, imageList);
        mRecyclerView.setAdapter(rssFeedListAdapter);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask().execute((Void) null);
            }
        });

        new FetchFeedTask().execute((Void) null);

        return root;
    }

    // class used to fetch the news and pictures. sets RecyclerView adapter upon completion
    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink = "https://www.mlb.com/cubs/feeds/news/rss.xml";
        private String jsonLink = "https://dapi.cms.mlbinfra.com/v2/content/en-us/sel-t112-news-list";

        @Override
        protected void onPreExecute() {
            mSwipeLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = parseFeed(inputStream);

                URL url2 = new URL(jsonLink);
                InputStream inputStream2 = url2.openConnection().getInputStream();
                imageList = getImageLinks(inputStream2);

                return true;
            } catch (IOException | JSONException | XmlPullParserException e) {
                Log.e("doInBackground", "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeLayout.setRefreshing(false);

            if (success) {
                // Fill RecyclerView
                rssFeedListAdapter = new RssFeedListAdapter(getContext(), mFeedModelList, imageList);
                mRecyclerView.setAdapter(rssFeedListAdapter);
            } else {
                Toast.makeText(getActivity(),
                        "Enter a valid Rss feed url",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    // function used to get navigate JSON and get all the images used in the articles
    public List<String> getImageLinks(InputStream inputStream) throws IOException, JSONException {
        String picture;
        List<String> imageLinks = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();

            String input;
            while ((input = bufferedReader.readLine()) != null) {
                stringBuilder.append(input);
            }
            JSONObject json = new JSONObject(stringBuilder.toString());
            JSONArray jArray = json.getJSONArray("items");
            System.out.println(jArray.length());
            for(int i = 0; i<jArray.length(); i++){
                JSONObject item = (JSONObject) jArray.get(i);
                JSONObject pictureJson = (JSONObject) item.get("thumbnail");
                picture = (String) pictureJson.get("thumbnailUrl").toString();
                picture = picture.replace("w_250,h_250,", "");
                imageLinks.add(picture);
            }

            return imageLinks;
        } finally {
            inputStream.close();
        }

    }

    // ParseFeed function used to parse the RSS document containing Cubs news
    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String title = null;
        String link = null;
        String author = null;
        String date = null;
        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        Log.d("NewsFragment","parseFeed");

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    System.out.println(isItem);
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("dc:creator")) {
                    author = result;
                } else if (name.equalsIgnoreCase("mlb:display-date")) {
                    date = result;
                }

                if (title != null && link != null && author != null && date != null) {
                    if(isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, author, date);
                        items.add(item);
                    }

                    title = null;
                    link = null;
                    author = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }

    // Object used to store each news article
    public class RssFeedModel {

        public String title;
        public String link;
        public String description;
        public String date;

        public RssFeedModel(String title, String link, String description, String date) {
            this.title = title;
            this.link = link;
            this.description = description;
            this.date = date;
        }
    }
}

