package com.Poojan.News;



import java.util.Map;

public class HashMapper {

    public static String urlToLabel (String value) {

        // Get the Section Label from the Section URL using a hashmap
        Map<String, String> vars = new java.util.HashMap<>();
        vars.put(
                MyApplication.getAppContext().getResources().getString(R.string.pref_topic_0_label_value),
                MyApplication.getAppContext().getResources().getString(R.string.pref_topic_0_label));
        vars.put(
                MyApplication.getAppContext().getResources().getString(R.string.pref_topic_1_label_value),
                MyApplication.getAppContext().getResources().getString(R.string.pref_topic_1_label));
        vars.put(
                MyApplication.getAppContext().getResources().getString(R.string.pref_topic_2_label_value),
                MyApplication.getAppContext().getResources().getString(R.string.pref_topic_2_label));



        return vars.get(value);
    }
}
