package com.example.android.newsfeedapp;

/**
 * Created by Zsolt on 2017. 10. 19..
 */

public class News {

    // String for the title of the article
    private String mNewsTitle;

    // String for the writer of the article
    private String mNewsWriter;

    // String for section of the article
    private String mNewsSection;

    // String for published date of the article
    private String mNewsDate;

    // Website URL of the article
    private String mNewsUrl;

    /**
     * Constructs a new {@link News} object.
     *
     * @param newsTitle
     * @param newsWriter
     * @param newsSection
     * @param newsDate
     * @param newsUrl
     */

    public News(String newsTitle, String newsWriter , String newsSection, String newsDate, String newsUrl) {
        mNewsTitle = newsTitle;
        mNewsWriter = newsWriter;
        mNewsSection = newsSection;
        mNewsDate = newsDate;
        mNewsUrl = newsUrl;
    }

    // Get the title text
    public String getNewsTitle() {
        return mNewsTitle;
    }

    // Get the writer text
    public String getNewsWriter() {
        return mNewsWriter;
    }

    // Get the section text
    public String getNewsSection() {
        return mNewsSection;
    }

    // Get the date text
    public String getNewsDate() {
        return mNewsDate;
    }

    // Returns the website URL to read the full article
    public String getNewsUrl() {
        return mNewsUrl;
    }

}
