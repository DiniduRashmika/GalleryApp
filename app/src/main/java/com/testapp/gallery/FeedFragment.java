package com.testapp.gallery;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    View view;
    SQLiteHelper dbHelper;
    private List<Feed> feedList;
    private FeedAdapter feedAdapter;
    private RecyclerView imageContainer;
    TextView noimages;
    EditText searchTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_feed, container, false);

        dbHelper = new SQLiteHelper(view.getContext());

        noimages = view.findViewById(R.id.empty_text);

        searchTxt = view.findViewById(R.id.search_txt);

        imagesLoader();
        setupSearchListener();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        imagesLoader();
        setupSearchListener();
    }

    private void imagesLoader() {

        imageContainer = view.findViewById(R.id.image_container);
        dbHelper = new SQLiteHelper(view.getContext());
        feedList = new ArrayList<>();
        feedAdapter = new FeedAdapter(feedList, view.getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        imageContainer.setLayoutManager(layoutManager);

        imageContainer.setAdapter(feedAdapter);


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"user_id","path", "timestamp", "lat", "lon"};
        Cursor cursor = db.query(
                "captures", projection, null, null, null, null, "timestamp DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String userId = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));
                String path = cursor.getString(cursor.getColumnIndexOrThrow("path"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
                String lat = cursor.getString(cursor.getColumnIndexOrThrow("lat"));
                String lon = cursor.getString(cursor.getColumnIndexOrThrow("lon"));

                Feed feed = new Feed(userId, path, timestamp, lat, lon);
                feedList.add(feed);
            } while (cursor.moveToNext());

            cursor.close();

            noimages.setVisibility(View.GONE);
            imageContainer.setVisibility(View.VISIBLE);
            searchTxt.setEnabled(true);
            feedAdapter.notifyDataSetChanged();
        } else {
            imageContainer.setVisibility(View.GONE);
            noimages.setVisibility(View.VISIBLE);
            searchTxt.setEnabled(false);
        }

        db.close();

    }

    private void setupSearchListener() {

        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().trim();
                searchImages(searchText);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void searchImages(String searchText) {

        feedList.clear();

        if (searchText.isEmpty()) {
            imagesLoader();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"user_id", "path", "timestamp", "lat", "lon"};
        String selection = "timestamp LIKE ? ";
        String[] selectionArgs = new String[]{"%" + searchText + "%"};

        Cursor cursor = db.query("captures", projection, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String userid = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));
                String path = cursor.getString(cursor.getColumnIndexOrThrow("path"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
                String lat = cursor.getString(cursor.getColumnIndexOrThrow("lat"));
                String lon = cursor.getString(cursor.getColumnIndexOrThrow("lon"));


                Feed feed = new Feed(userid, path, timestamp, lat, lon);
                feedList.add(feed);

            } while (cursor.moveToNext());

            cursor.close();
            feedAdapter.notifyDataSetChanged();
        }else{
            noimages.setVisibility(View.VISIBLE);
            noimages.setText("No images found");
            imageContainer.setVisibility(View.GONE);
            feedList.clear();
            feedAdapter.notifyDataSetChanged();
        }

        db.close();
    }
}