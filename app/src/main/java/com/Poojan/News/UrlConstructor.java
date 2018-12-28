package com.Poojan.News;

import android.support.annotation.Nullable;
import android.util.Log;




public final class UrlConstructor {

    // Tag for the log messages
    private static final String LOG_TAG = "UrlConstructor";

    // URL Base
    private static final String URL_BASE_1 = "http://meserverdata.000webhostapp.com/poojan.json";
    private static final String URL_BASE_2 = "http://meserverdata.000webhostapp.com/poojan2.json";
    private static final String URL_BASE_3 = "http://meserverdata.000webhostapp.com/poojan3.json";

    public static String constructUrl(@Nullable String section) {

        // Start the StringBuilder and add the URL Base to the beginning
        StringBuilder stringBuilder = new StringBuilder();

        // If the section isn't null then add that
        if (section.equals("dustbin1")) {
            stringBuilder.append(URL_BASE_1);
        } else if(section.equals("dustbin2"))
        {
            stringBuilder.append(URL_BASE_2);
        }
        else
        {
            stringBuilder.append(URL_BASE_3);
        }


        // LOG the API URL
        Log.e(LOG_TAG, "API GUARDIAN_REQUEST_URL: " + stringBuilder.toString());

        return stringBuilder.toString();
    }
}
