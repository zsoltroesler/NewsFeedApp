package com.example.android.newsfeedapp;

/**
 * Created by Zsolt on 2017. 10. 19..
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from the Guardian API.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Guardian API dataset and return an {@link News} object to represent a list of articles
     */
    public static List<News> fetchNewsData(String requestUrl) {
        Log.i(LOG_TAG, "TEST: fetchNewsData() called...");

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create a {@link News} object
        List<News> news = extractNews(jsonResponse);

        // Return the {@link News}
        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from parsing a JSON response.
     */
    private static List<News> extractNews(String newsJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> news = new ArrayList<>();

        // Try to parse the JSON response. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the newsJSON
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Extract JSONObject with the key called "response"
            JSONObject responseObject = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of news
            JSONArray resultsArray = responseObject.getJSONArray("results");

            // If there are results in the results array, create a news list
            if (responseObject.has("results")) {
                // For each article in the resultsArray, create a {@link News} object
                for (int i = 0; i < resultsArray.length(); i++) {

                    // Get a single article at position i within the list of news
                    JSONObject currentArticle = resultsArray.getJSONObject(i);

                    // Get the title of the current {@link News} object
                    String title = "";
                    if (currentArticle.has("webTitle")) {
                        title = currentArticle.getString("webTitle");
                    }

                    // Get the writer of the current {@link News} object
                    String writer = "";
                    // Get the writer(s) from JSONArray associated with the key called "tags"
                    if (currentArticle.has("tags")) {
                        JSONArray tagsArray = currentArticle.getJSONArray("tags");

                        // newsTags = tagsArray.getJSONObject(0);
                        if (tagsArray.length() > 0) {
                            for (int j = 0; j < 1; j++) {
                                JSONObject singleTag = tagsArray.getJSONObject(j);
                                if (singleTag.has("webTitle")) {
                                    writer = singleTag.getString("webTitle");
                                }
                            }
                        }
                    }

                    // Get the section of the current {@link News} object
                    String section = "";
                    if (currentArticle.has("sectionName")) {
                        section = currentArticle.getString("sectionName");
                    }

                    // Get the date of the current {@link News} object
                    String date = "";
                    if (currentArticle.has("webPublicationDate")) {
                        date = currentArticle.getString("webPublicationDate");
                    }

                    // Get the URL of the current {@link News} object
                    String url = "";
                    if (currentArticle.has("webUrl")) {
                        url = currentArticle.getString("webUrl");
                    }

                    // Create a new article {@link News} object with the title, writer, section, date
                    // and url for info from the JSON response.
                    News article = new News(title, writer, section, date, url);

                    // Add the article {@link News} to the list of news.
                    news.add(article);
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the books JSON results", e);
        }
        return news;
    }
}


