
package com.Poojan.News;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * An {@link NewsArticleAdapter} knows how to create a list item layout for each article
 * in the data source (a list of {@link NewsArticle} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsArticleAdapter extends ArrayAdapter<NewsArticle> {
    private Context context;

    /**
     * Constructs a new {@link NewsArticleAdapter}.
     *
     * @param context      of the app
     * @param newsArticles is the list of newsArticles, which is the data source of the adapter
     */
    public NewsArticleAdapter(@NonNull Context context, @NonNull List<NewsArticle> newsArticles) {
        super(context, 0, newsArticles);
        this.context = context;
    }

    /**
     * Returns a list item view that displays information about the article at the given position
     * in the list of articles.
     */
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        // Find the article at the given position in the list of articles
        NewsArticle currentNewsArticle = getItem(position);

        // Get and display the article's Section
        String distance = currentNewsArticle.getDistance();
        TextView sectionNameView = listItemView.findViewById(R.id.article_time);
        sectionNameView.setText(distance);

        // Get and display the article's Title
        String progress = currentNewsArticle.getLevel();
        ProgressBar progresView = listItemView.findViewById(R.id.progress_bar);
        progresView.setProgress(Integer.parseInt(progress));
        TextView level = listItemView.findViewById(R.id.article_title);
        level.setText(progress);

        // Get and display the article's Trail Text
        String voltage = currentNewsArticle.getVoltage();
        TextView trailView = listItemView.findViewById(R.id.article_author);
        trailView.setText(voltage);

//        ImageView photoView = listItemView.findViewById(R.id.article_image);

        ConstraintLayout constraintLayout = listItemView.findViewById(R.id.newslist_constraint_layout);
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);
      //  set.setDimensionRatio(photoView.getId(), "16:9");
        set.applyTo(constraintLayout);
        set.clear(R.id.article_title, ConstraintSet.START);
        // Then attach a new constraint connection.
        //set.connect(R.id.article_title, ConstraintSet.END, R.id.article_image, ConstraintSet.END, 0);
        set.applyTo(constraintLayout);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.abc_grow_fade_in_from_bottom);
        listItemView.startAnimation(animation);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }


}