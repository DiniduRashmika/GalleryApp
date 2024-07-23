package com.testapp.gallery;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    ImageButton captureBtn;
    ImageView logoutBtn;
    SQLiteHelper dbHelper;
    private List<Image> imageList;
    private ImageAdapter imageAdapter;
    private RecyclerView imageContainer;
    TextView noimages, nameTxt;
    EditText searchTxt;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_profile, container, false);

        dbHelper = new SQLiteHelper(view.getContext());

        captureBtn = view.findViewById(R.id.capture_btn);
        logoutBtn = view.findViewById(R.id.logout_btn);
        noimages = view.findViewById(R.id.empty_text);
        nameTxt = view.findViewById(R.id.name_txt);

        searchTxt = view.findViewById(R.id.search_txt);
        searchTxt.setEnabled(false);
        searchTxt.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("Startup", MODE_PRIVATE);
        String useremail = sharedPreferences.getString("email","");

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), CaptureActivity.class);
                startActivity(intent);
            }
        });

        imagesLoader();
        setupSearchListener();

        nameTxt.setText(dbHelper.getUsernameByEmail(useremail));

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to logout ?");
                builder.setTitle("Gallery");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("Startup", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("isStarted", "no");
                    editor.putString("email", "no");
                    editor.apply();

                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

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
        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageList, view.getContext());

        //LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        int spanCount = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), spanCount);
        imageContainer.setLayoutManager(layoutManager);

        imageContainer.setAdapter(imageAdapter);

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("Startup", MODE_PRIVATE);
        String useremail = sharedPreferences.getString("email","");
        String userid = dbHelper.getUserIdByEmail(useremail);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"path", "timestamp", "lat", "lon"};
        String selection = "user_id = ?";
        String[] selectionArgs = { userid };

        Cursor cursor = db.query(
                "captures", projection, selection, selectionArgs, null, null, "timestamp DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String path = cursor.getString(cursor.getColumnIndexOrThrow("path"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
                String lat = cursor.getString(cursor.getColumnIndexOrThrow("lat"));
                String lon = cursor.getString(cursor.getColumnIndexOrThrow("lon"));

                Image image = new Image(path, timestamp, lat, lon);
                imageList.add(image);
            } while (cursor.moveToNext());

            cursor.close();

            noimages.setVisibility(View.GONE);
            imageContainer.setVisibility(View.VISIBLE);
            searchTxt.setEnabled(true);
            imageAdapter.notifyDataSetChanged();
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

        imageList.clear();

        if (searchText.isEmpty()) {
            imagesLoader();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"path", "timestamp", "lat", "lon"};
        String selection = "timestamp LIKE ? ";
        String[] selectionArgs = new String[]{"%" + searchText + "%"};

        Cursor cursor = db.query("captures", projection, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String path = cursor.getString(cursor.getColumnIndexOrThrow("path"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
                String lat = cursor.getString(cursor.getColumnIndexOrThrow("lat"));
                String lon = cursor.getString(cursor.getColumnIndexOrThrow("lon"));


                Image image = new Image(path, timestamp, lat, lon);
                imageList.add(image);

            } while (cursor.moveToNext());

            cursor.close();
            imageAdapter.notifyDataSetChanged();
        }else{
            noimages.setVisibility(View.VISIBLE);
            noimages.setText("No images found");
            imageContainer.setVisibility(View.GONE);
            imageList.clear();
            imageAdapter.notifyDataSetChanged();
        }

        db.close();
    }

}