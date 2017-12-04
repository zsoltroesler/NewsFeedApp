package com.example.android.newsfeedapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Zsolt on 2017. 10. 19..
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> news;
    private Context context;

    /** Tag for log messages */
    private static final String LOG_TAG = NewsAdapter.class.getName();

    /**
     * Create a new {@link NewsAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param news   is the items_list of {@link News}s to be displayed.
     */
    public NewsAdapter(Context context, List<News> news) {
        this.context = context;
        this.news = news;
    }

    // Create the ViewHolder class for references
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleView;
        private TextView writerView;
        private TextView sectionView;
        private TextView dateView;
        private View parentView;

        // Add a public constructor, instantiate all of the references to the private variables
        public ViewHolder(View view) {
            super(view);
            this.parentView = view;
            this.titleView = (TextView) view.findViewById(R.id.textview_title);
            this.writerView = (TextView) view.findViewById(R.id.textview_writer);
            this.sectionView = (TextView) view.findViewById(R.id.textview_section);
            this.dateView = (TextView) view.findViewById(R.id.textview_date);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the {@link News} object located at this position in list_item layout
        final News currentArticle = news.get(position);

        // Get the title from the current News object and set this text on the title TextView.
        holder.titleView.setText(currentArticle.getNewsTitle());

        // Get the writer from the current News object and set this text on the writer TextView.
        holder.writerView.setText(currentArticle.getNewsWriter());

        // Get the section from the current News object and set this text on the section TextView.
        holder.sectionView.setText(currentArticle.getNewsSection());

        // Take the original format of the published date
        String originalDate = currentArticle.getNewsDate();

        // Take only the yyyy-MM-dd from i.e 2017-10-15T17:52:04Z
        String substringDate = originalDate.substring(0, 10);

        // Format the date
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date date = oldFormat.parse(substringDate);

            String formattedDate = newFormat.format(date);
            // Set the new date format of the current News object on the date TextView.
            holder.dateView.setText(formattedDate);

        } catch (ParseException e) {
            Log.e(LOG_TAG, "Problem parsing dates", e);
        }

        // Attach an OnClickListener to open a current article specific URL
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri articleUri = Uri.parse(currentArticle.getNewsUrl());

                // Create a new intent to view the article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent to launch a new activity
                if (websiteIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(websiteIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.news.size();
    }

    // Helper method to set new news list or clear the previous one
    public void setNewsList(List<News> news){
        this.news = news;
    }

}

