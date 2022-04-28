package com.varunsen.gurughantal.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.classes.ViewPagerAdapter;
import com.varunsen.gurughantal.fragments.LecturesFragment;
import com.varunsen.gurughantal.fragments.NotesFragment;
import com.varunsen.gurughantal.fragments.TestsFragment;

public class CourseMaterialActivity extends AppCompatActivity {

    public static String subjectName, productId;
    public static FirebaseFirestore firestore;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_material);

        firestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.courseMaterialToolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter.titleList.clear();
        ViewPagerAdapter.fragmentList.clear();

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        adapter.addFragment(new LecturesFragment(), "Lectures");
        adapter.addFragment(new NotesFragment(), "Notes");
        adapter.addFragment(new TestsFragment(), "Tests");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(ViewPagerAdapter.titleList.get(position));
            }
        });

        mediator.attach();

        if (getIntent() != null){
            subjectName = getIntent().getStringExtra("subject_name");
            productId = getIntent().getStringExtra("product_id");

            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(subjectName);

        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        } else {
            return  false;
        }
    }
}