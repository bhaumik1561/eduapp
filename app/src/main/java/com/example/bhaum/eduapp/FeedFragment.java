package com.example.bhaum.eduapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment  {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<Feed> listItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_feed, container, false);
        final FragmentActivity c = getActivity();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);

        listItems = new ArrayList<>();

        for(int i=0; i<=10; i++)
        {
            Feed listItem = new Feed(
                    "prit","ppsp"
            );
        listItems.add(listItem);
        }

        adapter = new MyFeedAdapter(listItems,getContext() );
        recyclerView.setAdapter(adapter);
        return view;
    }
}
