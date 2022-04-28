package com.varunsen.gurughantal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.classes.HomePageAdapter;
import com.varunsen.gurughantal.classes.HomePageModel;

import java.util.ArrayList;
import java.util.List;

import static com.varunsen.gurughantal.classes.HomePageModel.BANNER;
import static com.varunsen.gurughantal.classes.HomePageModel.GRID;
import static com.varunsen.gurughantal.classes.HomePageModel.HORIZONTAL;

public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView mainRv;
    private FirebaseFirestore firestore;
    private List<HomePageModel> homeList;
    private String categoryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        mainRv = findViewById(R.id.catMainRv);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        firestore = FirebaseFirestore.getInstance();
        homeList = new ArrayList<>();

        LinearLayoutManager homeLm = new LinearLayoutManager(this);
        homeLm.setOrientation(RecyclerView.VERTICAL);
        mainRv.setLayoutManager(homeLm);

        if(getIntent()!=null) {
           categoryName = getIntent().getStringExtra("category_name");
           getSupportActionBar().setTitle(categoryName);
            loadCategory(categoryName);

        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            finish();
        }

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

    private void loadCategory(String categoryName){

        firestore.collection("Categories")
                .document(categoryName)
                .collection("ToDisplay")
                .orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for(DocumentSnapshot shot : task.getResult()){

                        long layoutType = (long) shot.get("layout_type");

                        if(layoutType == BANNER){

                            List<String> bannerList = (List<String>) shot.get("url_list");

                            homeList.add(new HomePageModel(BANNER, bannerList));

                        } else if (layoutType == HORIZONTAL){

                            String layoutTitle = (String) shot.get("layout_title");
                            List<String> productList = (List<String>) shot.get("product_list");

                            homeList.add(new HomePageModel(HORIZONTAL, layoutTitle, productList));

                        } else if (layoutType == GRID){

                            String bgColor = (String) shot.get("bg_color");
                            String layoutTitle = (String) shot.get("layout_title");
                            List<String> productList = (List<String>) shot.get("product_list");

                            homeList.add(new HomePageModel(GRID, layoutTitle, bgColor ,productList));


                        } else {
                            // Don't do anything.
                        }

                    }

                    HomePageAdapter homePageAdapter = new HomePageAdapter(homeList);
                    mainRv.setAdapter(homePageAdapter);
                    homePageAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(CategoriesActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}