package com.varunsen.gurughantal.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.classes.SubjectsAdapter;

import java.util.List;

public class CourseSubjectsActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private int productType;
    private  String productId, productTitle;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_subjects);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.course_subjects_recyclerview);

        if (getIntent() != null){
            productId = getIntent().getStringExtra("product_id");
            productTitle = getIntent().getStringExtra("product_title");
            productType = getIntent().getIntExtra("product_type", 0);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(productTitle);

        //loading dialog
        loadingDialog = new Dialog(CourseSubjectsActivity.this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //end loading dialog

        int noOfColumns = 2;

        GridLayoutManager layoutManager = new GridLayoutManager(this, noOfColumns);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        firebaseFirestore.collection("PurchasedCourses").document(productId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    List<String> subjectNamesList = (List<String>) task.getResult().get("subjects_list");

                    SubjectsAdapter subjectsAdapter = new SubjectsAdapter(subjectNamesList, productId);
                    recyclerView.setAdapter(subjectsAdapter);
                    subjectsAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(CourseSubjectsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

                loadingDialog.dismiss();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}