package com.freadapp.fread.view_holder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.freadapp.fread.R;
import com.freadapp.fread.data.model.Article;

/**
 * Created by salaz on 2/20/2018.
 */

public class ArticleViewHolder extends RecyclerView.ViewHolder {

    private TextView mArticleTile;
    private TextView mArticleURL;
    private ImageView mArticleImage;

    public ArticleViewHolder(View itemView) {
        super(itemView);

        mArticleTile = itemView.findViewById(R.id.title_list_item);
        mArticleURL = itemView.findViewById(R.id.url_list_item);
        mArticleImage = itemView.findViewById(R.id.image_list_item);

    }

    /**
     * Binds the passed in Article properties to the populated view.
     * @param article Article object to be applied.
     */
    public void bindToArticle(Article article, Context context){

        mArticleTile.setText(article.getTitle());
        mArticleURL.setText(article.getUrl());
        Glide.with(context).load(article.getImage()).into(mArticleImage);

    }

}
