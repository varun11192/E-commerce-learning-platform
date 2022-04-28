package com.varunsen.gurughantal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.classes.NotificationAdapter;
import com.varunsen.gurughantal.classes.NotificationModelList;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.notificationRecycler);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<NotificationModelList> list = new ArrayList<>();

        list.add(new NotificationModelList("Grapes", 34));
        list.add(new NotificationModelList("apples", 50));
        list.add(new NotificationModelList("apples", 20));
        list.add(new NotificationModelList("apples", 30));
        list.add(new NotificationModelList("apples", 40));
        list.add(new NotificationModelList("apples", 60));

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        adapter = new NotificationAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            getActivity().finish();
            return true;
        } else{
            return false;
        }

    }

}