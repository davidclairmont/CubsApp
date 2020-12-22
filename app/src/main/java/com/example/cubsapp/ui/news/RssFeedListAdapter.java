package com.example.cubsapp.ui.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cubsapp.R;

import java.io.InputStream;
import java.util.List;

// Adapter class for inserting RssFeedModel objects into the RecyclerView
// Also uses the provided image links to download the news images from the internet and insert them into the view
public class RssFeedListAdapter extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

    private List<NewsFragment.RssFeedModel> mRssFeedModels;
    private List<String> imageList;
    private Context context;

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }
    }

    public RssFeedListAdapter(Context context, List<NewsFragment.RssFeedModel> rssFeedModels, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;
        mRssFeedModels = rssFeedModels;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_layout, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final NewsFragment.RssFeedModel rssFeedModel = mRssFeedModels.get(position);
        final String imageUrl= imageList.get(position);

        ImageView img = (holder.rssFeedView.findViewById(R.id.ivNews));
        new DownloadImageTask(img).execute(imageUrl);

        ((TextView)holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.title);
        if(rssFeedModel.description.isEmpty()){
            ((TextView)holder.rssFeedView.findViewById(R.id.creatorText)).setText("Author: N/A");
        } else{
            ((TextView)holder.rssFeedView.findViewById(R.id.creatorText)).setText(String.format("Author: %s", rssFeedModel.description));
        }
        ((TextView)holder.rssFeedView.findViewById(R.id.dateText)).setText(String.format("Date posted: %s", rssFeedModel.date));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssFeedModel.link));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
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
                //mIcon11 = Bitmap.createScaledBitmap(mIcon11, 320, 320, true);
            }
            mIcon11 = Bitmap.createScaledBitmap(mIcon11, 1000, 500, true);
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
