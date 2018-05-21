package com.example.bhaum.eduapp;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.ViewHolder> {

    private List<Feed> listItems;
    private Context context;

    public MyFeedAdapter(List<Feed> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MyFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyFeedAdapter.ViewHolder holder, int position) {
        Feed listItem = listItems.get(position);

        holder.author.setText(listItem.getAuthor());
        holder.content.setText(listItem.getContent());
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView author;
        public TextView content;

        public ViewHolder(View itemView) {
            super(itemView);

            author = (TextView)itemView.findViewById(R.id.author);
            content = (TextView)itemView.findViewById(R.id.content);
        }
    }
}
