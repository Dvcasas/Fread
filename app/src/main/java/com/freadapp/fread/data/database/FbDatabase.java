package com.freadapp.fread.data.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.freadapp.fread.data.model.Article;
import com.freadapp.fread.data.model.Tag;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FbDatabase {

    private static final String TAG = FbDatabase.class.getName();
    private static final String FB_USERS = "users";
    private static final String FB_ARTICLES = "articles";
    private static final String FB_TAGS = "tags";

    private static final FirebaseDatabase sFirebaseDatabase = FirebaseDatabase.getInstance();
    private static final FirebaseAuth sFirebaseAuth = FirebaseAuth.getInstance();

    /**
     * Get a reference of the User's Articles from the Firebase Database
     *
     * @param userUid Firebase User's UID
     */
    public static DatabaseReference getUserArticles(String userUid) {
        return sFirebaseDatabase.getReference().child(FB_USERS).child(userUid).child(FB_ARTICLES);
    }

    /**
     * Get a reference of the User's Tags from the Firebase Database
     *
     * @param userUid Firebase User's UID
     */
    public static DatabaseReference getUserTags(String userUid) {
        return sFirebaseDatabase.getReference().child(FB_USERS).child(userUid).child(FB_TAGS);
    }

    /**
     * Authenticate the Firebase User. Returns Null if no user logged in.
     *
     * @param user Firebase User
     */
    public static FirebaseUser getAuthUser(FirebaseUser user) {
        user = sFirebaseAuth.getCurrentUser();
        if (user != null) {
            return user;
        } else {
            Log.e(TAG, "No user logged in");
            return null;
        }
    }

    /**
     * Create a new tag in the User's tags database reference.
     *
     * @param userTags Database reference of the User Tags
     * @param tagName  Name of tag the user entered
     */
    public static void createNewTag(DatabaseReference userTags, String tagName) {

        Tag tag = new Tag();
        String key = userTags.push().getKey();

        tag.setKeyid(key);
        tag.setTagName(tagName);

        Map<String, Object> writeMap = new HashMap<>();
        writeMap.put(key, tag);
        //update the children of "tags" in the DB with the passed in Hash Map
        userTags.updateChildren(writeMap);

    }

    /**
     * Removes the specified article object found at /articles/$articlekeyid in the database
     *
     * @param context      Application context
     * @param article      Article to be unsaved
     * @param userArticles Database Reference to the User's Articles
     */
    public static void unSaveArticle(Context context, Article article, DatabaseReference userArticles) {

        userArticles.child(article.getKeyid()).removeValue();

        Toast.makeText(context, "Article Unsaved.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Saves the specified Article. If Article is null, creates a new Article and pushes it to the database
     *
     * @param context      Application context
     * @param article      Article to be saved
     * @param userArticles Database Reference to the User's Articles
     * @param url URL received from Intent.EXTRA_TEXT
     * @param uid User's UID
     */
    public static void saveArticle(Context context, Article article, DatabaseReference userArticles, String url, String uid) {

        //create a unique KeyID for the Article
        String key = userArticles.push().getKey();
        //store the keyid and userid into the Article object. This helps for retrieval later.
        article.setKeyid(key);
        article.setUid(uid);
        article.setUrl(url);
        article.setSaved(true);

        //a hash map to store the key (keyid) and value (article object) pair to be saved to the DB
        Map<String, Object> writeMap = new HashMap<>();
        writeMap.put(article.getKeyid(), article);
        //Save the specified article
        userArticles.updateChildren(writeMap);

        Toast.makeText(context, "Article Saved.", Toast.LENGTH_SHORT).show();

    }

    public static void openArticleWebView(Activity activity, String url) {

        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        activity.startActivity(intent);

    }

}
