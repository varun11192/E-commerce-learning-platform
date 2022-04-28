    package com.varunsen.gurughantal.activities;

    import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.classes.ProductImagesAdpater;
import com.varunsen.gurughantal.classes.ViewDialog;

import java.util.ArrayList;
import java.util.List;

    public class ProductDetailsActivity extends AppCompatActivity {

        ViewDialog viewDialog;

    public Dialog dialog;

    private ViewPager pager;
    private TabLayout viewPagerDots;

    // Layout elements

    private TextView titleTv, subtitleTv, sellPriceTv, normalPriceTv, totalRatingsSmallTv,
            totalRatingsBottomMostTv, totalRatingsSmallBelowTv, averageRatingAboveTv,
            averageRatingLargeTv, productDetailsTv, star1Tv, star2Tv, star3Tv, star4Tv, star5Tv;

    private Button buyNowBtn;

    // End Layout elements

    //Data Variables

    private String title, subtitle, sellPrice, normalPrice, averageRating,
            productDetails, productId;
    private List<String> productImagesList = new ArrayList<>();
    private long productType, star1, star2, star3, star4, star5, ratingCount;


    //End Data Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        viewDialog = new ViewDialog(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new Dialog(ProductDetailsActivity.this);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_bacgkround_white));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        //Layout Elements initialization.

        titleTv = findViewById(R.id.prodDetailstitle);
        subtitleTv = findViewById(R.id.prodDetailsSubtitle);
        sellPriceTv = findViewById(R.id.sellPriceTv);
        normalPriceTv = findViewById(R.id.oldSellPriceTv);
        totalRatingsSmallTv = findViewById(R.id.totalRatingsTv);
        averageRatingAboveTv = findViewById(R.id.smallRatingTv);
        totalRatingsBottomMostTv = findViewById(R.id.totalRatingsBottomTv);
        averageRatingLargeTv = findViewById(R.id.largeRatingTv);
        totalRatingsSmallBelowTv = findViewById(R.id.totalRatingsbelowTv);
        productDetailsTv = findViewById(R.id.elaboratedDetailsTv);
        star1Tv = findViewById(R.id.total1StarsRatingTv);
        star2Tv = findViewById(R.id.total2StarsRatingTv);
        star3Tv = findViewById(R.id.total3StarsRatingTv);
        star4Tv = findViewById(R.id.total4StarsRatingTv);
        star5Tv = findViewById(R.id.total5StarsRatingTv);
        buyNowBtn = findViewById(R.id.buyNowBtn);

        //End Layout Elements initialization.

        pager = findViewById(R.id.prodImageViewPager);
        viewPagerDots = findViewById(R.id.viewPagerDots);

        //todo: Loading Dialogue

        if (getIntent() != null){

            productId = getIntent().getStringExtra("product_id");

        }

        //Junk Code

        FirebaseFirestore.getInstance().collection("Products").document(productId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    DocumentSnapshot shot = task.getResult();

                    title = (String) shot.get("product_title");
                    subtitle = (String) shot.get("product_subtitle");
                    productType = (long) shot.get("product_type");
                    ratingCount = (long) shot.get("rating_count");
                    star1  = (long) shot.get("star_1");
                    star2  = (long) shot.get("star_2");
                    star3  = (long) shot.get("star_3");
                    star4  = (long) shot.get("star_4");
                    star5  = (long) shot.get("star_5");
                    sellPrice = (String) shot.get("product_price");
                    normalPrice = (String) shot.get("product_normal_price");
                    productDetails = (String) shot.get("product_details");
                    averageRating = (String) shot.get("average_rating");
                    productImagesList = (List<String>) shot.get("product_images");

                //Setting the data

                titleTv.setText(title);
                subtitleTv.setText(subtitle);
                averageRatingAboveTv.setText(averageRating);
                averageRatingLargeTv.setText(averageRating);
                totalRatingsBottomMostTv.setText("" + ratingCount);
                totalRatingsSmallBelowTv.setText("(" + ratingCount + ") ratings");
                totalRatingsSmallTv.setText("(" + ratingCount + ") ratings");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        productDetailsTv.setText(Html.fromHtml(productDetails, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        productDetailsTv.setText(Html.fromHtml(productDetails));
                    }
                sellPriceTv.setText("Rs. " + sellPrice + "/-");
                normalPriceTv.setText("Rs. " + normalPrice + "/-");
                star5Tv.setText("" + star5);
                star4Tv.setText("" + star4);
                star3Tv.setText("" + star3);
                star2Tv.setText("" + star2);
                star1Tv.setText("" + star1);

                    ProductImagesAdpater adapter = new ProductImagesAdpater(productImagesList);
                    pager.setAdapter(adapter);

                    viewPagerDots.setupWithViewPager(pager, true);

                } else{

                    Toast.makeText(ProductDetailsActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        //End Junk Code



        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);

                deliveryIntent.putExtra("product_title_intent", title);
                deliveryIntent.putExtra("product_sell_price_intent", sellPrice);
                deliveryIntent.putExtra("product_normal_price_intent", normalPrice);
                deliveryIntent.putExtra("product_image_intent", productImagesList.get(0));
                deliveryIntent.putExtra("product_id_intent", productId);

                startActivity(deliveryIntent);

            }
        });

    }

        public void showCustomLoadingDialog(View view) {

            viewDialog.showDialog();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewDialog.hideDialog();
                }
            }, 5000);
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