
package com.Poojan.News;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewParent;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

/**
 * Helper methods related to requesting and receiving article data from The Guardian.
 */
final class NewsQueryUtils {

    /* urlConnection.setReadTimeout in milliseconds */
    private static final int MAX_READ_TIMEOUT = 10000;
    /* urlConnection.setConnectTimeout in milliseconds */
    private static final int MAX_CONNECTION_TIMEOUT = 15000;/* milliseconds */

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = "NewsQueryUtils";

    /**
     * Create a private constructor because no one should ever create a {@link NewsQueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name NewsQueryUtils (and an object instance of NewsQueryUtils is not needed).
     */
    private NewsQueryUtils() {
    }

    /**
     * Query the Guardian dataset and return a list of {@link NewsArticle} objects.
     */
    static List<NewsArticle> fetchArticleData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link NewsArticle}s

        // Return the list of {@link NewsArticle}s
        return extractFeatureFromJson(jsonResponse);
    }



    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL.", e);
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
            Log.v(LOG_TAG, "jsonResponse: " + jsonResponse);
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(MAX_READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(MAX_CONNECTION_TIMEOUT /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                Log.v(LOG_TAG, "inputStream != null. Closing input stream.");
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
     * Return a list of {@link NewsArticle} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<NewsArticle> extractFeatureFromJson(String articleJSON) {
         String distance;
         String latitude;
         String  longitude;
         String level;
         String voltage;
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(articleJSON)) {
            Log.v(LOG_TAG, "The JSON string is empty or null. Returning early.");
            return null;
        }

        // Create an empty ArrayList that we can start adding newsArticles to
        List<NewsArticle> newsArticles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONArray jsonArrayResults = new JSONArray(articleJSON);

            // For each article in the articleArray, create an {@link NewsArticle} object
            for (int i = 0; i < jsonArrayResults.length(); i++) {

                // Get a single newsArticle at position i within the list of newsArticles
                JSONObject currentArticle = jsonArrayResults.getJSONObject(i);

                // Target the fields object that contains all the elements we need


                // Note:    optString() will return null when fails.
                //          getString() will throw exception when it fails.

                distance = currentArticle.optString("Distance");
                latitude = currentArticle.optString("Latitude");
                longitude = currentArticle.getString("Longitude");
                level = currentArticle.getString("Level");
                voltage = currentArticle.optString("Voltage");

                // Add a new NewsArticle from the data
                newsArticles.add(new NewsArticle(
                        distance,latitude,longitude,level,voltage
                ));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "NewsQueryUtils: Problem parsing the article JSON results", e);
            // Notify the user in a toast
            Toast.makeText(MyApplication.getAppContext(), R.string.no_json_ok_response, Toast.LENGTH_LONG).show();
        }

        // Return the list of newsArticles
        return newsArticles;
    }

    /**
     * Checks to see if there is a network connection when needed
     */
    static boolean isConnected(Context context) {
        boolean connected = false;
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            connected = true;
        }
        return connected;
    }
}