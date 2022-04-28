package com.varunsen.gurughantal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.classes.ViewAllEeAdapter;
import com.varunsen.gurughantal.classes.ViewAllEeModel;
import com.varunsen.gurughantal.fragments.NotificationFragment;

import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    public static List<ViewAllEeModel> viewAllActivityList;
    private RecyclerView recyclerView;
    private String layoutTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        recyclerView = findViewById(R.id.viewAllRv);

        layoutTitle = getIntent().getStringExtra("layout_title");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(layoutTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        ViewAllEeAdapter adapter = new ViewAllEeAdapter(viewAllActivityList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_action_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        } else{
            return false;
        }

    }
}