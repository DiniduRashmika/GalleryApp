package com.testapp.gallery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>{

    private List<Feed> feedList;
    private Context context;
    private SQLiteHelper dbHandler;

    public FeedAdapter(List<Feed> feedList, Context context) {
        dbHandler = new SQLiteHelper(context);
        this.feedList = feedList;
        this.context = context;
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.placeholder_feed, parent, false);
        return new FeedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {
        Feed feed = feedList.get(position);
        File imageFile = new File(feed.getImgPath());
        Picasso.get()
                .load(imageFile)
                .fit()
                .placeholder(R.drawable.placeholder)
                .into(holder.objImg);
        holder.dateTxt.setText(feed.getTimestamp());
        holder.nameTxt.setText(dbHandler.getUserNameById(feed.getUserid()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewActivity.class);
                intent.putExtra("path", feed.getImgPath());
                intent.putExtra("userid", feed.getUserid());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView objImg;
        TextView nameTxt, dateTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            objImg = itemView.findViewById(R.id.image_pic);
            nameTxt = itemView.findViewById(R.id.name_txt);
            dateTxt = itemView.findViewById(R.id.date_txt);

        }
    }
}
